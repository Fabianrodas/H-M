CREATE DATABASE  IF NOT EXISTS `proyectoprueba` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `proyectoprueba`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: proyectoprueba
-- ------------------------------------------------------
-- Server version	9.0.0

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
-- Table structure for table `carrito`
--

DROP TABLE IF EXISTS `carrito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrito` (
  `idCarrito` int NOT NULL AUTO_INCREMENT,
  `idCliente` int DEFAULT NULL,
  `idProducto` int DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  `precioUnitario` decimal(10,2) DEFAULT NULL,
  `talla` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`idCarrito`),
  KEY `idProducto` (`idProducto`),
  KEY `idCliente` (`idCliente`),
  CONSTRAINT `carrito_ibfk_1` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`),
  CONSTRAINT `carrito_ibfk_2` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrito`
--

LOCK TABLES `carrito` WRITE;
/*!40000 ALTER TABLE `carrito` DISABLE KEYS */;
/*!40000 ALTER TABLE `carrito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `idCategoria` int NOT NULL AUTO_INCREMENT,
  `nombre_categoria` varchar(70) NOT NULL,
  PRIMARY KEY (`idCategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'Women'),(2,'Men'),(3,'Baby'),(4,'Kids');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `idCliente` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `correo` varchar(120) DEFAULT NULL,
  `zipcode` varchar(50) DEFAULT NULL,
  `fecha_nacimiento` date NOT NULL,
  `contrasena` varchar(100) NOT NULL,
  `genero` varchar(20) NOT NULL,
  `imagen_usuario` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (12,'System','Admin','admin@admin.com','','2002-04-19','61646d696e','Hombre','default_user.jpg');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorito`
--

DROP TABLE IF EXISTS `favorito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorito` (
  `idFavorito` int NOT NULL AUTO_INCREMENT,
  `idProducto` int DEFAULT NULL,
  `idCliente` int DEFAULT NULL,
  PRIMARY KEY (`idFavorito`),
  KEY `idProducto` (`idProducto`),
  KEY `idCliente` (`idCliente`),
  CONSTRAINT `favorito_ibfk_1` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`),
  CONSTRAINT `favorito_ibfk_2` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorito`
--

LOCK TABLES `favorito` WRITE;
/*!40000 ALTER TABLE `favorito` DISABLE KEYS */;
/*!40000 ALTER TABLE `favorito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genero`
--

DROP TABLE IF EXISTS `genero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genero` (
  `idGenero` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(40) NOT NULL,
  PRIMARY KEY (`idGenero`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genero`
--

LOCK TABLES `genero` WRITE;
/*!40000 ALTER TABLE `genero` DISABLE KEYS */;
INSERT INTO `genero` VALUES (1,'Hombre'),(2,'Mujer'),(3,'Unisex');
/*!40000 ALTER TABLE `genero` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido` (
  `idPedido` bigint unsigned NOT NULL AUTO_INCREMENT,
  `idCliente` int DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`idPedido`),
  UNIQUE KEY `idPedido` (`idPedido`),
  KEY `idCliente` (`idCliente`),
  CONSTRAINT `pedido_ibfk_1` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `idProducto` int NOT NULL AUTO_INCREMENT,
  `nombre_Producto` varchar(250) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  `precio` decimal(10,2) NOT NULL,
  `idSubsub` int DEFAULT NULL,
  `idGenero` int DEFAULT NULL,
  `imagen_nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idProducto`),
  KEY `fk_idSubsub` (`idSubsub`),
  KEY `fk_idGenero` (`idGenero`),
  CONSTRAINT `fk_idGenero` FOREIGN KEY (`idGenero`) REFERENCES `genero` (`idGenero`),
  CONSTRAINT `fk_idSubsub` FOREIGN KEY (`idSubsub`) REFERENCES `subsub` (`idSubsub`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES (1,'Zebra Elegance','Long black and white zebra dress',49.99,1,2,'dress1.jpg'),(2,'Beige Comfort','Long sleeve cotton dress',39.99,1,2,'dress2.jpg'),(3,'Coffee Chic','Brown dress with buttons',42.99,1,2,'dress3.jpg'),(4,'Black Elegance','Black long sleeve top',29.99,2,2,'top1.jpg'),(5,'White Breeze','White short sleeve top',24.99,2,2,'top2.jpg'),(6,'Gray Mist','Gray short sleeve top',22.99,2,2,'top3.jpg'),(7,'Silk Rose','Large pink gray silk blouse',34.99,3,2,'blouse1.jpg'),(8,'Classic White','Classic white button blouse',32.99,3,2,'blouse2.jpg'),(9,'Striped Elegance','White and blue striped blouse',33.99,3,2,'blouse3.jpg'),(10,'Skinny Grey','Gray skinny fit jeans',44.99,4,2,'women_jeans1.jpg'),(11,'Straight Blue','Blue straight fit jeans',49.99,4,2,'women_jeans2.jpg'),(12,'Bell Blue','Blue bell bottom oversized jeans',54.99,4,2,'women_jeans3.jpg'),(13,'Light Grey Leggings','Light gray straight fit leggings',29.99,5,2,'leggings1.jpg'),(14,'Black Skinny','Black skinny fit leggings',34.99,5,2,'leggings2.jpg'),(15,'Beige Skinny','Beige skinny fit leggings',32.99,5,2,'leggings3.jpg'),(16,'Black Sport Bra','Black fabric sport bra',19.99,6,2,'bra1.jpg'),(17,'Soft Brown Sport Bra','Soft brown fabric sport bra',21.99,6,2,'bra2.jpg'),(18,'Pure White Sport Bra','White cotton sport bra',22.99,6,2,'bra3.jpg'),(19,'White Gym Fit','Tight white running gym shirt',27.99,7,2,'women_running1.jpg'),(20,'Scarlet Gym Crop Top','Red sports crop top',25.99,7,2,'women_running2.jpg'),(21,'White Sports Crop Top','White sports crop top',24.99,7,2,'women_running3.jpg'),(22,'Backless Black Gym Bra','Black gym bra with open back',26.99,8,2,'women_gym1.jpg'),(23,'Charcoal Gym Tee','Dark gray sleeveless gym shirt',34.99,8,2,'women_gym2.jpg'),(24,'White Gym Tank','White oversized sleeveless gym shirt',32.99,8,2,'women_gym3.jpg'),(25,'Sporty Grey','Gray sporty H&M shoes',59.99,9,2,'women_shoes1.jpg'),(26,'Beige Casual','Beige H&M style shoes',64.99,9,2,'women_shoes2.jpg'),(27,'Bright White','White casual style H&M shoes',67.99,9,2,'women_shoes3.jpg'),(28,'Urban Noir','Short black taco style boots',74.99,10,2,'boots1.jpg'),(29,'Brown Chic','Short brown taco style boots',79.99,10,2,'boots2.jpg'),(30,'Leather Brown','Long brown leather boots',89.99,10,2,'boots3.jpg'),(31,'Classic White Long Sleeve','Straight white long sleeve shirt',39.99,11,1,'shirt1.jpg'),(32,'Slim White Shirt','Slim fit white long sleeve shirt',41.99,11,1,'shirt2.jpg'),(33,'Light Beige Long Sleeve','Light beige straight shirt with long sleeves',42.99,11,1,'shirt3.jpg'),(34,'Café Classic Straight Pants','Classic light brown straight pants',49.99,12,1,'pants1.jpg'),(35,'Adjustable White Striped Pants','White striped straight pants with adjustable waist',54.99,12,1,'pants2.jpg'),(36,'Light Cotton Straight Pants','Light cotton straight pants',46.99,12,1,'pants3.jpg'),(37,'Bell Bottom Blue Jeans','Oversized blue bell bottom jeans',59.99,13,1,'jeans1.jpg'),(38,'Noir Straight Fit Jeans','Timeless black straight fit jeans',62.99,13,1,'jeans2.jpg'),(39,'Modern Gray Jeans','Straight fit modern gray jeans',64.99,13,1,'jeans3.jpg'),(40,'Dark Green Styled Hoodie','Dark green hoodie with stylish design',54.99,14,1,'hoodie1.jpg'),(41,'Artful White Hoodie','White hoodie adorned with unique illustrations',59.99,14,1,'hoodie2.jpg'),(42,'Timeless Black Hoodie','Classic black hoodie with a timeless design',25.99,14,1,'hoodie3.jpg'),(43,'Green Gray Training Shorts','Green gray shorts for training',29.99,15,1,'sport_shorts1.jpg'),(44,'Classic Black Training Shorts','All black shorts for training',27.99,15,1,'sport_shorts2.jpg'),(45,'Oversized Light Gray Shorts','Light gray oversized cotton shorts',34.99,15,1,'sport_shorts3.jpg'),(46,'Slim Black Running Shirt','Slim fit black shirt for running',31.99,16,1,'running1.jpg'),(47,'Charcoal Long Sleeve Hoodie','Gray long sleeve hoodie',36.99,16,1,'running2.jpg'),(48,'Eclipse Black Sweatshirt','Black long sleeve sweatshirt with a sleek design',39.99,16,1,'running3.jpg'),(49,'Dark Gray Training Hoodie','Dark gray hoodie for training',44.99,17,1,'gym1.jpg'),(50,'Crimson Workout Tank','Red sleeveless tank for workouts',28.99,17,1,'gym2.jpg'),(51,'Black Training Tank','Black sleeveless tank top for training',27.99,17,1,'gym3.jpg'),(52,'Dual-Tone Modern Sneakers','Contemporary black and white sneakers',69.99,18,1,'sneakers1.jpg'),(53,'Classic All-Black Sneakers','Timeless all-black sneakers',72.99,18,1,'sneakers2.jpg'),(54,'Classic All-White Sneakers','Timeless all-white sneakers',72.99,18,1,'sneakers3.jpg'),(55,'Void Black Dress Shoes','Classic black dress shoes with a sleek design',89.99,19,1,'dress_shoe1.jpg'),(56,'Elegant Black Leather Oxfords','Sophisticated black leather dress shoes',94.99,19,1,'dress_shoe2.jpg'),(57,'Classic Brown Leather Dress Shoes','Timeless brown leather dress shoes',89.99,19,1,'dress_shoe3.jpg'),(58,'Classic White Baby Suit','Classic white suit for baby',54.99,20,3,'baby_clothes1.jpg'),(59,'Baby Shirt Set (Cream, Beige, White)','Pack of 3 baby shirts in cream, beige, and white',79.99,20,3,'baby_clothes2.jpg'),(60,'Modern Baby Suit Set (White, Pink, Cream)','Pack of 3 modern baby suits in white, pink, and cream',89.99,20,3,'baby_clothes3.jpg'),(61,'Light Cream Bear Sweater','Light cream sweater with bear design for newborns',34.99,21,3,'baby_outwear1.jpg'),(62,'Cozy Brown Fleece Full-Body Sweater','Brown fleece full-body sweater for newborns',39.99,21,3,'baby_outwear2.jpg'),(63,'Cozy White Fleece Full-Body Sweater','White fleece full-body sweater for newborns',39.99,21,3,'baby_outwear3.jpg'),(64,'Classic Gray Baby Shoes H&M','Gray classic baby shoes from H&M',29.99,22,3,'baby_shoes1.jpg'),(65,'Pink Stuffy Newborn Shoes','Soft pink shoes for newborns',32.99,22,3,'baby_shoes2.jpg'),(66,'Beige Stuffy Newborn Shoes','Cozy beige shoes for newborns',32.99,22,3,'baby_shoes3.jpg'),(67,'White Baby Girl Summer Dress','White sleeveless summer dress for baby girl',34.99,23,2,'babygirl_clothes1.jpg'),(68,'Pink Dress with Floral Pants Set','Pink sleeveless dress with floral pants for baby girl',39.99,23,2,'babygirl_clothes2.jpg'),(69,'Dark Blue Floral Pattern Dress','Dark blue sleeveless dress with floral pattern for baby girl',37.99,23,2,'babygirl_clothes3.jpg'),(70,'White Floral Sweater','White sweater with floral pattern for baby girl',32.99,24,2,'babygirl_outerwear1.jpg'),(71,'Pink Floral Sweater','Pink sweater with floral design for baby girl',32.99,24,2,'babygirl_outerwear2.jpg'),(72,'Dark Blue Windbreaker Jacket','Dark blue windbreaker jacket',42.99,24,2,'babygirl_outerwear3.jpg'),(73,'White Sneakers with White Bow H&M','White sneakers with a white bow from H&M',29.99,25,2,'babygirl_shoes1.jpg'),(74,'Cream Puppy Boots H&M','Cream-colored puppy-themed boots from H&M',34.99,25,2,'babygirl_shoes2.jpg'),(75,'Sky Blue Velcro Sneakers H&M','Sky blue sneakers with velcro straps from H&M',31.99,25,2,'babygirl_shoes3.jpg'),(76,'Simba Brown Casual Shirt','Casual brown shirt featuring Simba design',27.99,26,1,'babyboy_clothes1.jpg'),(77,'Dark Gray Low-Pocket Pants','Dark gray pants with low pockets',39.99,26,1,'babyboy_clothes2.jpg'),(78,'Mickey Mouse Gray Shirt and Shorts Set','Gray shirt and shorts set featuring Mickey Mouse',44.99,26,1,'babyboy_clothes3.jpg'),(79,'Dark Blue Winter Button Sweater','Dark blue winter sweater with buttons',54.99,27,1,'babyboy_outerwear1.jpg'),(80,'Sky Blue Casual Zip Sweater','Casual zip-up sweater in sky blue',49.99,27,1,'babyboy_outerwear2.jpg'),(81,'Brown Sleeveless Winter Zip Vest','Brown sleeveless vest with a zip for winter',47.99,27,1,'babyboy_outerwear3.jpg'),(82,'Red and White Velcro Casual Shoes','Casual red and white shoes with velcro straps',34.99,28,1,'babyboy_shoes1.jpg'),(83,'Blue and Dark Green Velcro Casual Shoes','Casual shoes in blue, dark green, and white with velcro straps',36.99,28,1,'babyboy_shoes2.jpg'),(84,'Gray Casual Lace-Up Shoes','Casual gray shoes with lace-up closure',39.99,28,1,'babyboy_shoes3.jpg'),(85,'Light Blue Casual Jeans','Light blue casual jeans',44.99,29,1,'boy1_clothes1.jpg'),(86,'Dark Gray Sports Shirt','Dark gray shirt designed for sports activities',29.99,29,1,'boy1_clothes2.jpg'),(87,'Light Blue Casual Polo','Light blue polo shirt for casual wear',32.99,29,1,'boy1_clothes3.jpg'),(88,'Navy Blue Soccer Shoes with Laces','Navy blue soccer shoes with laces',54.99,30,1,'boy1_shoes1.jpg'),(89,'White Velcro Shoes with Laces','White shoes with velcro straps and laces',39.99,30,1,'boy1_shoes2.jpg'),(90,'Dark Teal Mickey Mouse Velcro Shoes','Dark teal shoes with Mickey Mouse design and velcro closure',44.99,30,1,'boy1_shoes3.jpg'),(91,'Black Minecraft Varsity Jacket','Black varsity jacket with Minecraft design',59.99,31,1,'boy1_outerwear1.jpg'),(92,'Cream Elegant Zip-Up Sweater','Elegant cream sweater with zip closure',49.99,31,1,'boy1_outerwear2.jpg'),(93,'Sky Blue Button-Up Varsity Jacket','Sky blue varsity jacket with button closure',57.99,31,1,'boy1_outerwear3.jpg'),(94,'Dark Blue Thermal Pants','Dark blue thermal pants for extra warmth',49.99,32,1,'boy2_clothes1.jpg'),(95,'Los Angeles Black Casual Shirt','Black casual shirt with Los Angeles design',34.99,32,1,'boy2_clothes2.jpg'),(96,'Light Blue Casual Shirt','Light blue casual shirt for everyday wear',32.99,32,1,'boy2_clothes3.jpg'),(97,'Modern Brown Velcro and Lace Shoes','Modern brown shoes with both velcro straps and laces',44.99,33,1,'boy2_shoes1.jpg'),(98,'White Casual Sports Shoes','White casual sports shoes for everyday wear',39.99,33,1,'boy2_shoes1.jpg'),(99,'Black Casual Sports Shoes','Black casual sports shoes for everyday wear',39.99,33,1,'boy2_shoes3.jpg'),(100,'All Black Extra Pocket Sweater','All black sweater with extra pockets for added utility',54.99,34,1,'boy2_outerwear1.jpg'),(101,'PlayStation Rain Jacket Black and Blue','Rain jacket in black and blue with PlayStation design',62.99,34,1,'boy2_outerwear2.jpg'),(102,'All Black Leather Jacket with Zipper','All black leather jacket with zipper closure',79.99,34,1,'boy2_outerwear3.jpg'),(103,'White High Collar Jacket','White jacket with a high collar and zipper closure',49.99,35,2,'girl1_clothes1.jpg'),(104,'Classic White Polka Dot Jacket','White jacket with classic polka dot pattern',52.99,35,2,'girl1_clothes2.jpg'),(105,'White Floral Pattern Button Jacket','White jacket with floral pattern and button closure',55.99,35,2,'girl1_clothes3.jpg'),(106,'Barbie Pink Shoes','Bright pink shoes with Barbie design',34.99,36,2,'girl1_shoes1.jpg'),(107,'H&M Black Formal Shoes','Black formal shoes from H&M',49.99,36,2,'girl1_shoes2.jpg'),(108,'Elegant Pink Bow Sandals','Stylish sandals with an elegant pink bow',29.99,36,2,'girl1_shoes3.jpg'),(109,'Blue Jean Button Jacket','Blue jean jacket with long sleeves and button closure',59.99,37,2,'girl1_outerwear1.jpg'),(110,'White Sleeveless Windbreaker','White sleeveless windbreaker with toon designs',45.99,37,2,'girl1_outerwear2.jpg'),(111,'Retro Black Leather Jacket','Black retro leather jacket with zipper closure',55.99,37,2,'girl1_outerwear3.jpg'),(112,'Black Street Tee','Black tee with central illustration, street style',25.99,38,2,'girl2_clothes1.jpg'),(113,'Light Brown Cargo Pants','Light brown cargo pants with multiple pockets',42.99,38,2,'girl2_clothes2.jpg'),(114,'Black School Shirt','Black school shirt with side image',29.99,38,2,'girl2_clothes3.jpg'),(115,'Dark Gray Casual Shoes','Dark gray casual shoes, lace-up style',34.99,39,2,'girl2_shoes1.jpg'),(116,'Black Bow Sandals','Elegant black sandals with a fine bow, H&M',29.99,39,2,'girl2_shoes2.jpg'),(117,'All White Sports Shoes','Completely white sports shoes for a clean look',44.99,39,2,'girl2_shoes3.jpg'),(118,'Black and Light Brown Varsity Jacket','Varsity jacket in black and light brown',69.99,40,2,'girl2_outerwear1.jpg'),(119,'Light Blue Jean Jacket','Light blue jean jacket with button closure',59.99,40,2,'girl2_outerwear2.jpg'),(120,'Full Black Rain Jacket','Black rain jacket with zipper closure',54.99,40,2,'girl2_outerwear3.jpg');
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sesion`
--

DROP TABLE IF EXISTS `sesion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sesion` (
  `idSesion` int NOT NULL AUTO_INCREMENT,
  `idCliente` int DEFAULT NULL,
  PRIMARY KEY (`idSesion`),
  KEY `idCliente` (`idCliente`),
  CONSTRAINT `sesion_ibfk_1` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sesion`
--

LOCK TABLES `sesion` WRITE;
/*!40000 ALTER TABLE `sesion` DISABLE KEYS */;
/*!40000 ALTER TABLE `sesion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subcategoria`
--

DROP TABLE IF EXISTS `subcategoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subcategoria` (
  `idSubcategoria` int NOT NULL AUTO_INCREMENT,
  `nombre_Subcategoria` varchar(70) DEFAULT NULL,
  `idCategoria` int DEFAULT NULL,
  PRIMARY KEY (`idSubcategoria`),
  KEY `idCategoria` (`idCategoria`),
  CONSTRAINT `subcategoria_ibfk_1` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idCategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcategoria`
--

LOCK TABLES `subcategoria` WRITE;
/*!40000 ALTER TABLE `subcategoria` DISABLE KEYS */;
INSERT INTO `subcategoria` VALUES (1,'Clothes',1),(2,'Sport',1),(3,'Shoes',1),(4,'Clothes',2),(5,'Sport',2),(6,'Shoes',2),(7,'Newborn',3),(8,'Babygirl',3),(9,'Babyboy',3),(10,'Boy1',4),(11,'Boy2',4),(12,'Girl1',4),(13,'Girl2',4);
/*!40000 ALTER TABLE `subcategoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subsub`
--

DROP TABLE IF EXISTS `subsub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subsub` (
  `idSubsub` int NOT NULL AUTO_INCREMENT,
  `nombre_Subsub` varchar(70) DEFAULT NULL,
  `idSubcategoria` int DEFAULT NULL,
  PRIMARY KEY (`idSubsub`),
  KEY `idSubcategoria` (`idSubcategoria`),
  CONSTRAINT `subsub_ibfk_1` FOREIGN KEY (`idSubcategoria`) REFERENCES `subcategoria` (`idSubcategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subsub`
--

LOCK TABLES `subsub` WRITE;
/*!40000 ALTER TABLE `subsub` DISABLE KEYS */;
INSERT INTO `subsub` VALUES (1,'Dresses',1),(2,'Tops',1),(3,'Blouses',1),(4,'Jeans',1),(5,'Leggings',2),(6,'Sportbras',2),(7,'Running',2),(8,'Gym',2),(9,'Sneakers',3),(10,'Boots',3),(11,'Shirts',4),(12,'Pants',4),(13,'Jeans',4),(14,'Hoodies',4),(15,'Shorts',5),(16,'Running',5),(17,'Gym',5),(18,'Sneakers',6),(19,'Dress Shoes',6),(20,'Clothing',7),(21,'Outerwear',7),(22,'Shoes',7),(23,'Clothing',8),(24,'Outerwear',8),(25,'Shoes',8),(26,'Clothing',9),(27,'Outerwear',9),(28,'Shoes',9),(29,'Clothing',10),(30,'Shoes',10),(31,'Outerwear',10),(32,'Clothing',11),(33,'Shoes',11),(34,'Outerwear',11),(35,'Clothing',12),(36,'Shoes',12),(37,'Outerwear',12),(38,'Clothing',13),(39,'Shoes',13),(40,'Outerwear',13);
/*!40000 ALTER TABLE `subsub` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-18 16:28:01
