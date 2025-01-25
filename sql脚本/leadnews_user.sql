/*
 Navicat Premium Data Transfer

 Source Server         : docker
 Source Server Type    : MySQL
 Source Server Version : 80038 (8.0.38)
 Source Host           : localhost:3306
 Source Schema         : leadnews_user

 Target Server Type    : MySQL
 Target Server Version : 80038 (8.0.38)
 File Encoding         : 65001

 Date: 25/01/2025 11:39:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ap_user
-- ----------------------------
DROP TABLE IF EXISTS `ap_user`;
CREATE TABLE `ap_user`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `salt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码、通信等加密盐',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码,md5加密',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `sex` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '0 男\r\n            1 女\r\n            2 未知',
  `is_certification` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '0 未\r\n            1 是',
  `is_identity_authentication` tinyint(1) NULL DEFAULT NULL COMMENT '是否身份认证',
  `status` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '0正常\r\n            1锁定',
  `flag` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '0 普通用户\r\n            1 自媒体人\r\n            2 大V',
  `created_time` datetime NULL DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'APP用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ap_user_fan
-- ----------------------------
DROP TABLE IF EXISTS `ap_user_fan`;
CREATE TABLE `ap_user_fan`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `fans_id` int UNSIGNED NULL DEFAULT NULL COMMENT '粉丝ID',
  `fans_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '粉丝昵称',
  `level` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '粉丝忠实度\r\n            0 正常\r\n            1 潜力股\r\n            2 勇士\r\n            3 铁杆\r\n            4 老铁',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `is_display` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '是否可见我动态',
  `is_shield_letter` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '是否屏蔽私信',
  `is_shield_comment` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '是否屏蔽评论',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'APP用户粉丝信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ap_user_follow
-- ----------------------------
DROP TABLE IF EXISTS `ap_user_follow`;
CREATE TABLE `ap_user_follow`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `follow_id` int UNSIGNED NULL DEFAULT NULL COMMENT '关注作者ID',
  `follow_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '粉丝昵称',
  `level` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '关注度\r\n            0 偶尔感兴趣\r\n            1 一般\r\n            2 经常\r\n            3 高度',
  `is_notice` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '是否动态通知',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'APP用户关注信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ap_user_realname
-- ----------------------------
DROP TABLE IF EXISTS `ap_user_realname`;
CREATE TABLE `ap_user_realname`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '账号ID',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名称',
  `idno` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '资源名称',
  `font_image` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '正面照片',
  `back_image` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '背面照片',
  `hold_image` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手持照片',
  `live_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活体照片',
  `status` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '状态\r\n            0 创建中\r\n            1 待审核\r\n            2 审核失败\r\n            9 审核通过',
  `reason` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '拒绝原因',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `submited_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `updated_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'APP实名认证信息表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
