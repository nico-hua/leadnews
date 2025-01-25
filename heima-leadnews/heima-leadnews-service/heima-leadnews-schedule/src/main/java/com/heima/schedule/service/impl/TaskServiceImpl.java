package com.heima.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.common.constants.ScheduleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.schedule.pojos.Taskinfo;
import com.heima.model.schedule.pojos.TaskinfoLogs;
import com.heima.schedule.mapper.TaskinfoLogsMapper;
import com.heima.schedule.mapper.TaskinfoMapper;
import com.heima.schedule.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private TaskinfoMapper taskinfoMapper;
    @Autowired
    private TaskinfoLogsMapper taskinfoLogsMapper;
    @Override
    public long addTask(Task task) {
        // 添加任务到数据库
        boolean success = addTaskToDb(task);
        if(success){
            // 添加任务到redis
            addTaskToCache(task);
        }
        return task.getTaskId();
    }

    @Override
    public boolean cancelTask(long taskId) {
        boolean flag = false;
        // 删除任务，更新日志
        Task task = updateDb(taskId, ScheduleConstants.CANCELLED);
        // 删除redis的数据
        if(task!=null){
            removeTaskFromCache(task);
            flag = true;
        }
        return flag;
    }

    @Override
    public Task poll(int type, int priority) {
        Task task = null;
        try{
            String key = type+"_"+priority;
            String task_json = cacheService.lRightPop(ScheduleConstants.TOPIC+key);
            if(StringUtils.isNoneBlank(task_json)){
                task = JSON.parseObject(task_json,Task.class);
                // 更新数据库信息
                updateDb(task.getTaskId(),ScheduleConstants.EXECUTED);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("task poll exception");
        }
        return task;
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh(){
        // redis分布式锁
        String token = cacheService.tryLock("FUTURE_TASK_SYNC", 1000*30);
        if(StringUtils.isNotBlank(token)){
            System.out.println(System.currentTimeMillis()/1000+"执行了定时任务");
            // 获取所有未来数据集合的key值
            Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE+"*");
            for(String futureKey:futureKeys){
                String topicKey = ScheduleConstants.TOPIC+futureKey.split(ScheduleConstants.FUTURE)[1];
                // 获取该组key下当前需要消费的任务数据
                Set<String> tasks = cacheService.zRangeByScore(futureKey,0,System.currentTimeMillis());
                if(!tasks.isEmpty()){
                    // 将这些任务加入消费者队列中
                    cacheService.refreshWithPipeline(futureKey,topicKey,tasks);
                    System.out.println("成功的将" + futureKey + "下的当前需要执行的任务数据刷新到" + topicKey + "下");
                }
            }
        }
    }

    /**
     * 同步数据库数据到redis
     * */
    @Scheduled(cron = "0 */5 * * * ?")
    @PostConstruct
    public void reloadData(){
        clearCache();
        log.info("执行数据库数据同步到缓存");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,5);
        // 查看小于未来5分钟的任务
        List<Taskinfo> allTasks = taskinfoMapper.selectList(Wrappers.<Taskinfo>lambdaQuery().lt(Taskinfo::getExecuteTime,calendar.getTime()));
        if(allTasks!=null&&allTasks.size()>0){
            for(Taskinfo taskinfo:allTasks){
                Task task = new Task();
                BeanUtils.copyProperties(taskinfo,task);
                task.setExecuteTime(taskinfo.getExecuteTime().getTime());
                addTaskToCache(task);
            }
        }
    }

    /**
     * 清除redis缓存
     * */
    private void clearCache(){
        Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE+"*");
        Set<String> topicKeys = cacheService.scan(ScheduleConstants.TOPIC+"*");
        cacheService.delete(futureKeys);
        cacheService.delete(topicKeys);
    }

    /**
     * 删除redis中的任务
     * */
    private void removeTaskFromCache(Task task){
        String key = task.getTaskType()+"_"+task.getPriority();

        if(task.getExecuteTime()<=System.currentTimeMillis()){
            cacheService.lRemove(ScheduleConstants.TOPIC+key,0,JSON.toJSONString(task));
        }
        else{
            cacheService.zRemove(ScheduleConstants.FUTURE+key,JSON.toJSONString(task));
        }
    }

    /**
     * 删除任务，更新日志
     * */
    private Task updateDb(long taskId,int status){
        Task task = null;
        try{
            // 删除任务
            taskinfoMapper.deleteById(taskId);
            // 更新日志
            TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
            taskinfoLogs.setStatus(status);
            taskinfoLogsMapper.updateById(taskinfoLogs);
            // 返回任务
            task = new Task();
            BeanUtils.copyProperties(taskinfoLogs,task);
            task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());
        }catch (Exception e){
            e.printStackTrace();
            log.error("task cancel exception taskid={}",taskId);
        }
        return task;
    }

    /**
     * 添加任务到redis
     * */
    private void addTaskToCache(Task task){
        String key = task.getTaskType()+"_"+task.getPriority();
        // 获取5分钟之后的时间  毫秒值
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,5);
        long nextScheduleTime = calendar.getTimeInMillis();
        // 如果任务的执行时间小于当前时间，则存入list
        if(task.getExecuteTime()<=System.currentTimeMillis()){
            cacheService.lLeftPush(ScheduleConstants.TOPIC+key, JSON.toJSONString(task));
        }
        // 如果任务的执行时间大于当前时间&&小于预设时间（5分钟），则存入zset
        else if(task.getExecuteTime()<=nextScheduleTime){
            cacheService.zAdd(ScheduleConstants.FUTURE+key,JSON.toJSONString(task),task.getExecuteTime());
        }
    }

    /**
     * 添加任务到数据库
     * */
    private boolean addTaskToDb(Task task){
        boolean flag = false;
        try{
            // 保存任务
            Taskinfo taskinfo = new Taskinfo();
            BeanUtils.copyProperties(task,taskinfo);
            taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
            taskinfoMapper.insert(taskinfo);
            // 设置task ID
            task.setTaskId(taskinfo.getTaskId());
            // 保存任务日志
            TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
            BeanUtils.copyProperties(taskinfo,taskinfoLogs);
            taskinfoLogs.setVersion(1);
            taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskinfoLogsMapper.insert(taskinfoLogs);

            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }
}
































