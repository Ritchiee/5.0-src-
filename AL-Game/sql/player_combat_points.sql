/*
Navicat MySQL Data Transfer

Source Server         : Local
Source Server Version : 50611
Source Host           : 127.0.0.1:3306
Source Database       : insance_db

Target Server Type    : MYSQL
Target Server Version : 50611
File Encoding         : 65001

Date: 2016-08-15 12:01:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for player_combat_points
-- ----------------------------
DROP TABLE IF EXISTS `player_combat_points`;
CREATE TABLE `player_combat_points` (
  `player_id` int(11) NOT NULL,
  `slot_id` int(11) NOT NULL,
  `cp_point` int(3) NOT NULL DEFAULT '1',
  PRIMARY KEY (`player_id`,`slot_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
