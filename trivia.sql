-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: trivia
-- ------------------------------------------------------
-- Server version	5.7.18

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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Historia','2017-06-13 11:40:07','2017-06-13 11:40:07'),(2,'Ciencia','2017-06-13 11:40:07','2017-06-13 11:40:07'),(3,'Entrenenimiento','2017-06-13 11:40:07','2017-06-13 11:40:07'),(4,'Deporte','2017-06-13 11:40:07','2017-06-13 11:40:07'),(5,'Geografia','2017-06-15 09:51:23','2017-06-15 09:51:23'),(6,'Geografia','2017-06-15 09:52:55','2017-06-15 09:52:55');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `games` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `question_number` int(11) DEFAULT NULL,
  `in_progress` tinyint(1) DEFAULT NULL,
  `vs_mode` tinyint(1) DEFAULT NULL,
  `current_question_id` int(11) DEFAULT NULL,
  `current_question_state` tinyint(1) DEFAULT NULL,
  `corrects` int(11) DEFAULT NULL,
  `incorrects` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
INSERT INTO `games` VALUES (1,1,5,0,0,68,1,4,1,'2017-09-05 18:59:31','2017-09-12 07:51:59'),(2,1,5,0,0,1,1,1,4,'2017-09-12 07:51:59','2017-09-14 18:23:59'),(3,2,5,0,0,55,1,2,3,'2017-09-12 11:07:14','2017-09-12 11:07:54'),(4,2,5,0,0,1,1,4,1,'2017-09-12 11:07:54','2017-09-12 11:08:30'),(5,2,5,0,0,34,1,3,2,'2017-09-12 11:08:30','2017-09-12 11:09:00'),(6,2,0,1,0,43,0,0,0,'2017-09-12 11:09:00','2017-10-18 08:43:45'),(7,1,5,0,0,31,1,2,3,'2017-09-14 18:23:59','2017-09-14 18:24:02'),(8,1,5,0,0,43,1,3,2,'2017-09-14 18:24:02','2017-09-14 18:24:03'),(9,1,5,0,0,23,1,1,4,'2017-09-14 18:24:03','2017-10-09 08:41:58'),(10,3,5,0,0,34,1,4,1,'2017-09-14 18:28:08','2017-09-14 18:32:07'),(11,3,5,0,0,55,1,1,4,'2017-09-14 18:32:07','2017-09-14 18:32:42'),(12,3,0,1,0,44,0,0,0,'2017-09-14 18:32:42','2017-09-14 18:32:42'),(13,1,5,0,0,50,1,0,5,'2017-10-09 08:41:58','2017-10-11 22:58:12'),(14,1,5,0,0,14,1,2,3,'2017-10-11 22:58:12','2017-10-17 12:29:30'),(15,1,5,0,0,33,1,0,5,'2017-10-17 12:29:30','2017-10-17 12:29:37'),(16,1,3,1,0,59,0,0,3,'2017-10-17 12:29:37','2017-10-23 14:34:10'),(17,2,0,1,0,47,0,0,0,'2017-10-19 14:50:30','2017-10-19 14:50:30'),(18,1,0,1,0,2,0,0,0,'2017-10-19 14:50:30','2017-10-19 14:50:30'),(19,2,0,1,0,7,0,0,0,'2017-10-19 14:52:10','2017-10-19 14:52:10'),(20,1,0,1,0,6,0,0,0,'2017-10-19 14:52:10','2017-10-19 14:52:10'),(21,1,0,1,0,13,0,0,0,'2017-10-19 14:58:31','2017-10-19 14:58:31'),(22,2,0,1,0,65,0,0,0,'2017-10-19 14:58:31','2017-10-19 14:58:31'),(23,1,0,1,0,63,0,0,0,'2017-10-19 15:01:30','2017-10-19 15:01:30'),(24,2,0,1,0,61,0,0,0,'2017-10-19 15:01:30','2017-10-19 15:01:30'),(25,2,0,1,0,69,0,0,0,'2017-10-19 16:02:52','2017-10-19 16:02:52'),(26,1,0,1,0,21,0,0,0,'2017-10-19 16:02:52','2017-10-19 16:02:52'),(27,2,0,1,0,39,0,0,0,'2017-10-19 19:08:11','2017-10-19 19:08:11'),(28,1,0,1,0,39,0,0,0,'2017-10-19 19:08:11','2017-10-19 19:08:11');
/*!40000 ALTER TABLE `games` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(100) DEFAULT NULL,
  `a1` varchar(50) DEFAULT NULL,
  `a2` varchar(50) DEFAULT NULL,
  `a3` varchar(50) DEFAULT NULL,
  `a4` varchar(50) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `correct_a` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,'Quien fue el anterior presidente de Argentina?','Macri','Menem','Cristina','Marcelo Tinelli',1,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(2,'¿Cuál es la rama mayoritaria del Islam?','Chiismo','Sunismo','Jariyismo','Sufismo',1,2,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(3,'¿De que fue ministro Manuel Fraga durante el franquismo?','De interior','De economia','De informacion y turismo','Del ejercito',1,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(4,'¿En que año tuvo lugar el ataque a Pearl Harbor?','1939','1940','1941','1942',1,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(5,'¿Cual es la ciudad mas antigua de America Latina?','Caral','Valparaiso','Arequipa','La Paz',1,1,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(6,'¿Con qué emperador estuvo casada Cleopatra?','Ptolomeo XIV','Julio Cesar','Marco Antonio','Todas son correctas',1,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(7,'El Renacimiento marcó el inicio de la Edad...','Moderna','Antigüedad clasica','Contemporanea','Media',1,1,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(8,'¿Que pais fue dirigido por Stalin?','Cuba','Alemania','Argentina','Union Sovietica',1,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(9,'¿Que se celebra el 8 de Marzo?','Dia del trabajador','Dia del medio ambiente','Dia del niño','Dia de la mujer',1,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(10,'¿Quién liberó a Argentina, Chile y Perú?','Manuel Belgrano','Simon Bolivar','Ernesto Che Guevara','Jose de San Martin',1,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(11,'¿Donde surgio la filosofia?','Argentina','Egipto','Chile','Grecia',1,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(12,'¿Quién gobernó Francia desde 1799 a 1815?','Adam Smith','Louis Bonaldgug','Peron','Napoleon Bonaparte',1,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(13,'Cual de los siguientes numeros binarios representa el 10 decimal?','0000010','0001010','11111111','0001000',2,2,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(14,'¿Cuál de las sisguientes enfermedades ataca al higado?','Hepatitis','Diabetes','Artrosis','Cifoescoliosis',2,1,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(15,'¿Cómo tomarías la sustancia alucinógena natural llamada ayahuasca? ','Inhalada','Inyectada','Esnifada','Ingerida',2,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(16,'¿Qué cambio de estado ocurre en la sublimación?','De solido a liquido','De solido a gaseoso','De gaseoso a liquido','De liquido a solido',2,2,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(17,'¿De qué color es la sange de los peces?','Verde oscuro','Marron oscuro','Roja','Azul',2,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(18,'¿Cuántas caras tiene un icosaedro?','20','16','8','24',2,1,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(19,'¿Qué estudia la icitología?','Los insectos','DLas erupciones cutaneas','Las rocas','Los peces',2,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(20,'¿Con qué otro nombre se conoce el ciclo del agua?','Ciclo natural','Ciclo hidroponico','Ciclo hidrologico','Ciclo acuoso',2,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(21,'¿Cuántos rayos gamma hay en una neurona?','Uno','Dos','De dos a tres','Ninguno',2,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(22,'¿Cómo se llama a los electrones que se encuentran en la última capa del átomo?','Isotopos','Iones','Electrones','Electrones de valencia',2,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(23,'¿Cómo se llama el nivel más alto de la marea?','Pleamar','Bajamar','Repunte','Estacionario',2,1,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(24,'¿Cómo se llama el incremento en el volumen,fluidez o frecuencia de las deposiciones?','Estreñimiento','Diverticulosis','Hemorroides','Diarrea',2,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(25,'¿Cómo se mide la fuerza del viento en el mar?','Pies','Zancadas','Nudos','Kilometros por hora',2,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(26,'Como le dicen a Macri?','Pato','Raro','Malo','Gatooooooo',3,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(27,'¿Cómo se llamaba el personaje que interpretaba John Travolta en Grease?','Danny Puño','Danny Zuko','Danny Grease','Danny Chulo',3,2,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(28,'¿En qué año se estrenó la película de Disney Pinocho?','1940','1950','1952','1946',3,1,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(29,'¿Cuál es el verdadero nombre de Alejandro Sanz?','Alejandro Sánchez Pizarro','Alejandro Sánchez Gutiérrez','Alexander Santiago Seguiz','Alejandro Alberto Sanzón',3,1,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(30,'¿Cómo se llama el protagonista de la saga Indiana Jones?','Jack Nicholson','Michael Fox','Harrison Ford','Robin Williams',3,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(31,'¿Cuál de ellas es una cantante española?','Chenoa','Ariana Grande','Jennifer Lopez','Alaska',3,1,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(32,'¿A quién se considera el Rey del Pop?','Justin Bieber','Michael Jackson','Zayn Malik','Zac Efron',3,2,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(33,'¿Qué personaje del videojuego Mortal Kombat tiene poderes de hielo?','Scorpion','Reptile','Motaro','Sub-Zero',3,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(34,'¿Cómo se llama el pájaro símbolo de los Juegos del Hambre?','Lechuza','Llamas','Paloma','Sinsajo',3,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(35,'¿Cuántos colores tiene un cubo de Rubik clásico?','4','6','8','9',3,2,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(36,'¿Cómo se llama el robot alcohólico de la serie \'Futurama\'?','Zoidberg','Vader','Bender','Hubert',3,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(37,'¿Cómo se llama el payaso de \'Los Simpson\'?','Kurty','Krusty','Kroger','Blaquito',3,2,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(38,'Por cuantos goles le ganaria River a Boca si jugaran hoy?','1-0','2-0','4-0','Boca no asistiria',4,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(39,'¿De que deporte es el kemari uno de los principales antecesores?','Futbol','Tenis','Rugby','Karate',4,1,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(40,'¿Cuántas finales del mundo jugó la Selección Argentina de fútbol?','Cinco','Seis','Cuatro','Tres',4,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(41,'¿Cuál es el estilo de natación más rápido?','Crol','Espalda','Mariposa','Pecho',4,1,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(42,'¿Cuántos jugadores componen un equipo de rugby?','11','12','15','21',4,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(43,'¿Quién inventó el arte marcial llamado Jeet Kune Do?','David Carradine','Bruce Lee','Kato Mimoko','Ninguna es correcta',4,2,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(44,'¿De qué color es el cero en el cilindro de la ruleta?','Blanco','Negro','Rojo','Verde',4,4,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(45,'¿Cuánto dura un partido de fútbol?','80 minutos','50 minutos','90 minutos','75 minutos',4,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(46,'¿Cuál de estos pilotos no es de F1?','Richard Petty','Fernando Alonso','Sebastian Vettel','Mark Webber',4,1,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(47,'¿En cuál de estos deportes no hay árbitro?','Football','Ultimate','Basketball','Volleyball',4,2,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(48,'¿En cuántos mundiales de fútbol participó la selección de Canadá?','0','3','1','5',4,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(49,'¿Cuántos cuadros tiene un tablero de ajedres?','36','54','64','81',4,3,'2017-06-13 11:40:07','2017-06-13 11:40:07'),(50,'¿Cual es el pais menos turistico de Europa?','Armenia','Moldavia','Lichtenstein','Hungria',5,3,'2017-06-15 09:51:23','2017-06-15 09:51:23'),(51,'¿A que pais pertenece la isla de Tasmania?','Estados Unidos','Australia','Portugal','Ninguna es correcta',5,2,'2017-06-15 09:51:23','2017-06-15 09:51:23'),(52,'¿En cuál de los siguientes países NO hay ningún desierto?','España','Chile','Mongolia','Alemania',5,4,'2017-06-15 09:51:23','2017-06-15 09:51:23'),(53,'¿Cuál es el código internacional para Chile?','CH','CI','CL','CE',5,3,'2017-06-15 09:51:23','2017-06-15 09:51:23'),(54,'¿En qué país situarías la ciudad de Cali?','Colombia','Venezuela','Costa Rica','Chile',5,1,'2017-06-15 09:51:23','2017-06-15 09:51:23'),(55,'¿Cuál de estas características no pertenece al clima Mediterráneo?','Veranos secos y calurosos','Es un tipo de clima templado','Lluvias muy abundantes','Variables temperaturas en primavera',5,3,'2017-06-15 09:51:23','2017-06-15 09:51:23'),(56,'¿Qué es el Cabo de Creus?','El punto más oriental de España','El punto más oriental de la Península','El punto más oriental de Cataluña','Ninguna es correcta',5,2,'2017-06-15 09:51:23','2017-06-15 09:51:23'),(57,'¿Cuál es principal celebración holandesa?','Navidad','La llegada del verano','El dia de la Reina','Halloween',5,3,'2017-06-15 09:51:23','2017-06-15 09:51:23'),(58,'¿Cuál de las siguientes especialidades NO es típica de la cocina estadounidense?','La hamburguesa','El pastel de cangrejo','La tarta de manzana','Todas son tipicas',5,4,'2017-06-15 09:51:23','2017-06-15 09:51:23'),(59,'¿Con cuántos países limita Argentina?','Tres','Cuatro','Cinco','Seis',5,3,'2017-06-15 09:51:23','2017-06-15 09:51:23'),(60,'¿Cuál es la capital de Suiza?','Berna','Zurich','Ginebra','Basilea',5,1,'2017-06-15 09:51:23','2017-06-15 09:51:23'),(61,'¿Cuál de las siguientes islas está más al sur?','Sicilia','Malta','Córcega','Cerdeña',5,2,'2017-06-15 09:51:23','2017-06-15 09:51:23'),(62,'¿Cual es el pais menos turistico de Europa?','Armenia','Moldavia','Lichtenstein','Hungria',6,3,'2017-06-15 09:52:55','2017-06-15 09:52:55'),(63,'¿A que pais pertenece la isla de Tasmania?','Estados Unidos','Australia','Portugal','Ninguna es correcta',6,2,'2017-06-15 09:52:55','2017-06-15 09:52:55'),(64,'¿En cuál de los siguientes países NO hay ningún desierto?','España','Chile','Mongolia','Alemania',6,4,'2017-06-15 09:52:55','2017-06-15 09:52:55'),(65,'¿Cuál es el código internacional para Chile?','CH','CI','CL','CE',6,3,'2017-06-15 09:52:55','2017-06-15 09:52:55'),(66,'¿En qué país situarías la ciudad de Cali?','Colombia','Venezuela','Costa Rica','Chile',6,1,'2017-06-15 09:52:55','2017-06-15 09:52:55'),(67,'¿Cuál de estas características no pertenece al clima Mediterráneo?','Veranos secos y calurosos','Es un tipo de clima templado','Lluvias muy abundantes','Variables temperaturas en primavera',6,3,'2017-06-15 09:52:55','2017-06-15 09:52:55'),(68,'¿Qué es el Cabo de Creus?','El punto más oriental de España','El punto más oriental de la Península','El punto más oriental de Cataluña','Ninguna es correcta',6,2,'2017-06-15 09:52:55','2017-06-15 09:52:55'),(69,'¿Cuál es principal celebración holandesa?','Navidad','La llegada del verano','El dia de la Reina','Halloween',6,3,'2017-06-15 09:52:55','2017-06-15 09:52:55'),(70,'¿Cuál de las siguientes especialidades NO es típica de la cocina estadounidense?','La hamburguesa','El pastel de cangrejo','La tarta de manzana','Todas son tipicas',6,4,'2017-06-15 09:52:55','2017-06-15 09:52:55'),(71,'¿Con cuántos países limita Argentina?','Tres','Cuatro','Cinco','Seis',6,3,'2017-06-15 09:52:55','2017-06-15 09:52:55'),(72,'¿Cuál es la capital de Suiza?','Berna','Zurich','Ginebra','Basilea',6,1,'2017-06-15 09:52:55','2017-06-15 09:52:55'),(73,'¿Cuál de las siguientes islas está más al sur?','Sicilia','Malta','Córcega','Cerdeña',6,2,'2017-06-15 09:52:55','2017-06-15 09:52:55');
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schema_version`
--

DROP TABLE IF EXISTS `schema_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schema_version` (
  `version` varchar(32) NOT NULL,
  `applied_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `duration` int(11) NOT NULL,
  UNIQUE KEY `version` (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schema_version`
--

LOCK TABLES `schema_version` WRITE;
/*!40000 ALTER TABLE `schema_version` DISABLE KEYS */;
INSERT INTO `schema_version` VALUES ('20170407112621','2017-05-20 01:06:38',-16),('20170525003123','2017-05-25 03:37:57',-14),('20170525003847','2017-05-25 03:48:53',0),('20170525003906','2017-05-25 03:48:53',-1),('20170526102547','2017-05-26 13:37:57',-16),('20171018075546','2017-10-18 11:29:12',-16);
/*!40000 ALTER TABLE `schema_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `mail` varchar(30) DEFAULT NULL,
  `i_questions` int(11) DEFAULT NULL,
  `c_questions` int(11) DEFAULT NULL,
  `win_rate` double DEFAULT NULL,
  `acces_level` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'gabriel','81dc9bdb52d04dc2036dbd8313ed055','gabriel@gmail.com',48,24,33,5,'2017-06-18 19:04:00','2017-10-23 14:34:10'),(2,'pepe','81dc9bdb52d04dc2036dbd8313ed055','pepe@gmail.com',6,9,60,0,'2017-09-12 11:07:07','2017-09-12 11:09:00'),(3,'santiago147','733d7be2196ff7efaf6913fc8bdcabf','sasa@gmail.com',5,5,50,0,'2017-09-14 18:27:45','2017-09-14 18:32:42'),(4,'gabriel2','81dc9bdb52d04dc2036dbd8313ed055','gabriel_ogh@outlook.com',0,0,0,0,'2017-10-11 22:35:39','2017-10-11 22:35:39'),(5,'gabriel3','81dc9bdb52d04dc2036dbd8313ed055','agujuarez97@hotmail.com.ar',0,0,0,0,'2017-10-12 18:12:15','2017-10-12 18:12:15'),(6,'juan','81dc9bdb52d04dc2036dbd8313ed055','juan@gmail.com',0,0,0,0,'2017-10-17 12:16:14','2017-10-17 12:16:14');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `versusmodes`
--

DROP TABLE IF EXISTS `versusmodes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `versusmodes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `game_p1_id` int(11) DEFAULT NULL,
  `game_p2_id` int(11) DEFAULT NULL,
  `turn` int(11) DEFAULT NULL,
  `in_progress` tinyint(1) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `versusmodes`
--

LOCK TABLES `versusmodes` WRITE;
/*!40000 ALTER TABLE `versusmodes` DISABLE KEYS */;
INSERT INTO `versusmodes` VALUES (1,23,24,1,1,'2017-10-19 15:01:30','2017-10-19 15:01:30'),(2,25,26,1,1,'2017-10-19 16:02:52','2017-10-19 16:02:52'),(3,27,28,1,1,'2017-10-19 19:08:11','2017-10-19 19:08:11'),(4,29,30,1,1,'2017-10-19 19:09:42','2017-10-19 19:09:42'),(5,31,32,1,1,'2017-10-19 19:09:53','2017-10-19 19:09:53'),(6,33,34,1,1,'2017-10-19 19:10:17','2017-10-19 19:10:17'),(7,35,36,1,1,'2017-10-19 19:48:35','2017-10-19 19:48:35');
/*!40000 ALTER TABLE `versusmodes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-23 14:39:45
