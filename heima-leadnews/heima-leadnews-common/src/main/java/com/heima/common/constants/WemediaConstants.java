package com.heima.common.constants;

public class WemediaConstants {

    public static final Short COLLECT_MATERIAL = 1;//收藏

    public static final Short CANCEL_COLLECT_MATERIAL = 0;//取消收藏

    public static final String WM_NEWS_TYPE_IMAGE = "image";

    public static final Short WM_NEWS_NONE_IMAGE = 0;
    public static final Short WM_NEWS_SINGLE_IMAGE = 1;
    public static final Short WM_NEWS_MANY_IMAGE = 3;
    public static final Short WM_NEWS_TYPE_AUTO = -1;

    public static final Short WM_CONTENT_REFERENCE = 0;
    public static final Short WM_COVER_REFERENCE = 1;

    /**
     * 当前状态
     0 草稿
     1 提交（待审核）
     2 审核失败
     3 人工审核
     4 人工审核通过
     8 审核通过（待发布）
     9 已发布
     */
    public static final Short WM_NEWS_STATUS_NORMAL = 0;
    public static final Short WM_NEWS_STATUS_SUBMIT = 1;
    public static final Short WM_NEWS_STATUS_FAIL = 2;
    public static final Short WM_NEWS_STATUS_NEED_REVIEW = 3;
    public static final Short WM_NEWS_STATUS_REVIEW_SUCCESS = 4;
    public static final Short WM_NEWS_STATUS_TO_PUBLISH = 8;
    public static final Short WM_NEWS_STATUS_PUBLISHED = 9;
}