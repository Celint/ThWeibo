-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1:3308
-- 生成日期： 2020-03-13 08:03:22
-- 服务器版本： 10.4.10-MariaDB
-- PHP 版本： 7.4.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `weibo`
--

-- --------------------------------------------------------

--
-- 表的结构 `comment`
--

DROP TABLE IF EXISTS `comment`;
CREATE TABLE IF NOT EXISTS `comment` (
  `comment_id` int(11) DEFAULT NULL,
  `comment_weibo_id` int(11) DEFAULT NULL,
  `comment_username` varchar(32) DEFAULT NULL,
  `comment_content` mediumtext DEFAULT NULL,
  `comment_time` datetime NOT NULL DEFAULT current_timestamp(),
  KEY `comment_weibo_id` (`comment_weibo_id`),
  KEY `comment_username` (`comment_username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `comment`
--

INSERT INTO `comment` (`comment_id`, `comment_weibo_id`, `comment_username`, `comment_content`, `comment_time`) VALUES
(1, 11, 'redsky', '兰彬炎你好', '2019-12-18 21:04:44'),
(1, 12, 'redsky', '那童年的希望是时光机', '2019-12-18 21:09:40'),
(1, 1, 'Binyan', '你好', '2019-12-18 21:12:05'),
(1, 5, 'Binyan', '好听耶', '2019-12-18 21:12:27'),
(2, 12, 'Binyan', '我可以一路开心到底都不换气', '2020-01-03 10:13:54');

-- --------------------------------------------------------

--
-- 表的结构 `myblog`
--

DROP TABLE IF EXISTS `myblog`;
CREATE TABLE IF NOT EXISTS `myblog` (
  `weibo_id` int(11) NOT NULL AUTO_INCREMENT,
  `weibo_username` varchar(32) DEFAULT NULL,
  `weibo_content` mediumtext DEFAULT NULL,
  `weibo_img` varchar(64) DEFAULT NULL,
  `weibo_time` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`weibo_id`),
  KEY `myblog` (`weibo_username`)
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `myblog`
--

INSERT INTO `myblog` (`weibo_id`, `weibo_username`, `weibo_content`, `weibo_img`, `weibo_time`) VALUES
(1, 'redsky', '我是李天红', NULL, '2019-12-15 16:04:20'),
(5, 'redsky', '在专辑《依然范特西》中，周杰伦表示会从旧歌中寻找新意，故《白色风车》便是周杰伦从之前的《黑色毛衣》得到的灵感而创作的出的歌曲。虽然两首歌的歌词都出自周杰伦的手笔，但周杰伦表示后面的创作的《白色风车》难写。周杰伦享用2这首歌来告诉大家，爱情是一种梦幻又真实的，甜蜜而忧伤的复杂情怀。', NULL, '2019-12-18 15:10:46'),
(12, 'Binyan', '《时光机》是周杰伦演唱的一首歌曲，由方文shan作词，收录在周杰伦发行的第九张个人专辑《魔杰座》中，2008年，《时光机》这首歌被选为“2008风尚大典”的主题曲。', NULL, '2019-12-18 16:49:43'),
(11, 'Binyan', '我是兰彬炎', NULL, '2019-12-18 16:46:46');

--
-- 触发器 `myblog`
--
DROP TRIGGER IF EXISTS `tr_delete_blog`;
DELIMITER $$
CREATE TRIGGER `tr_delete_blog` BEFORE DELETE ON `myblog` FOR EACH ROW DELETE FROM comment WHERE comment_weibo_id = OLD.weibo_id
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- 表的结构 `userinfo`
--

DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE IF NOT EXISTS `userinfo` (
  `user_nickname` varchar(32) NOT NULL,
  `user_password` varchar(32) NOT NULL,
  `user_icon` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`user_nickname`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `userinfo`
--

INSERT INTO `userinfo` (`user_nickname`, `user_password`, `user_icon`) VALUES
('redsky', '170405', NULL),
('Binyan', '170402', NULL);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
