package com.heima.schedule.test;

import com.heima.common.redis.CacheService;
import com.heima.schedule.ScheduleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {
    @Autowired
    private CacheService cacheService;
    @Test
    public void testList(){
        // 在list的左边添加元素
        // cacheService.lLeftPush("list_001","hello,redis!");

        // 在list的右边获取元素，并删除
        String value = cacheService.lRightPop("list_001");
        System.out.println(value);
    }

    @Test
    public void testZset(){
        // 添加数据到zset中
//        cacheService.zAdd("zset_001", "zset_value_001", 1000);
//        cacheService.zAdd("zset_001", "zset_value_002", 888);
//        cacheService.zAdd("zset_001", "zset_value_003", 7777);
//        cacheService.zAdd("zset_001", "zset_value_004", 9999999);

        // 按照分值获取数据
        Set<String> zset_001 = cacheService.zRangeByScore("zset_001", 0, 7777);
        System.out.println(zset_001);
    }
}













