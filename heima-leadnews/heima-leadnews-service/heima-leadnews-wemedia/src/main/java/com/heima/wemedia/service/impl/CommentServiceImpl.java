package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.apis.article.IArticleClient;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.constants.WmNewsMessageConstants;
import com.heima.model.comment.dto.CommentLikeDto;
import com.heima.model.comment.dto.RepaySaveDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.wemedia.dtos.CommentStatusDto;
import com.heima.model.wemedia.dtos.ListCommentsDto;
import com.heima.model.wemedia.dtos.NewsCommentsDto;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.thread.ApThreadLocalUtil;
import com.heima.utils.thread.WmThreadLocalUtil;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.pojos.ApComment;
import com.heima.wemedia.pojos.ApRepay;
import com.heima.wemedia.service.CommentService;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IArticleClient articleClient;
    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private WmUserMapper wmUserMapper;
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Override
    public PageResponseResult findNewsComments(NewsCommentsDto dto) {
        return articleClient.findNewsComments(dto);
    }

    @Override
    public PageResponseResult list(ListCommentsDto dto) {
        dto.checkParam();
        Query query = new Query();
        query.addCriteria(Criteria.where("entryId").is(dto.getArticleId()));
        query.skip((dto.getPage()-1)*dto.getSize());
        query.limit(dto.getSize());
        query.with(Sort.by(Sort.Direction.DESC,"createdTime"));
        List<ApComment> apComments = mongoTemplate.find(query, ApComment.class, "ap_comments");
        List<Object> data = new ArrayList<>();
        for(ApComment apComment:apComments){
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("commentId").is(apComment.getId()));
            query1.with(Sort.by(Sort.Direction.DESC,"createdTime"));
            List<ApRepay> apRepays = mongoTemplate.find(query1, ApRepay.class, "ap_repays");
            Map<String,Object> map = new HashMap<>();
            map.put("apComments",apComment);
            map.put("apCommentRepays",apRepays);
            data.add(map);
        }
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(),dto.getSize(),data.size());
        pageResponseResult.setData(data);
        return pageResponseResult;
    }

    @Override
    public ResponseResult like(CommentLikeDto dto) {
        if(dto==null||dto.getCommentId()==null||dto.getOperation()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 获取评论ID
        String commentId = dto.getCommentId();
        Short operation = dto.getOperation(); // 0为点赞，1为取消点赞
        // 查询评论
        Query query = new Query(Criteria.where("_id").is(commentId));
        ApComment comment = mongoTemplate.findOne(query, ApComment.class, "ap_comments");
        if (comment == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "评论不存在");
        }
        // 更新点赞数
        Update update = new Update();
        int newLikes = comment.getLikes();
        if (operation == 0) {
            // 点赞，likes数+1
            update.inc("likes", 1);
            newLikes++;
        } else if (operation == 1) {
            // 取消点赞，likes数-1
            if (comment.getLikes() > 0) {
                update.inc("likes", -1);
                newLikes--;
            } else {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "点赞数已经是0，无法取消点赞");
            }
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "操作类型无效");
        }

        // 执行更新
        mongoTemplate.updateFirst(query, update, "ap_comments");
        Map<String,Integer> map = new HashMap<>();
        map.put("likes",newLikes);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult commentRepay(RepaySaveDto dto) throws Exception {
        // 参数校验
        if(dto == null || dto.getCommentId() == null|| dto.getContent() == null||"".equals(dto.getContent())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        // 评论内容审核
        Map map = greenTextScan.greeTextScan(dto.getContent());
        if(!map.get("suggestion").equals("pass")){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"回复内容不正当！");
        }
        // 获取用户信息
        WmUser wmUser = WmThreadLocalUtil.getUser();
        wmUser = wmUserMapper.selectById(wmUser.getId());
        // 保存评论
        ApRepay apRepay = new ApRepay();
        apRepay.setAuthorId(wmUser.getId());
        apRepay.setCommentId(dto.getCommentId());
        apRepay.setAuthorName(wmUser.getName());
        apRepay.setContent(dto.getContent());
        apRepay.setCreatedTime(new Date().getTime());
        apRepay.setType(0);
        apRepay.setImage(wmUser.getImage());
        apRepay.setLikes(0);
        mongoTemplate.save(apRepay);
        // 评论回复数加一
        Query query = new Query(Criteria.where("_id").is(dto.getCommentId()));
        Update update = new Update().inc("reply", 1);
        mongoTemplate.updateFirst(query, update, "ap_comments");
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult updateCommentStatus(CommentStatusDto dto) {
        if(dto==null||dto.getArticleId()==null||dto.getOperation()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("articleId",dto.getArticleId());
        map.put("operation",dto.getOperation());
        kafkaTemplate.send(WmNewsMessageConstants.COMMENT_OPEN_OR_CLOSE_TOPIC, JSON.toJSONString(map));
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult delComment(String commentId) {
        // 校验 commentId 是否有效
        if (StringUtils.isEmpty(commentId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "评论ID不能为空");
        }

        try {
            // 根据 commentId 删除评论
            Query query = new Query(Criteria.where("_id").is(commentId));
            DeleteResult result = mongoTemplate.remove(query, ApComment.class,"ap_comments");

            // 检查删除结果
            if (result.getDeletedCount() > 0) {
                return ResponseResult.okResult("评论删除成功");
            } else {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "评论不存在或已删除");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR, "删除评论时发生错误");
        }
    }

    @Override
    public ResponseResult delCommentRepay(String commentRepayId) {
        // 校验 commentId 是否有效
        if (StringUtils.isEmpty(commentRepayId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "评论回复ID不能为空");
        }

        try {
            // 根据 commentId 删除评论
            Query query = new Query(Criteria.where("_id").is(commentRepayId));
            DeleteResult result = mongoTemplate.remove(query, ApRepay.class,"ap_repays");

            // 检查删除结果
            if (result.getDeletedCount() > 0) {
                return ResponseResult.okResult("评论回复删除成功");
            } else {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "评论回复不存在或已删除");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR, "删除评论回复时发生错误");
        }
    }
}

























