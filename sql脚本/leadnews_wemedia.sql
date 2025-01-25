/*
 Navicat Premium Data Transfer

 Source Server         : docker
 Source Server Type    : MySQL
 Source Server Version : 80038 (8.0.38)
 Source Host           : localhost:3306
 Source Schema         : leadnews_wemedia

 Target Server Type    : MySQL
 Target Server Version : 80038 (8.0.38)
 File Encoding         : 65001

 Date: 25/01/2025 11:39:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '频道信息表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '自媒体图文素材信息表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 6254 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '自媒体图文内容信息表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 322 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '自媒体图文引用素材信息表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '自媒体图文数据统计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wm_sensitive
-- ----------------------------
DROP TABLE IF EXISTS `wm_sensitive`;
CREATE TABLE `wm_sensitive`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sensitives` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '敏感词',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用，1为启用，0为禁用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3204 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '敏感词信息表' ROW_FORMAT = DYNAMIC;

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
