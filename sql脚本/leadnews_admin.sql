/*
 Navicat Premium Data Transfer

 Source Server         : docker
 Source Server Type    : MySQL
 Source Server Version : 80038 (8.0.38)
 Source Host           : localhost:3306
 Source Schema         : leadnews_admin

 Target Server Type    : MySQL
 Target Server Version : 80038 (8.0.38)
 File Encoding         : 65001

 Date: 25/01/2025 11:38:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ad_article_statistics
-- ----------------------------
DROP TABLE IF EXISTS `ad_article_statistics`;
CREATE TABLE `ad_article_statistics`  (
  `id` int UNSIGNED NOT NULL COMMENT '主键',
  `article_we_media` int UNSIGNED NULL DEFAULT NULL COMMENT '主账号ID',
  `article_crawlers` int UNSIGNED NULL DEFAULT NULL COMMENT '子账号ID',
  `channel_id` int UNSIGNED NULL DEFAULT NULL COMMENT '频道ID',
  `read_20` int UNSIGNED NULL DEFAULT NULL COMMENT '草读量',
  `read_100` int UNSIGNED NULL DEFAULT NULL COMMENT '读完量',
  `read_count` int UNSIGNED NULL DEFAULT NULL COMMENT '阅读量',
  `comment` int UNSIGNED NULL DEFAULT NULL COMMENT '评论量',
  `follow` int UNSIGNED NULL DEFAULT NULL COMMENT '关注量',
  `collection` int UNSIGNED NULL DEFAULT NULL COMMENT '收藏量',
  `forward` int UNSIGNED NULL DEFAULT NULL COMMENT '转发量',
  `likes` int UNSIGNED NULL DEFAULT NULL COMMENT '点赞量',
  `unlikes` int UNSIGNED NULL DEFAULT NULL COMMENT '不喜欢',
  `unfollow` int UNSIGNED NULL DEFAULT NULL COMMENT 'unfollow',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章数据统计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_channel_label
-- ----------------------------
DROP TABLE IF EXISTS `ad_channel_label`;
CREATE TABLE `ad_channel_label`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `channel_id` int UNSIGNED NULL DEFAULT NULL,
  `label_id` int UNSIGNED NULL DEFAULT NULL COMMENT '标签ID',
  `ord` int UNSIGNED NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1118 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '频道标签信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_function
-- ----------------------------
DROP TABLE IF EXISTS `ad_function`;
CREATE TABLE `ad_function`  (
  `id` int UNSIGNED NOT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '功能名称',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '功能代码',
  `parent_id` int UNSIGNED NULL DEFAULT NULL COMMENT '父功能',
  `created_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '页面功能信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_label
-- ----------------------------
DROP TABLE IF EXISTS `ad_label`;
CREATE TABLE `ad_label`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '频道名称',
  `type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '0系统增加\r\n            1人工增加',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24237 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '标签信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_menu
-- ----------------------------
DROP TABLE IF EXISTS `ad_menu`;
CREATE TABLE `ad_menu`  (
  `id` int UNSIGNED NOT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单代码',
  `parent_id` int UNSIGNED NULL DEFAULT NULL COMMENT '父菜单',
  `created_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单资源信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_recommend_strategy
-- ----------------------------
DROP TABLE IF EXISTS `ad_recommend_strategy`;
CREATE TABLE `ad_recommend_strategy`  (
  `id` int UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '策略名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '策略描述',
  `is_enable` tinyint(1) NULL DEFAULT NULL COMMENT '是否有效',
  `group_id` int UNSIGNED NULL DEFAULT NULL COMMENT '分组ID',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '推荐策略信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_role
-- ----------------------------
DROP TABLE IF EXISTS `ad_role`;
CREATE TABLE `ad_role`  (
  `id` int UNSIGNED NOT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色名称',
  `description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
  `is_enable` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '是否有效',
  `created_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `ad_role_auth`;
CREATE TABLE `ad_role_auth`  (
  `id` int UNSIGNED NOT NULL,
  `role_id` int UNSIGNED NULL DEFAULT NULL COMMENT '角色ID',
  `type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '资源类型\r\n            0 菜单\r\n            1 功能',
  `entry_id` int UNSIGNED NULL DEFAULT NULL COMMENT '资源ID',
  `created_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色权限信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_strategy_group
-- ----------------------------
DROP TABLE IF EXISTS `ad_strategy_group`;
CREATE TABLE `ad_strategy_group`  (
  `id` int UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '策略名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '策略描述',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '推荐策略分组信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_user
-- ----------------------------
DROP TABLE IF EXISTS `ad_user`;
CREATE TABLE `ad_user`  (
  `id` int UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录用户名',
  `password` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录密码',
  `salt` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '盐',
  `nickname` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '状态\r\n            0 暂时不可用\r\n            1 永久不可用\r\n            9 正常可用',
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `login_time` datetime NULL DEFAULT NULL COMMENT '最后一次登录时间',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_user_equipment
-- ----------------------------
DROP TABLE IF EXISTS `ad_user_equipment`;
CREATE TABLE `ad_user_equipment`  (
  `id` int UNSIGNED NOT NULL,
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '0PC\r\n            1ANDROID\r\n            2IOS\r\n            3PAD\r\n            9 其他',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备版本',
  `sys` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备系统',
  `no` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备唯一号，MD5加密',
  `created_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员设备信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_user_login
-- ----------------------------
DROP TABLE IF EXISTS `ad_user_login`;
CREATE TABLE `ad_user_login`  (
  `id` int UNSIGNED NOT NULL,
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `equipment_id` int UNSIGNED NULL DEFAULT NULL COMMENT '登录设备ID',
  `ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录IP',
  `address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录地址',
  `longitude` decimal(5, 5) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(5, 5) NULL DEFAULT NULL COMMENT '纬度',
  `created_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员登录行为信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_user_opertion
-- ----------------------------
DROP TABLE IF EXISTS `ad_user_opertion`;
CREATE TABLE `ad_user_opertion`  (
  `id` int UNSIGNED NOT NULL,
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `equipment_id` int UNSIGNED NULL DEFAULT NULL COMMENT '登录设备ID',
  `ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录IP',
  `address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录地址',
  `type` int NULL DEFAULT NULL COMMENT '操作类型',
  `description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作描述',
  `created_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员操作行为信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_user_role
-- ----------------------------
DROP TABLE IF EXISTS `ad_user_role`;
CREATE TABLE `ad_user_role`  (
  `id` int UNSIGNED NOT NULL,
  `role_id` int UNSIGNED NULL DEFAULT NULL COMMENT '角色ID',
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `created_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ad_vistor_statistics
-- ----------------------------
DROP TABLE IF EXISTS `ad_vistor_statistics`;
CREATE TABLE `ad_vistor_statistics`  (
  `id` int UNSIGNED NOT NULL COMMENT '主键',
  `activity` int UNSIGNED NULL DEFAULT NULL COMMENT '日活',
  `vistor` int UNSIGNED NULL DEFAULT NULL COMMENT '访问量',
  `ip` int UNSIGNED NULL DEFAULT NULL COMMENT 'IP量',
  `register` int UNSIGNED NULL DEFAULT NULL COMMENT '注册量',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '访问数据统计表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
