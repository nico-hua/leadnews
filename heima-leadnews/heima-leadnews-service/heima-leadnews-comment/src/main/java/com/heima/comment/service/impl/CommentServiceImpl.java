package com.heima.comment.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.apis.user.IUserClient;
import com.heima.comment.pojos.ApComment;
import com.heima.comment.service.CommentService;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.model.comment.dto.CommentLikeDto;
import com.heima.model.comment.dto.CommentLoadDto;
import com.heima.model.comment.dto.CommentSaveDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.ApThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IUserClient userClient;

    @Autowired
    private GreenTextScan greenTextScan;
    @Override
    public ResponseResult load(CommentLoadDto dto) {
        // 参数校验
        if(dto==null||dto.getArticleId()==null||dto.getIndex()==null||dto.getMinDate()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 构建查询条件
        Query query = new Query();
        query.addCriteria(Criteria.where("entryId").is(dto.getArticleId()));
        query.addCriteria(Criteria.where("createdTime").lt(dto.getMinDate()));
        // 分页设置
        int pageSize = 10;
        int skip = (dto.getIndex()-1)*pageSize;
        query.skip(skip).limit(pageSize);
        // 按创建时间倒序
        query.with(Sort.by(Sort.Direction.DESC,"createdTime"));
        // 执行查询
        List<ApComment> apComments = mongoTemplate.find(query, ApComment.class, "ap_comments");
        return ResponseResult.okResult(apComments);
    }

    @Override
    public ResponseResult save(CommentSaveDto dto) throws Exception {
        // 参数校验
        if(dto == null || dto.getArticleId() == null|| dto.getContent() == null||"".equals(dto.getContent())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        // 评论内容审核
        Map map = greenTextScan.greeTextScan(dto.getContent());
        if(!map.get("suggestion").equals("pass")){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"评论内容不正当！");
        }
        // 获取用户信息
        ApUser user = ApThreadLocalUtil.getUser();
        ResponseResult responseResult = userClient.findUserInfo(user);
        if(responseResult.getCode()!=200){
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
        String apUserString = JSON.toJSONString(responseResult.getData());
        user = JSON.parseObject(apUserString, ApUser.class);
        if(user==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 保存评论
        ApComment apComment = new ApComment();
        apComment.setEntryId(dto.getArticleId());
        apComment.setAuthorId(user.getId());
        apComment.setAuthorName(user.getName());
        apComment.setContent(dto.getContent());
        apComment.setCreatedTime(new Date().getTime());
        apComment.setType(0);
        apComment.setFlag(user.getFlag());
        apComment.setImage(user.getImage());
        apComment.setLikes(0);
        apComment.setReply(0);
        mongoTemplate.save(apComment);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
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
}

















