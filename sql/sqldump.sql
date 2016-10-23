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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnosis`
--

LOCK TABLES `diagnosis` WRITE;
/*!40000 ALTER TABLE `diagnosis` DISABLE KEYS */;
INSERT INTO `diagnosis` VALUES (1,'Heart Disease','Heart Disease'),(2,'HIV','HIV'),(3,'Diabetes','Type-II Diabetes'),(4,'SLE','Lupus'),(5,'Cancer','Cancer'),(6,'Lung mass','Lung mass');
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
INSERT INTO `health_supporter` VALUES (1,'Leonard Hofstader','Sacramento SC','9199145555',2),(2,'Penny Hofstader','2500-204 Sacramento','+1(919)9170067',3),(3,'Amy Farrahfowler','2500-204 Sacramento','+1(919)9170067',4);
/*!40000 ALTER TABLE `health_supporter` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mood`
--

LOCK TABLES `mood` WRITE;
/*!40000 ALTER TABLE `mood` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observation`
--

LOCK TABLES `observation` WRITE;
/*!40000 ALTER TABLE `observation` DISABLE KEYS */;
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
  `pid` int(11) NOT NULL,
  `diagnosis_id` int(11) DEFAULT NULL,
  `lower_limit` varchar(10) DEFAULT NULL,
  `upper_limit` varchar(10) DEFAULT NULL,
  `start_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `required_ind` tinyint(1) DEFAULT '1',
  KEY `observation_type_id` (`observation_type_id`),
  KEY `obs_subtype_id` (`obs_subtype_id`),
  KEY `hs_id` (`hs_id`),
  KEY `pid` (`pid`),
  KEY `diagnosis_id` (`diagnosis_id`),
  CONSTRAINT `observation_requirement_ibfk_1` FOREIGN KEY (`observation_type_id`) REFERENCES `observation_type` (`observation_type_id`),
  CONSTRAINT `observation_requirement_ibfk_2` FOREIGN KEY (`obs_subtype_id`) REFERENCES `observation_sub_type` (`obs_subtype_id`),
  CONSTRAINT `observation_requirement_ibfk_3` FOREIGN KEY (`hs_id`) REFERENCES `health_supporter` (`id`),
  CONSTRAINT `observation_requirement_ibfk_4` FOREIGN KEY (`pid`) REFERENCES `patient` (`id`),
  CONSTRAINT `observation_requirement_ibfk_5` FOREIGN KEY (`diagnosis_id`) REFERENCES `diagnosis` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observation_requirement`
--

LOCK TABLES `observation_requirement` WRITE;
/*!40000 ALTER TABLE `observation_requirement` DISABLE KEYS */;
/*!40000 ALTER TABLE `observation_requirement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `observation_sub_type`
--

DROP TABLE IF EXISTS `observation_sub_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observation_sub_type` (
  `observation_type_id` int(11) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `obs_subtype_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`obs_subtype_id`),
  UNIQUE KEY `name` (`name`),
  KEY `observation_type_id` (`observation_type_id`),
  CONSTRAINT `observation_sub_type_ibfk_1` FOREIGN KEY (`observation_type_id`) REFERENCES `observation_type` (`observation_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observation_sub_type`
--

LOCK TABLES `observation_sub_type` WRITE;
/*!40000 ALTER TABLE `observation_sub_type` DISABLE KEYS */;
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
  `name` int(11) NOT NULL,
  `description` text NOT NULL,
  `metric` varchar(10) NOT NULL,
  PRIMARY KEY (`observation_type_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observation_type`
--

LOCK TABLES `observation_type` WRITE;
/*!40000 ALTER TABLE `observation_type` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oxygen_saturation`
--

LOCK TABLES `oxygen_saturation` WRITE;
/*!40000 ALTER TABLE `oxygen_saturation` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pain_intensity`
--

LOCK TABLES `pain_intensity` WRITE;
/*!40000 ALTER TABLE `pain_intensity` DISABLE KEYS */;
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
INSERT INTO `patient` VALUES (1,'Sheldon Cooper','1984-05-05','New Jersey NY','male','919917067',1),(2,'Leonard Hofstader','0000-00-00','Chapel Hill RTP','male','+1(917)9190045',2),(3,'Penny Hofstader','1989-12-25','2500-204 Sacramento','female','+1(919)9170067',3),(4,'Amy Farrahfowler','1989-06-15','2500-204 Sacramento','female','+1(919)9170067',4);
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

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
  CONSTRAINT `recommended_observations_ibfk_1` FOREIGN KEY (`hs_id`) REFERENCES `health_supporter` (`id`),
  CONSTRAINT `recommended_observations_ibfk_2` FOREIGN KEY (`observation_type_id`) REFERENCES `observation_type` (`observation_type_id`),
  CONSTRAINT `recommended_observations_ibfk_3` FOREIGN KEY (`subtype_id`) REFERENCES `observation_sub_type` (`obs_subtype_id`),
  CONSTRAINT `recommended_observations_ibfk_4` FOREIGN KEY (`pid`) REFERENCES `patient` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recommended_observations`
--

LOCK TABLES `recommended_observations` WRITE;
/*!40000 ALTER TABLE `recommended_observations` DISABLE KEYS */;
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
  CONSTRAINT `required_observations_ibfk_1` FOREIGN KEY (`diagnosis_id`) REFERENCES `diagnosis` (`id`),
  CONSTRAINT `required_observations_ibfk_2` FOREIGN KEY (`observation_type_id`) REFERENCES `observation_type` (`observation_type_id`),
  CONSTRAINT `required_observations_ibfk_3` FOREIGN KEY (`subtype_id`) REFERENCES `observation_sub_type` (`obs_subtype_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `required_observations`
--

LOCK TABLES `required_observations` WRITE;
/*!40000 ALTER TABLE `required_observations` DISABLE KEYS */;
/*!40000 ALTER TABLE `required_observations` ENABLE KEYS */;
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
  `sec_question` varchar(100) NOT NULL DEFAULT 'Your favorite color?',
  `sec_answer` varchar(30) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `weight`
--

LOCK TABLES `weight` WRITE;
/*!40000 ALTER TABLE `weight` DISABLE KEYS */;
/*!40000 ALTER TABLE `weight` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-22 20:02:19
