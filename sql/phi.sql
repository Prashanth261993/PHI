CREATE DATABASE  IF NOT EXISTS `phi` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `phi`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: phi
-- ------------------------------------------------------
-- Server version	5.7.15-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alerts`
--

DROP TABLE IF EXISTS `alerts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alerts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `observation_type_id` int(11) DEFAULT NULL,
  `alert_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `observation_type_id` (`observation_type_id`),
  CONSTRAINT `alerts_ibfk_1` FOREIGN KEY (`observation_type_id`) REFERENCES `observation` (`observation_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alerts`
--

LOCK TABLES `alerts` WRITE;
/*!40000 ALTER TABLE `alerts` DISABLE KEYS */;
INSERT INTO `alerts` VALUES (1,'Out of limits',1,NULL,NULL),(2,'Out of limits',1,NULL,'2005-05-16'),(3,'Out of limits',1,NULL,'2016-05-23');
/*!40000 ALTER TABLE `alerts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blood_pressure`
--

DROP TABLE IF EXISTS `blood_pressure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blood_pressure` (
  `systolic_value` int(11) NOT NULL,
  `diastolic_value` int(11) NOT NULL,
  `observation_id` int(11) NOT NULL,
  PRIMARY KEY (`observation_id`),
  CONSTRAINT `blood_pressure_ibfk_1` FOREIGN KEY (`observation_id`) REFERENCES `observation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blood_pressure`
--

LOCK TABLES `blood_pressure` WRITE;
/*!40000 ALTER TABLE `blood_pressure` DISABLE KEYS */;
/*!40000 ALTER TABLE `blood_pressure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagnosis`
--

DROP TABLE IF EXISTS `diagnosis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagnosis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnosis`
--

LOCK TABLES `diagnosis` WRITE;
/*!40000 ALTER TABLE `diagnosis` DISABLE KEYS */;
INSERT INTO `diagnosis` VALUES (0,'Well Paient','Well Patient'),(1,'Heart Disease','Heart Disease'),(2,'HIV','HIV'),(3,'Diabetes','Type-II Diabetes'),(4,'SLE','Lupus'),(5,'Cancer','Cancer'),(6,'Lung mass','Lung mass'),(7,'COPD','COPD');
/*!40000 ALTER TABLE `diagnosis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_supporter`
--

DROP TABLE IF EXISTS `health_supporter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `health_supporter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `address` varchar(200) NOT NULL,
  `phone_num` varchar(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `health_supporter_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_supporter`
--

LOCK TABLES `health_supporter` WRITE;
/*!40000 ALTER TABLE `health_supporter` DISABLE KEYS */;
INSERT INTO `health_supporter` VALUES (1,'Leonard Hofstader','Sacramento SC','+1-9199170098',2),(2,'Penny Hofstader','2500-204 Sacramento','+1-9199170098',3),(3,'Amy Farrahfowler','2500-204 Sacramento','+1-9199170098',4);
/*!40000 ALTER TABLE `health_supporter` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER hs_phone_check BEFORE INSERT ON health_supporter
FOR EACH ROW 
BEGIN 
IF (NEW.phone_num REGEXP '^(\\+?[0-9]{1,4}-)?[0-9]{3,10}$' ) = 0 THEN 
  SIGNAL SQLSTATE '12345'
     SET MESSAGE_TEXT = 'Wrong phone number format!!!';
END IF; 
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `hs_manages_patient`
--

DROP TABLE IF EXISTS `hs_manages_patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hs_manages_patient` (
  `p_id` int(11) DEFAULT NULL,
  `hs_id` int(11) DEFAULT NULL,
  `primary_ind` tinyint(1) DEFAULT '0',
  `relation` varchar(15) DEFAULT NULL,
  `auth_date` datetime DEFAULT CURRENT_TIMESTAMP,
  KEY `p_id` (`p_id`),
  KEY `hs_id` (`hs_id`),
  CONSTRAINT `hs_manages_patient_ibfk_1` FOREIGN KEY (`p_id`) REFERENCES `patient` (`id`),
  CONSTRAINT `hs_manages_patient_ibfk_2` FOREIGN KEY (`hs_id`) REFERENCES `health_supporter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hs_manages_patient`
--

LOCK TABLES `hs_manages_patient` WRITE;
/*!40000 ALTER TABLE `hs_manages_patient` DISABLE KEYS */;
INSERT INTO `hs_manages_patient` VALUES (2,2,1,'family','2016-10-09 00:00:00'),(3,3,1,'family','2016-10-21 00:00:00'),(1,1,0,'social worker','2016-10-22 00:00:00'),(1,3,1,'family','2016-10-22 00:00:00');
/*!40000 ALTER TABLE `hs_manages_patient` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER pat_hs_primary_ind_check BEFORE INSERT ON hs_manages_patient
FOR EACH ROW 
BEGIN 
IF NEW.primary_ind NOT IN (0,1) THEN 
  SIGNAL SQLSTATE '12345'
     SET MESSAGE_TEXT = 'Invalid primary relation(should be either 0 or 1 only)!!!';
END IF; 
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER pat_hs_relation_check BEFORE INSERT ON hs_manages_patient
FOR EACH ROW 
BEGIN 
IF NEW.relation NOT IN ('family','friend','social worker') THEN 
  SIGNAL SQLSTATE '12345'
     SET MESSAGE_TEXT = 'Invalid relation between patient and health supporter!!!';
END IF; 
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER patient_cant_add_self_as_hs_check BEFORE INSERT ON hs_manages_patient
FOR EACH ROW 
BEGIN 

IF (select count(*) from health_supporter hs, patient p, user_info u where hs.user_id = u.id and p.user_id = u.id and p.id = NEW.p_id and hs.id=NEW.hs_id) > 0 THEN
  SIGNAL SQLSTATE '12345'
     SET MESSAGE_TEXT = 'Patient cannnot add self as a health supporter!!!';
END IF; 
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER sick_patient_hs_count_check BEFORE DELETE ON hs_manages_patient
FOR EACH ROW 
BEGIN 

declare hs_count int;
set hs_count = (select count(*) from hs_manages_patient hsp where hsp.p_id = OLD.p_id);

IF hs_count < 2 and (select count(*) from patient_diagnosis pd where pd.p_id = OLD.p_id) > 0 THEN
  SIGNAL SQLSTATE '12345'
     SET MESSAGE_TEXT = 'Sick patient should have atleast one health supporter!!!';
END IF; 
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `mood`
--

DROP TABLE IF EXISTS `mood`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mood` (
  `value` varchar(30) NOT NULL,
  `observation_id` int(11) NOT NULL,
  PRIMARY KEY (`observation_id`),
  CONSTRAINT `mood_ibfk_1` FOREIGN KEY (`observation_id`) REFERENCES `observation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mood`
--

LOCK TABLES `mood` WRITE;
/*!40000 ALTER TABLE `mood` DISABLE KEYS */;
INSERT INTO `mood` VALUES ('1',16);
/*!40000 ALTER TABLE `mood` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `observation`
--

DROP TABLE IF EXISTS `observation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `observation_type_id` int(11) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `observation_date` datetime DEFAULT NULL,
  `recorded_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `observation_type_id` (`observation_type_id`),
  KEY `pid` (`pid`),
  CONSTRAINT `observation_ibfk_1` FOREIGN KEY (`observation_type_id`) REFERENCES `observation_type` (`observation_type_id`),
  CONSTRAINT `observation_ibfk_2` FOREIGN KEY (`pid`) REFERENCES `patient` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observation`
--

LOCK TABLES `observation` WRITE;
/*!40000 ALTER TABLE `observation` DISABLE KEYS */;
INSERT INTO `observation` VALUES (1,1,2,'2016-10-10 00:00:00','2016-10-11 00:00:00'),(2,1,2,'2016-10-17 00:00:00','2016-10-17 00:00:00'),(3,1,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(4,1,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(5,1,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(6,1,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(7,1,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(8,1,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(9,1,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(10,1,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(11,1,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(12,1,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(13,1,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(14,5,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(15,5,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(16,5,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(17,6,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(18,6,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(19,3,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(20,3,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(21,3,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(22,4,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(23,3,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(24,4,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(25,4,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(26,6,1,'2016-01-01 00:00:00','2016-10-25 00:00:00'),(27,1,1,'2015-01-12 00:00:00','2016-10-26 00:00:00');
/*!40000 ALTER TABLE `observation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `observation_requirement`
--

DROP TABLE IF EXISTS `observation_requirement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observation_requirement` (
  `observation_type_id` int(11) NOT NULL,
  `obs_subtype_id` int(11) DEFAULT NULL,
  `hs_id` int(11) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `diagnosis_id` int(11) DEFAULT NULL,
  `lower_limit` int(11) DEFAULT NULL,
  `upper_limit` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `required_ind` tinyint(1) DEFAULT '1',
  `frequency` int(11) DEFAULT NULL,
  `alert_threshold` int(11) NOT NULL DEFAULT '1',
  KEY `observation_type_id` (`observation_type_id`),
  KEY `obs_subtype_id` (`obs_subtype_id`),
  KEY `hs_id` (`hs_id`),
  KEY `pid` (`pid`),
  KEY `diagnosis_id` (`diagnosis_id`),
  KEY `observation_type_id_2` (`observation_type_id`,`obs_subtype_id`),
  CONSTRAINT `observation_requirement_ibfk_1` FOREIGN KEY (`observation_type_id`) REFERENCES `observation_type` (`observation_type_id`),
  CONSTRAINT `observation_requirement_ibfk_3` FOREIGN KEY (`hs_id`) REFERENCES `health_supporter` (`id`),
  CONSTRAINT `observation_requirement_ibfk_4` FOREIGN KEY (`pid`) REFERENCES `patient` (`id`),
  CONSTRAINT `observation_requirement_ibfk_5` FOREIGN KEY (`diagnosis_id`) REFERENCES `diagnosis` (`id`),
  CONSTRAINT `observation_requirement_ibfk_6` FOREIGN KEY (`observation_type_id`, `obs_subtype_id`) REFERENCES `observation_sub_type` (`observation_type_id`, `obs_subtype_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observation_requirement`
--

LOCK TABLES `observation_requirement` WRITE;
/*!40000 ALTER TABLE `observation_requirement` DISABLE KEYS */;
INSERT INTO `observation_requirement` VALUES (1,1,NULL,NULL,1,120,200,NULL,1,7,1),(2,1,NULL,NULL,1,140,159,NULL,1,1,1),(2,2,NULL,NULL,1,90,99,NULL,1,1,1),(5,5,NULL,NULL,1,1,1,NULL,1,1,1),(1,1,NULL,NULL,2,120,200,NULL,1,7,1),(2,1,NULL,NULL,2,NULL,NULL,NULL,1,1,1),(2,2,NULL,NULL,2,NULL,NULL,NULL,1,1,1),(4,4,NULL,NULL,2,1,5,NULL,1,1,1),(3,3,NULL,NULL,7,90,99,NULL,1,1,1),(6,6,NULL,NULL,7,95,100,NULL,1,1,1),(1,1,NULL,NULL,0,120,200,NULL,0,7,1),(1,1,2,2,2,120,190,'2016-10-10 00:00:00',1,7,1),(2,1,2,2,2,NULL,NULL,'2016-10-10 00:00:00',1,1,1),(2,2,2,2,2,NULL,NULL,'2016-10-10 00:00:00',1,1,1),(4,4,2,2,2,1,5,'2016-10-10 00:00:00',1,1,1);
/*!40000 ALTER TABLE `observation_requirement` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER observation_requirement_freq_check BEFORE INSERT ON observation_requirement
FOR EACH ROW 
BEGIN 
IF NEW.frequency < 1 THEN 
  SIGNAL SQLSTATE '12345'
     SET MESSAGE_TEXT = 'Frequency cannot be less than 1 day!!!';
END IF; 
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `observation_sub_type`
--

DROP TABLE IF EXISTS `observation_sub_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observation_sub_type` (
  `observation_type_id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `obs_subtype_id` int(11) NOT NULL,
  PRIMARY KEY (`observation_type_id`,`obs_subtype_id`),
  UNIQUE KEY `name` (`name`),
  KEY `observation_type_id` (`observation_type_id`),
  CONSTRAINT `observation_sub_type_ibfk_1` FOREIGN KEY (`observation_type_id`) REFERENCES `observation_type` (`observation_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observation_sub_type`
--

LOCK TABLES `observation_sub_type` WRITE;
/*!40000 ALTER TABLE `observation_sub_type` DISABLE KEYS */;
INSERT INTO `observation_sub_type` VALUES (2,'Diastolic',2),(5,'Mood',5),(4,'Pain',4),(3,'SPo2',3),(2,'Systolic',1),(6,'Temperature',6),(1,'Weight',1);
/*!40000 ALTER TABLE `observation_sub_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `observation_type`
--

DROP TABLE IF EXISTS `observation_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observation_type` (
  `observation_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `description` text NOT NULL,
  `metric` varchar(10) NOT NULL,
  PRIMARY KEY (`observation_type_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observation_type`
--

LOCK TABLES `observation_type` WRITE;
/*!40000 ALTER TABLE `observation_type` DISABLE KEYS */;
INSERT INTO `observation_type` VALUES (1,'Weight','Weight in kg','kg'),(2,'Blood Pressure','Blood Pressure in mmHg','mmHg'),(3,'SPo2','Oxygen Saturation','%'),(4,'Pain','Pain Intensity',''),(5,'Mood','Patient Mood',''),(6,'Temperature','Body Temperature in F','F');
/*!40000 ALTER TABLE `observation_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oxygen_saturation`
--

DROP TABLE IF EXISTS `oxygen_saturation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oxygen_saturation` (
  `value` double NOT NULL,
  `observation_id` int(11) NOT NULL,
  PRIMARY KEY (`observation_id`),
  CONSTRAINT `oxygen_saturation_ibfk_1` FOREIGN KEY (`observation_id`) REFERENCES `observation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oxygen_saturation`
--

LOCK TABLES `oxygen_saturation` WRITE;
/*!40000 ALTER TABLE `oxygen_saturation` DISABLE KEYS */;
INSERT INTO `oxygen_saturation` VALUES (20,21),(40,23);
/*!40000 ALTER TABLE `oxygen_saturation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pain_intensity`
--

DROP TABLE IF EXISTS `pain_intensity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pain_intensity` (
  `value` int(11) NOT NULL,
  `observation_id` int(11) NOT NULL,
  PRIMARY KEY (`observation_id`),
  CONSTRAINT `pain_intensity_ibfk_1` FOREIGN KEY (`observation_id`) REFERENCES `observation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pain_intensity`
--

LOCK TABLES `pain_intensity` WRITE;
/*!40000 ALTER TABLE `pain_intensity` DISABLE KEYS */;
INSERT INTO `pain_intensity` VALUES (5,25);
/*!40000 ALTER TABLE `pain_intensity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `DOB` date NOT NULL,
  `address` varchar(200) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `phone_num` varchar(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `patient_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (1,'P1','1984-05-05','New Jersey NY','male','+1-9199170098',1),(2,'Leonard Hofstader','1980-10-22','Chapel Hill RTP','male','+1-9199170098',2),(3,'Penny Hofstader','1989-12-25','2500-204 Sacramento','female','+1-9199170098',3),(4,'Amy Farrahfowler','1989-06-15','2500-204 Sacramento','female','+1-9199170098',4);
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER pat_gender BEFORE INSERT ON patient
FOR EACH ROW 
BEGIN 
IF NEW.gender NOT IN ('male','female','others') THEN 
  SIGNAL SQLSTATE '12345'
     SET MESSAGE_TEXT = 'Invalid gender!!!';
END IF; 
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER pat_phone_check BEFORE INSERT ON patient
FOR EACH ROW 
BEGIN 
IF (NEW.phone_num REGEXP '^(\\+?[0-9]{1,4}-)?[0-9]{3,10}$' ) = 0 THEN 
  SIGNAL SQLSTATE '12345'
     SET MESSAGE_TEXT = 'Wrong phone number format!!!';
END IF; 
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `patient_diagnosis`
--

DROP TABLE IF EXISTS `patient_diagnosis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_diagnosis` (
  `p_id` int(11) NOT NULL,
  `d_id` int(11) NOT NULL,
  `diagnosis_date` datetime DEFAULT NULL,
  PRIMARY KEY (`p_id`,`d_id`),
  KEY `d_id` (`d_id`),
  CONSTRAINT `patient_diagnosis_ibfk_1` FOREIGN KEY (`p_id`) REFERENCES `patient` (`id`),
  CONSTRAINT `patient_diagnosis_ibfk_2` FOREIGN KEY (`d_id`) REFERENCES `diagnosis` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_diagnosis`
--

LOCK TABLES `patient_diagnosis` WRITE;
/*!40000 ALTER TABLE `patient_diagnosis` DISABLE KEYS */;
INSERT INTO `patient_diagnosis` VALUES (1,5,'2016-10-22 00:00:00'),(2,2,'2016-01-01 00:00:00');
/*!40000 ALTER TABLE `patient_diagnosis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recommended_observations`
--

DROP TABLE IF EXISTS `recommended_observations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recommended_observations` (
  `observation_type_id` int(11) NOT NULL,
  `subtype_id` int(11) NOT NULL,
  `hs_id` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  PRIMARY KEY (`hs_id`,`pid`,`observation_type_id`,`subtype_id`),
  KEY `observation_type_id` (`observation_type_id`),
  KEY `subtype_id` (`subtype_id`),
  KEY `pid` (`pid`),
  KEY `observation_type_id_2` (`observation_type_id`,`subtype_id`),
  CONSTRAINT `recommended_observations_ibfk_1` FOREIGN KEY (`hs_id`) REFERENCES `health_supporter` (`id`),
  CONSTRAINT `recommended_observations_ibfk_2` FOREIGN KEY (`observation_type_id`) REFERENCES `observation_type` (`observation_type_id`),
  CONSTRAINT `recommended_observations_ibfk_4` FOREIGN KEY (`pid`) REFERENCES `patient` (`id`),
  CONSTRAINT `recommended_observations_ibfk_5` FOREIGN KEY (`observation_type_id`, `subtype_id`) REFERENCES `observation_sub_type` (`observation_type_id`, `obs_subtype_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recommended_observations`
--

LOCK TABLES `recommended_observations` WRITE;
/*!40000 ALTER TABLE `recommended_observations` DISABLE KEYS */;
INSERT INTO `recommended_observations` VALUES (1,1,2,2),(2,1,2,2),(2,2,2,2),(4,4,2,2);
/*!40000 ALTER TABLE `recommended_observations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `required_observations`
--

DROP TABLE IF EXISTS `required_observations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `required_observations` (
  `diagnosis_id` int(11) NOT NULL,
  `observation_type_id` int(11) NOT NULL,
  `subtype_id` int(11) NOT NULL,
  PRIMARY KEY (`diagnosis_id`,`observation_type_id`,`subtype_id`),
  KEY `observation_type_id` (`observation_type_id`),
  KEY `subtype_id` (`subtype_id`),
  KEY `observation_type_id_2` (`observation_type_id`,`subtype_id`),
  CONSTRAINT `required_observations_ibfk_1` FOREIGN KEY (`diagnosis_id`) REFERENCES `diagnosis` (`id`),
  CONSTRAINT `required_observations_ibfk_2` FOREIGN KEY (`observation_type_id`) REFERENCES `observation_type` (`observation_type_id`),
  CONSTRAINT `required_observations_ibfk_3` FOREIGN KEY (`observation_type_id`, `subtype_id`) REFERENCES `observation_sub_type` (`observation_type_id`, `obs_subtype_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `required_observations`
--

LOCK TABLES `required_observations` WRITE;
/*!40000 ALTER TABLE `required_observations` DISABLE KEYS */;
INSERT INTO `required_observations` VALUES (0,1,1),(1,1,1),(2,1,1),(1,2,1),(1,2,2),(2,2,1),(2,2,2),(7,3,3),(2,4,4),(1,5,5),(7,6,6);
/*!40000 ALTER TABLE `required_observations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `temperature`
--

DROP TABLE IF EXISTS `temperature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temperature` (
  `value` int(11) NOT NULL,
  `observation_id` int(11) NOT NULL,
  PRIMARY KEY (`observation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `temperature`
--

LOCK TABLES `temperature` WRITE;
/*!40000 ALTER TABLE `temperature` DISABLE KEYS */;
INSERT INTO `temperature` VALUES (60,26);
/*!40000 ALTER TABLE `temperature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `password` varchar(50) NOT NULL,
  `sec_question` varchar(100) DEFAULT 'Your favorite color?',
  `sec_answer` varchar(30) DEFAULT 'RGB',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (1,'P1','password','Favorite series','GoT'),(2,'P2','password','Favorite series','Walking Dead'),(3,'P3','password','Favorite series','Newshour'),(4,'P4','password','Favorite series','Walking Dead');
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `weight`
--

DROP TABLE IF EXISTS `weight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weight` (
  `value` double NOT NULL,
  `observation_id` int(11) NOT NULL,
  PRIMARY KEY (`observation_id`),
  CONSTRAINT `weight_ibfk_1` FOREIGN KEY (`observation_id`) REFERENCES `observation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `weight`
--

LOCK TABLES `weight` WRITE;
/*!40000 ALTER TABLE `weight` DISABLE KEYS */;
INSERT INTO `weight` VALUES (180,1),(195,2),(2,12),(50,13),(74,27);
/*!40000 ALTER TABLE `weight` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'phi'
--
/*!50106 SET @save_time_zone= @@TIME_ZONE */ ;
/*!50106 DROP EVENT IF EXISTS `lowAlerts` */;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8 */ ;;
/*!50003 SET character_set_results = utf8 */ ;;
/*!50003 SET collation_connection  = utf8_general_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`localhost`*/ /*!50106 EVENT `lowAlerts` ON SCHEDULE EVERY 1 DAY STARTS '2016-10-26 14:06:54' ON COMPLETION NOT PRESERVE ENABLE DO CALL createLowActivityAlerts() */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
DELIMITER ;
/*!50106 SET TIME_ZONE= @save_time_zone */ ;

--
-- Dumping routines for database 'phi'
--
/*!50003 DROP PROCEDURE IF EXISTS `createLowActivityAlert` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `createLowActivityAlert`()
BEGIN
	IF ( Select count(*) from observation inner join patient_diagnosis on (observation.pid = patient_diagnosis.p_id) inner join observation_requirement on (observation.observation_type_id = observation_requirement.observation_type_id and observation.pid = observation_requirement.pid and patient_diagnosis.d_id = observation_requirement.diagnosis_id ) where id in (select o.id from observation o left join observation o2 on o.observation_type_id = o2.observation_type_id and o.observation_date < o2.observation_date where o2.observation_date IS NULL) and datediff(CURDATE(), observation_date + frequency) >= alert_threshold) > 0 THEN
	BEGIN
		DECLARE var1 INT;
		DECLARE var2 INT;
		DECLARE var3 INT;
		DECLARE bDone INT;
		DECLARE curs CURSOR FOR Select id, observation.pid, observation.observation_type_id  from observation inner join patient_diagnosis on (observation.pid = patient_diagnosis.p_id) inner join observation_requirement on (observation.observation_type_id = observation_requirement.observation_type_id and observation.pid = observation_requirement.pid and patient_diagnosis.d_id = observation_requirement.diagnosis_id ) where id in (select o.id from observation o left join observation o2 on o.observation_type_id = o2.observation_type_id and o.observation_date < o2.observation_date where o2.observation_date IS NULL) and datediff(CURDATE(), observation_date + frequency) >= alert_threshold;
		DECLARE CONTINUE HANDLER FOR NOT FOUND SET bDone = 1;

		OPEN curs;

		SET bDone = 0;
		add_alerts1: LOOP
			FETCH curs INTO var1, var2, var3;
            IF bDone = 1 THEN
				LEAVE add_alerts1;
			END IF;
			INSERT INTO alerts VALUES(NULL,'Low Activity',var2,var3,CURDATE());
		END LOOP add_alerts1;
		CLOSE curs;
	END;
	END IF;
	IF (Select count(*) from observation inner join patient_diagnosis on (observation.pid = patient_diagnosis.p_id) inner join observation_requirement on (observation.observation_type_id = observation_requirement.observation_type_id and patient_diagnosis.d_id = observation_requirement.diagnosis_id ) where id in (select o.id from observation o left join observation o2 on o.observation_type_id = o2.observation_type_id and o.pid = o2.pid and o.observation_date < o2.observation_date where o2.observation_date IS NULL) and datediff(CURDATE(), observation_date + frequency) >= alert_threshold and id not in (Select id from observation inner join patient_diagnosis on (observation.pid = patient_diagnosis.p_id) inner join observation_requirement on (observation.observation_type_id = observation_requirement.observation_type_id and patient_diagnosis.d_id = observation_requirement.diagnosis_id ) where id in (select o.id from observation o left join observation o2 on o.observation_type_id = o2.observation_type_id and o.pid = o2.pid and o.observation_date < o2.observation_date where o2.observation_date IS NULL) and datediff(CURDATE(), observation_date + frequency) >= alert_threshold group by id, observation.pid, observation.observation_type_id, observation_requirement.diagnosis_id having count(*) > 1)) > 0 THEN
	BEGIN
		DECLARE var1 INT;
		DECLARE var2 INT;
		DECLARE var3 INT;
		DECLARE bDone INT;
		DECLARE curs2 CURSOR FOR Select id, observation.pid, observation.observation_type_id  from observation inner join patient_diagnosis on (observation.pid = patient_diagnosis.p_id) inner join observation_requirement on (observation.observation_type_id = observation_requirement.observation_type_id and patient_diagnosis.d_id = observation_requirement.diagnosis_id ) where id in (select o.id from observation o left join observation o2 on o.observation_type_id = o2.observation_type_id and o.pid = o2.pid and o.observation_date < o2.observation_date where o2.observation_date IS NULL) and datediff(CURDATE(), observation_date + frequency) >= alert_threshold and id not in (Select id from observation inner join patient_diagnosis on (observation.pid = patient_diagnosis.p_id) inner join observation_requirement on (observation.observation_type_id = observation_requirement.observation_type_id and patient_diagnosis.d_id = observation_requirement.diagnosis_id ) where id in (select o.id from observation o left join observation o2 on o.observation_type_id = o2.observation_type_id and o.pid = o2.pid and o.observation_date < o2.observation_date where o2.observation_date IS NULL) and datediff(CURDATE(), observation_date + frequency) >= alert_threshold group by id, observation.pid, observation.observation_type_id, observation_requirement.diagnosis_id having count(*) > 1);
		DECLARE CONTINUE HANDLER FOR NOT FOUND SET bDone = 1;

		OPEN curs2;

		SET bDone = 0;
		add_alerts: LOOP
			FETCH curs2 INTO var1, var2, var3;
            IF bDone = 1 THEN
				LEAVE add_alerts;
			END IF;
			INSERT INTO alerts VALUES(NULL,'Low Activity',var2,var3,CURDATE());
		END LOOP add_alerts;
		CLOSE curs2;
	END;
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-26 16:40:08
