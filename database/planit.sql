CREATE DATABASE  IF NOT EXISTS `planit` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `planit`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: planit
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `date` datetime NOT NULL,
  `photo_url` varchar(255) DEFAULT NULL,
  `user_id` int NOT NULL,
  `location_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `location_id` (`location_id`),
  CONSTRAINT `event_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `event_ibfk_2` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (1,'Team Meeting','Discuss project milestones and progress.','2024-12-20 10:00:00',NULL,2,NULL),(2,'Team Meeting 2','Discuss project milestones and progress. 2','2024-12-20 10:00:00',NULL,2,NULL),(3,'Team Meeting 3','Discuss project milestones and progress. 3','2024-12-20 10:00:00',NULL,2,NULL),(4,'Team Meeting 3','Discuss project milestones and progress. 3','2024-12-20 10:00:00','https://example.com/images/team-meeting.jpg',2,NULL),(5,'Team Meeting 3','Discuss project milestones and progress. 3','2024-12-20 10:00:00','https://example.com/images/team-meeting.jpg',2,NULL),(6,'Team Meeting 3','Discuss project milestones and progress. 3','2024-12-20 10:00:00','https://example.com/images/team-meeting.jpg',2,NULL),(7,'Team Meeting 3','Discuss project milestones and progress. 3','2024-12-20 10:00:00','https://example.com/images/team-meeting.jpg',1,NULL),(8,'Team Meeting 4','Discuss project milestones and progress. 4','2024-12-20 10:00:00','https://example.com/images/team-meeting.jpg',2,NULL),(9,'Team Meeting 4','Discuss project milestones and progress. 4','2024-12-20 10:00:00','https://example.com/images/team-meeting.jpg',1,NULL),(12,'teste','teste','2024-12-20 10:00:00','',1,NULL),(13,'teste','teste','2024-12-20 10:00:00','',1,NULL),(14,'Novo Evento','Descricao teste','2024-12-20 10:00:00','https://i.imgur.com/GTMh9Cd.jpeg',1,NULL),(15,'Primero Evenro','Uma breve descricao','2024-12-20 10:00:00','https://i.imgur.com/ADwc7uf.jpeg',102,NULL),(16,'another event	','short event description','2025-04-24 00:00:00','',102,NULL),(17,'Jantar de Natal','Jantar de Natal da Equipa de APIM','2025-12-18 20:00:00','https://www.maximillion.co.uk/wp-content/uploads/2024/08/christmas.jpg',52,NULL),(18,'teste pending','para teste','2025-01-01 00:00:00','',102,NULL),(19,'Teste Evento','Um pequeno texto de exemplo','2025-04-24 00:00:00','https://media.istockphoto.com/id/974238866/pt/foto/audience-listens-to-the-lecturer-at-the-conference.jpg?s=612x612&w=0&k=20&c=ZOCjHsr2nyWVvAgjA36Kg1FaIQpvQ3oThxEf_W7JdCc=',152,NULL),(21,'Almoco de equipa','Um Evento para a equipa de trabalho','2025-01-24 07:59:00','https://www.quintadesantana.com/cms/wp-content/uploads/2021/04/Evento_Picnic_QuintadeSantAna_Mafra.png',152,18),(23,'Teste','Uma breve descrição','2025-01-12 03:17:00','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPQiPdOiMYO9sBD80Y6SpNJC3YUeE-wGFFcA&s',152,4),(24,'Evento LX Factory ','Evento no LX factory','2025-01-01 16:31:00','https://plus.unsplash.com/premium_photo-1664474653221-8412b8dfca3e?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8ZXZlbnRvfGVufDB8fDB8fHww',152,5),(25,'Another One','another event','2025-01-01 23:40:00','https://cdn-images-1.medium.com/max/828/1*55Whgf0dNWpgbeq-qhRd9A.png',152,6),(26,'Visit to the Zoo','Um passeio ao jardim zoologico','2025-01-15 19:19:00','https://lisbonshopping.com/wp-content/uploads/2021/10/LS-site__0004_caf33a84b_cred-Zoo-1000x680.jpg',152,7),(27,'Event Test','testestestes','2025-01-01 17:37:00','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQs65bkdZlbO9DL6dm8qCoTH54SUt9IurZ63A&s',152,8),(28,'Aniversario','Jantar de aniversario','2025-01-22 23:11:00','https://www.arrozsaludaes.pt/wp-content/uploads/2024/08/unnamed-5-2-1080x675.jpg',152,9),(31,'Primeiro Evento','este é o primeiro evento','2025-01-08 05:26:00','https://lh3.googleusercontent.com/proxy/yWa9el0QN4WLu-qDs1CGZQoCfyN15OalYcYKiVOcvR1084frMFWjIrF2O0SQ83Ft-yTNaIKqjDLZ8p389g6P290ukkn4jj4rKDmiRmeFeIsVZgbHKe2XchNwyHwK_i7YZzPbZZSjuMPIcohOywArQyspLiL-KNBS9ugpIiIk',53,12),(39,'Evento Disney','maratona de filmes','2025-01-11 23:54:00','content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000000006',152,14),(40,'evento no iade','teste de evento','2025-01-12 00:05:00','content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000000006',152,15),(42,'outro evento para testar a morada','outro','2025-01-12 00:15:00','content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000000004',152,NULL),(44,'outro teste da loc','outro teste','2025-01-12 00:22:00','content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000000005',152,NULL),(45,'estouvfarto ','mais um ','2025-01-12 00:24:00','content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000000006',152,NULL),(48,'Apresentação IADE','Apresentação do projeto movel\nIADE Creative University ','2025-01-14 09:00:00','content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000000012',202,20),(50,'Apresentação da App','Apresentação da app','2025-01-14 07:30:00','content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000000012',203,22),(51,'Segundo Evento','mais um evento','2025-01-15 21:45:00','content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000000008',203,23),(52,'Apresentação','descrição ','2025-01-12 23:23:00','content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000000012',204,24),(54,'Festa Final de Curso','Uma festa organizada para os alunos finalistas do curso.\nTorre de Belem','2025-01-17 23:30:00','content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000000007',202,26),(55,'Viagem','Uma viagem a disney em Paris','2025-02-27 02:20:00','content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000000006',204,27),(56,'Almoço Leitão','Vamos almoçar Leitão em Negrais','2025-01-23 12:22:00','content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000000011',204,28);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_seq`
--

DROP TABLE IF EXISTS `event_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_seq`
--

LOCK TABLES `event_seq` WRITE;
/*!40000 ALTER TABLE `event_seq` DISABLE KEYS */;
INSERT INTO `event_seq` VALUES (51);
/*!40000 ALTER TABLE `event_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
INSERT INTO `location` VALUES (1,'Rua do Centro, 123',38.736946,-9.142685),(2,'Rua do Centro, 123',38.736946,-9.142685),(4,'estrada da outorela n50 ',38.7232511,-9.234798699999999),(5,'lx factory ',38.7034979,-9.178873),(6,NULL,38.74584989523363,-9.226975366473198),(7,NULL,38.72870757026596,-9.190743416547775),(8,'',38.74227939626015,-9.166550561785698),(9,NULL,38.7361415,-9.2639935),(10,NULL,38.7232511,-9.234798699999999),(11,NULL,-23.9439519,-46.3740115),(12,NULL,38.7072166,-9.1524327),(13,'iade creative university',38.7072166,-9.1524327),(14,'Unknown Address',38.7232511,-9.234798699999999),(15,'Endereço desconhecido',38.7072166,-9.1524327),(16,NULL,38.7232511,-9.234798699999999),(17,'estrada da outorela n50 ',38.7232511,-9.234798699999999),(18,'parque eduardo viii',38.7283001,-9.1526828),(19,'estrada da outorela n50 ',38.7232511,-9.234798699999999),(20,'iade creative university ',38.7072166,-9.1524327),(21,'Torre de belem',38.6915837,-9.215977299999999),(22,'iade creative university ',38.7072166,-9.1524327),(23,'estrada da outorela n50',38.7232511,-9.234798699999999),(24,'parque eduardo vii',38.7283001,-9.1526828),(25,'estrada da outorela n50',38.7232511,-9.234798699999999),(26,'Torre de Belem',38.6915837,-9.215977299999999),(27,'Disney Paris ',48.8673858,2.783593),(28,'Negrais',38.8782826,-9.279044899999999);
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_seq`
--

DROP TABLE IF EXISTS `location_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_seq`
--

LOCK TABLES `location_seq` WRITE;
/*!40000 ALTER TABLE `location_seq` DISABLE KEYS */;
INSERT INTO `location_seq` VALUES (1);
/*!40000 ALTER TABLE `location_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participant`
--

DROP TABLE IF EXISTS `participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `participant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `event_id` int NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`event_id`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `participant_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `participant_ibfk_2` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participant`
--

LOCK TABLES `participant` WRITE;
/*!40000 ALTER TABLE `participant` DISABLE KEYS */;
INSERT INTO `participant` VALUES (2,2,1,'CONFIRMED'),(3,1,1,'CONFIRMED'),(4,102,1,'CONFIRMED'),(5,53,1,'CONFIRMED'),(6,52,1,'CONFIRMED'),(7,52,15,'CONFIRMED'),(8,1,15,'CONFIRMED'),(9,2,15,'CONFIRMED'),(10,53,15,'CONFIRMED'),(12,1,16,'CONFIRMED'),(13,102,15,'INVITED'),(14,102,17,'CONFIRMED'),(15,52,18,'CONFIRMED'),(16,1,18,'CONFIRMED'),(17,102,7,'INVITED'),(18,152,14,'CONFIRMED'),(19,152,7,'CONFIRMED'),(20,102,19,'CONFIRMED'),(22,1,19,'INVITED'),(23,53,19,'INVITED'),(24,52,19,'INVITED'),(25,102,25,'INVITED'),(26,102,27,'CONFIRMED'),(27,52,27,'INVITED'),(28,102,26,'INVITED'),(29,102,28,'CONFIRMED'),(30,102,23,'INVITED'),(31,152,31,'INVITED'),(33,202,24,'CONFIRMED'),(34,202,39,'CONFIRMED'),(35,202,21,'CONFIRMED'),(36,152,48,'INVITED'),(37,1,48,'CONFIRMED'),(38,202,48,'DECLINED'),(39,152,50,'DECLINED'),(40,202,50,'CONFIRMED'),(41,203,48,'CONFIRMED'),(42,202,52,'CONFIRMED'),(43,152,52,'INVITED'),(44,202,55,'INVITED'),(45,203,56,'INVITED'),(46,202,56,'INVITED');
/*!40000 ALTER TABLE `participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participant_seq`
--

DROP TABLE IF EXISTS `participant_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `participant_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participant_seq`
--

LOCK TABLES `participant_seq` WRITE;
/*!40000 ALTER TABLE `participant_seq` DISABLE KEYS */;
INSERT INTO `participant_seq` VALUES (1);
/*!40000 ALTER TABLE `participant_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Lu','lu@email.com','teste123lu'),(2,'gui','gui@teste.com	','teste'),(52,'teste','teste@teste.com','teste'),(53,'teste1','teste1@gmail.com','teste'),(102,'teste','teste','teste'),(152,'teste','teste@teste.pt','teste123.'),(202,'Guilherme','gui@email.pt','teste123.'),(203,'Pedro','pedro@email.pt','teste123.'),(204,'Diogo','diogo@email.pt','teste123.');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_seq`
--

DROP TABLE IF EXISTS `user_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_seq`
--

LOCK TABLES `user_seq` WRITE;
/*!40000 ALTER TABLE `user_seq` DISABLE KEYS */;
INSERT INTO `user_seq` VALUES (301);
/*!40000 ALTER TABLE `user_seq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-14  3:04:56
