package com.heima.model.wemedia.vos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.heima.model.article.pojos.ApArticle;
import lombok.Data;

@Data
public class NewsCommentsVo extends ApArticle {
    /**
     * 是否可评论
     * true: 可以评论   1
     * false: 不可评论  0
     */
    private Boolean isComment;

    /**
     * 是否转发
     * true: 可以转发   1
     * false: 不可转发  0
     */
    private Boolean isForward;

    /**
     * 是否下架
     * true: 下架   1
     * false: 没有下架  0
     */
    private Boolean isDown;

    /**
     * 是否已删除
     * true: 删除   1
     * false: 没有删除  0
     */
    private Boolean isDelete;

    private Integer comments;
}
