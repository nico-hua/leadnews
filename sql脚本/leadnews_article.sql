/*
 Navicat Premium Data Transfer

 Source Server         : docker
 Source Server Type    : MySQL
 Source Server Version : 80038 (8.0.38)
 Source Host           : localhost:3306
 Source Schema         : leadnews_article

 Target Server Type    : MySQL
 Target Server Version : 80038 (8.0.38)
 File Encoding         : 65001

 Date: 25/01/2025 11:39:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ap_article
-- ----------------------------
DROP TABLE IF EXISTS `ap_article`;
CREATE TABLE `ap_article`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `author_id` int UNSIGNED NULL DEFAULT NULL COMMENT '文章作者的ID',
  `author_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '作者昵称',
  `channel_id` int UNSIGNED NULL DEFAULT NULL COMMENT '文章所属频道ID',
  `channel_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '频道名称',
  `layout` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '文章布局\r\n            0 无图文章\r\n            1 单图文章\r\n            2 多图文章',
  `flag` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '文章标记\r\n            0 普通文章\r\n            1 热点文章\r\n            2 置顶文章\r\n            3 精品文章\r\n            4 大V 文章',
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文章图片\r\n            多张逗号分隔',
  `labels` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文章标签最多3个 逗号分隔',
  `likes` int UNSIGNED NULL DEFAULT NULL COMMENT '点赞数量',
  `collection` int UNSIGNED NULL DEFAULT NULL COMMENT '收藏数量',
  `comment` int UNSIGNED NULL DEFAULT NULL COMMENT '评论数量',
  `views` int UNSIGNED NULL DEFAULT NULL COMMENT '阅读数量',
  `province_id` int UNSIGNED NULL DEFAULT NULL COMMENT '省市',
  `city_id` int UNSIGNED NULL DEFAULT NULL COMMENT '市区',
  `county_id` int UNSIGNED NULL DEFAULT NULL COMMENT '区县',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `sync_status` tinyint(1) NULL DEFAULT 0 COMMENT '同步状态',
  `origin` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '来源',
  `static_url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1882035992832118786 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章信息表，存储已发布的文章' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ap_article_config
-- ----------------------------
DROP TABLE IF EXISTS `ap_article_config`;
CREATE TABLE `ap_article_config`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `article_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '文章ID',
  `is_comment` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '是否可评论',
  `is_forward` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '是否转发',
  `is_down` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '是否下架',
  `is_delete` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1882035992928587778 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'APP已发布文章配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ap_article_content
-- ----------------------------
DROP TABLE IF EXISTS `ap_article_content`;
CREATE TABLE `ap_article_content`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `article_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '文章ID',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '文章内容',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1882035992995696643 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'APP已发布文章内容表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ap_author
-- ----------------------------
DROP TABLE IF EXISTS `ap_author`;
CREATE TABLE `ap_author`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '作者名称',
  `type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '0 爬取数据\r\n            1 签约合作商\r\n            2 平台自媒体人\r\n            ',
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '社交账号ID',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `wm_user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '自媒体账号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_type_name`(`type` ASC, `name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = 'APP文章作者信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ap_collection
-- ----------------------------
DROP TABLE IF EXISTS `ap_collection`;
CREATE TABLE `ap_collection`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `entry_id` int UNSIGNED NULL DEFAULT NULL COMMENT '实体ID',
  `article_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '文章ID',
  `type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '点赞内容类型\r\n            0文章\r\n            1动态',
  `collection_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `published_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_type`(`entry_id` ASC, `article_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'APP收藏信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wm_channel
-- ----------------------------
DROP TABLE IF EXISTS `wm_channel`;
CREATE TABLE `wm_channel`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '频道名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '频道描述',
  `is_default` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '是否默认频道',
  `status` tinyint UNSIGNED NULL DEFAULT NULL,
  `ord` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '默认排序',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '频道信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wm_fans_statistics
-- ----------------------------
DROP TABLE IF EXISTS `wm_fans_statistics`;
CREATE TABLE `wm_fans_statistics`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '主账号ID',
  `article` int UNSIGNED NULL DEFAULT NULL COMMENT '子账号ID',
  `read_count` int UNSIGNED NULL DEFAULT NULL,
  `comment` int UNSIGNED NULL DEFAULT NULL,
  `follow` int UNSIGNED NULL DEFAULT NULL,
  `collection` int UNSIGNED NULL DEFAULT NULL,
  `forward` int UNSIGNED NULL DEFAULT NULL,
  `likes` int UNSIGNED NULL DEFAULT NULL,
  `unlikes` int UNSIGNED NULL DEFAULT NULL,
  `unfollow` int UNSIGNED NULL DEFAULT NULL,
  `burst` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_time` date NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_id_time`(`user_id` ASC, `created_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '自媒体粉丝数据统计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wm_material
-- ----------------------------
DROP TABLE IF EXISTS `wm_material`;
CREATE TABLE `wm_material`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '自媒体用户ID',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片地址',
  `type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '素材类型\r\n            0 图片\r\n            1 视频',
  `is_collection` tinyint(1) NULL DEFAULT NULL COMMENT '是否收藏',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '自媒体图文素材信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wm_news
-- ----------------------------
DROP TABLE IF EXISTS `wm_news`;
CREATE TABLE `wm_news`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '自媒体用户ID',
  `title` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '图文内容',
  `type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '文章布局\r\n            0 无图文章\r\n            1 单图文章\r\n            3 多图文章',
  `channel_id` int UNSIGNED NULL DEFAULT NULL COMMENT '图文频道ID',
  `labels` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `submited_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `status` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '当前状态\r\n            0 草稿\r\n            1 提交（待审核）\r\n            2 审核失败\r\n            3 人工审核\r\n            4 人工审核通过\r\n            8 审核通过（待发布）\r\n            9 已发布',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '定时发布时间，不定时则为空',
  `reason` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '拒绝理由',
  `article_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '发布库文章ID',
  `images` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '//图片用逗号分隔',
  `enable` tinyint UNSIGNED NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6232 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '自媒体图文内容信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wm_news_material
-- ----------------------------
DROP TABLE IF EXISTS `wm_news_material`;
CREATE TABLE `wm_news_material`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `material_id` int UNSIGNED NULL DEFAULT NULL COMMENT '素材ID',
  `news_id` int UNSIGNED NULL DEFAULT NULL COMMENT '图文ID',
  `type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '引用类型\r\n            0 内容引用\r\n            1 主图引用',
  `ord` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '引用排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 281 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '自媒体图文引用素材信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wm_news_statistics
-- ----------------------------
DROP TABLE IF EXISTS `wm_news_statistics`;
CREATE TABLE `wm_news_statistics`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '主账号ID',
  `article` int UNSIGNED NULL DEFAULT NULL COMMENT '子账号ID',
  `read_count` int UNSIGNED NULL DEFAULT NULL COMMENT '阅读量',
  `comment` int UNSIGNED NULL DEFAULT NULL COMMENT '评论量',
  `follow` int UNSIGNED NULL DEFAULT NULL COMMENT '关注量',
  `collection` int UNSIGNED NULL DEFAULT NULL COMMENT '收藏量',
  `forward` int UNSIGNED NULL DEFAULT NULL COMMENT '转发量',
  `likes` int UNSIGNED NULL DEFAULT NULL COMMENT '点赞量',
  `unlikes` int UNSIGNED NULL DEFAULT NULL COMMENT '不喜欢',
  `unfollow` int UNSIGNED NULL DEFAULT NULL COMMENT '取消关注量',
  `burst` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_time` date NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_id_time`(`user_id` ASC, `created_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '自媒体图文数据统计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wm_user
-- ----------------------------
DROP TABLE IF EXISTS `wm_user`;
CREATE TABLE `wm_user`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ap_user_id` int NULL DEFAULT NULL,
  `ap_author_id` int NULL DEFAULT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录用户名',
  `password` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录密码',
  `salt` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '盐',
  `nickname` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `location` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '归属地',
  `phone` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '状态\r\n            0 暂时不可用\r\n            1 永久不可用\r\n            9 正常可用',
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '账号类型\r\n            0 个人 \r\n            1 企业\r\n            2 子账号',
  `score` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '运营评分',
  `login_time` datetime NULL DEFAULT NULL COMMENT '最后一次登录时间',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1120 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '自媒体用户信息表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
