CREATE DATABASE IF NOT EXISTS `ticketdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION = 'N' */;
USE `ticketdb`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: ticketdb
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account`
(
    `account_number` varchar(19) NOT NULL,
    `account_type`   enum ('Checking Account','Savings Account','Credit Card Account','Debit Card Account') DEFAULT NULL,
    PRIMARY KEY (`account_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account`
    DISABLE KEYS */;
INSERT INTO `account`
VALUES ('111122223333', 'Checking Account');
/*!40000 ALTER TABLE `account`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_user`
--

DROP TABLE IF EXISTS `account_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_user`
(
    `account_number` varchar(19)  NOT NULL,
    `user_username`  varchar(255) NOT NULL,
    PRIMARY KEY (`account_number`, `user_username`),
    KEY `user_username` (`user_username`),
    CONSTRAINT `account_user_ibfk_1` FOREIGN KEY (`account_number`) REFERENCES `account` (`account_number`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `account_user_ibfk_2` FOREIGN KEY (`user_username`) REFERENCES `user` (`user_username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_user`
--

LOCK TABLES `account_user` WRITE;
/*!40000 ALTER TABLE `account_user`
    DISABLE KEYS */;
INSERT INTO `account_user`
VALUES ('111122223333', 'admin'),
       ('111122223333', 'admin2');
/*!40000 ALTER TABLE `account_user`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event`
(
    `event_id`      int                                                       NOT NULL AUTO_INCREMENT,
    `event_name`    varchar(255)                                              NOT NULL,
    `event_date`    date                                                      NOT NULL,
    `event_type`    enum ('Sport','Concert','Art & Theater','Family','Other') NOT NULL,
    `stadium_id`    int                                                       NOT NULL,
    `host_username` varchar(64)                                               NOT NULL,
    PRIMARY KEY (`event_id`),
    KEY `stadium_id` (`stadium_id`),
    KEY `host_username` (`host_username`),
    CONSTRAINT `event_ibfk_1` FOREIGN KEY (`stadium_id`) REFERENCES `stadium` (`stadium_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `event_ibfk_2` FOREIGN KEY (`host_username`) REFERENCES `host` (`host_username`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event`
    DISABLE KEYS */;
INSERT INTO `event`
VALUES (1, 'Manchester United vs Machester City', '2024-06-12', 'Sport', 1, 'admin'),
       (2, 'Gathering', '2024-08-19', 'Family', 2, 'admin');
/*!40000 ALTER TABLE `event`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `host`
--

DROP TABLE IF EXISTS `host`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `host`
(
    `host_username`     varchar(64)  NOT NULL,
    `host_password`     varchar(64)  NOT NULL,
    `host_email`        varchar(64)  NOT NULL,
    `host_phone_number` varchar(32)  NOT NULL,
    `host_first_name`   varchar(255) NOT NULL,
    `host_last_name`    varchar(255) NOT NULL,
    `host_birth_date`   date         NOT NULL,
    PRIMARY KEY (`host_username`),
    UNIQUE KEY `host_email` (`host_email`),
    UNIQUE KEY `host_phone_number` (`host_phone_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `host`
--

LOCK TABLES `host` WRITE;
/*!40000 ALTER TABLE `host`
    DISABLE KEYS */;
INSERT INTO `host`
VALUES ('admin', 'admin', 'admin@hotmail.com', '1234567890', 'admin', 'admin', '2000-01-01'),
       ('admin2', 'admin2', 'admin2@hotmail.com', '1234567891', 'admin2', 'admin2', '2000-02-03');
/*!40000 ALTER TABLE `host`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seat`
--

DROP TABLE IF EXISTS `seat`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seat`
(
    `seat_id`      int                                                                                                      NOT NULL AUTO_INCREMENT,
    `seat_section` varchar(32) DEFAULT NULL,
    `seat_row`     int                                                                                                      NOT NULL,
    `seat_number`  int                                                                                                      NOT NULL,
    `seat_type`    enum ('General Admission','Box Seats','Club Seats','Suites','Accessible Seats','Standing Areas','Other') NOT NULL,
    `stadium_id`   int                                                                                                      NOT NULL,
    PRIMARY KEY (`seat_id`, `stadium_id`),
    KEY `stadium_id` (`stadium_id`),
    CONSTRAINT `seat_ibfk_1` FOREIGN KEY (`stadium_id`) REFERENCES `stadium` (`stadium_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat`
--

LOCK TABLES `seat` WRITE;
/*!40000 ALTER TABLE `seat`
    DISABLE KEYS */;
INSERT INTO `seat`
VALUES (1, 'Level 1', 1, 1, 'General Admission', 1),
       (2, 'Level 2', 2, 1, 'Box Seats', 1),
       (3, 'Level 3', 3, 1, 'Club Seats', 1),
       (4, 'Level 4', 4, 1, 'Suites', 1);
/*!40000 ALTER TABLE `seat`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stadium`
--

DROP TABLE IF EXISTS `stadium`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stadium`
(
    `stadium_id`     int          NOT NULL AUTO_INCREMENT,
    `stadium_name`   varchar(255) NOT NULL,
    `capacity`       int          NOT NULL,
    `address_line_1` varchar(255) NOT NULL,
    `address_line_2` varchar(255) DEFAULT NULL,
    `city`           varchar(64)  NOT NULL,
    `state`          varchar(64)  NOT NULL,
    `country`        varchar(64)  NOT NULL,
    `zip_code`       int          NOT NULL,
    PRIMARY KEY (`stadium_id`),
    UNIQUE KEY `address_line_1` (`address_line_1`, `address_line_2`, `city`, `state`, `country`, `zip_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stadium`
--

LOCK TABLES `stadium` WRITE;
/*!40000 ALTER TABLE `stadium`
    DISABLE KEYS */;
INSERT INTO `stadium`
VALUES (1, 'Trafford Stadium', 74310, 'Sir Matt Busby Way', NULL, 'Manchester', 'MN', 'United Kindom', 12345),
       (2, 'Crypto Arena', 20000, '1111 S Figueroa St', NULL, 'Los Angelas', 'CA', 'United States', 90015);
/*!40000 ALTER TABLE `stadium`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store`
(
    `store_id`   int                                                                     NOT NULL AUTO_INCREMENT,
    `store_name` varchar(255)                                                            NOT NULL,
    `store_type` enum ('Merchandise','Food and Beverage','Souvenir','Fan Shops','Other') NOT NULL,
    `stadium_id` int                                                                     NOT NULL,
    PRIMARY KEY (`store_id`, `stadium_id`),
    KEY `stadium_id` (`stadium_id`),
    CONSTRAINT `store_ibfk_1` FOREIGN KEY (`stadium_id`) REFERENCES `stadium` (`stadium_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store`
    DISABLE KEYS */;
INSERT INTO `store`
VALUES (1, 'Burger King', 'Food and Beverage', 1);
/*!40000 ALTER TABLE `store`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket`
(
    `ticket_id`      int NOT NULL AUTO_INCREMENT,
    `booking_date`   date         DEFAULT NULL,
    `price`          int NOT NULL,
    `first_name`     varchar(255) DEFAULT '',
    `last_name`      varchar(255) DEFAULT '',
    `seat_id`        int NOT NULL,
    `stadium_id`     int NOT NULL,
    `event_id`       int NOT NULL,
    `buyer`          varchar(255) DEFAULT NULL,
    `seller`         varchar(255) DEFAULT NULL,
    `buyer_account`  varchar(19)  DEFAULT NULL,
    `seller_account` varchar(19)  DEFAULT NULL,
    PRIMARY KEY (`ticket_id`),
    UNIQUE KEY `seat_id` (`seat_id`, `stadium_id`, `event_id`),
    KEY `stadium_id` (`stadium_id`),
    KEY `event_id` (`event_id`),
    KEY `buyer` (`buyer`),
    KEY `seller` (`seller`),
    KEY `buyer_account` (`buyer_account`),
    KEY `seller_account` (`seller_account`),
    CONSTRAINT `ticket_ibfk_1` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`seat_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `ticket_ibfk_2` FOREIGN KEY (`stadium_id`) REFERENCES `stadium` (`stadium_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `ticket_ibfk_3` FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `ticket_ibfk_4` FOREIGN KEY (`buyer`) REFERENCES `user` (`user_username`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `ticket_ibfk_5` FOREIGN KEY (`seller`) REFERENCES `user` (`user_username`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `ticket_ibfk_6` FOREIGN KEY (`buyer_account`) REFERENCES `account` (`account_number`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `ticket_ibfk_7` FOREIGN KEY (`seller_account`) REFERENCES `account` (`account_number`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket`
    DISABLE KEYS */;
INSERT INTO `ticket`
VALUES (1, '2020-10-10', 50, 'Jonathan', 'Q', 1, 1, 1, 'admin', NULL, '111122223333', NULL),
       (2, '2020-05-12', 100, 'Patrick', 'J', 2, 1, 1, 'admin', NULL, '111122223333', NULL),
       (3, NULL, 150, '', '', 3, 1, 1, NULL, NULL, NULL, NULL),
       (4, NULL, 200, '', '', 4, 1, 1, NULL, NULL, NULL, NULL),
       (5, NULL, 250, '', '', 1, 2, 2, NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `ticket`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
/*!50003 CREATE */ /*!50017 DEFINER =`root`@`localhost`*/ /*!50003 TRIGGER `check_buyer_and_seller_are_not_same_on_insert`
    BEFORE INSERT
    ON `ticket`
    FOR EACH ROW
BEGIN
    IF (NEW.buyer IS NOT NULL AND NEW.seller IS NOT NULL) AND (NEW.buyer = NEW.seller) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = "Buyer and Seller cannot be the same user";
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user`
(
    `user_username`     varchar(64)  NOT NULL,
    `user_password`     varchar(64)  NOT NULL,
    `user_email`        varchar(255) NOT NULL,
    `user_phone_number` varchar(32)  NOT NULL,
    `user_birth_year`   year         NOT NULL,
    PRIMARY KEY (`user_username`),
    UNIQUE KEY `user_email` (`user_email`),
    UNIQUE KEY `user_phone_number` (`user_phone_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
    DISABLE KEYS */;
INSERT INTO `user`
VALUES ('admin', 'admin', 'admin@hotmail.com', '1234567890', 2005),
       ('admin2', 'admin2', 'admin2@hotmail.com', '1234567891', 2019);
/*!40000 ALTER TABLE `user`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'ticketdb'
--

--
-- Dumping routines for database 'ticketdb'
--
/*!50003 DROP PROCEDURE IF EXISTS `add_host` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `add_host`(IN username VARCHAR(64), IN password VARCHAR(64),
                                                      IN email VARCHAR(64), IN phone VARCHAR(32), IN first VARCHAR(255),
                                                      IN last VARCHAR(255), IN birth DATE)
BEGIN
    INSERT INTO host VALUES (username, password, email, phone, first, last, birth);
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `add_store` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `add_store`(IN name VARCHAR(255),
                                                       IN type ENUM ("Merchandise", "Food and Beverage", "Souvenir", "Fan Shops", "Other"),
                                                       IN stadium INT)
BEGIN
    INSERT INTO store(store_name, store_type, stadium_id) VALUES (name, type, stadium);
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `add_user` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `add_user`(IN username VARCHAR(64), IN password VARCHAR(64),
                                                      IN email VARCHAR(64), IN phone VARCHAR(32), IN birth YEAR)
BEGIN
    INSERT INTO user VALUES (username, password, email, phone, birth);
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `create_account` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `create_account`(IN account_ VARCHAR(19),
                                                            IN type_ ENUM ("Checking Account", "Savings Account", "Credit Card Account", "Debit Card Account"))
BEGIN
    INSERT INTO account VALUES (account_, type_);
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `create_account_user` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `create_account_user`(IN account_ VARCHAR(19), IN username_ VARCHAR(64))
BEGIN
    INSERT INTO account_user VALUES (account_, username_);
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `create_event` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `create_event`(IN name_ VARCHAR(255), IN date_ DATE,
                                                          IN event_ ENUM ("Sport", "Concert", "Art & Theater", "Family", "Other"),
                                                          IN stadium_ INT, IN host_ VARCHAR(64))
BEGIN
    INSERT INTO event(event_name, event_date, event_type, stadium_id, host_username)
    VALUES (name_, date_, event_, stadium_, host_);
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `create_seats` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `create_seats`(IN row_ INT, IN num INT,
                                                          IN type_ ENUM ("General Admission", "Box Seats", "Club Seats", "Suites", "Accessible Seats", "Standing Areas", "Other"),
                                                          IN id INT)
BEGIN
    INSERT INTO seat(seat_row, seat_number, seat_type, stadium_id) VALUES (row_, num, type_, id);
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `create_stadium` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `create_stadium`(IN name VARCHAR(255), IN capacity INT,
                                                            IN address_1 VARCHAR(255), IN address_2 VARCHAR(255),
                                                            IN city VARCHAR(64), IN state VARCHAR(64),
                                                            IN country VARCHAR(64), IN zip_code int)
BEGIN
    INSERT INTO stadium(stadium_name, capacity, address_line_1, address_line_2, city, state, country,
                        zip_code) VALUE (name, capacity, address_1, address_2, city, state, country, zip_code);
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `create_ticket` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `create_ticket`(IN price_ INT, IN seat_id_ INT, IN stadium_id_ INT, IN event_id_ INT)
BEGIN
    INSERT INTO ticket(price, seat_id, stadium_id, event_id)
    VALUES (price_, seat_id_, stadium_id_, event_id_);
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `delete_all_ticket_related_to_event` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `delete_all_ticket_related_to_event`(IN event_id_ INT)
BEGIN
    DELETE FROM ticket WHERE event_id = event_id_;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `delete_event` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `delete_event`(IN event_id_ INT)
BEGIN
    DELETE FROM event WHERE event_id = event_id_;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `delete_stadium` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `delete_stadium`(IN id INT)
BEGIN
    DELETE FROM stadium WHERE stadium_id = id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `delete_store` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `delete_store`(IN id INT)
BEGIN
    DELETE FROM store WHERE store_id = id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `delete_user` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `delete_user`(IN id INT)
BEGIN
    DELETE FROM user WHERE user_username = id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_account_number` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_account_number`(IN user_ VARCHAR(64))
BEGIN
    SELECT au.account_number
    FROM user AS u
             JOIN account_user AS au ON u.user_username = au.user_username
    WHERE u.user_username = user_;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_empty_seat_from_stadium_for_event` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_all_empty_seat_from_stadium_for_event`(IN event_id_ INT)
BEGIN
    SELECT s.*
    FROM ticket t
             JOIN seat s ON t.seat_id = s.seat_id AND t.stadium_id = s.stadium_id
    WHERE t.event_id = event_id_
      AND t.buyer IS NULL;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_event` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_all_event`()
BEGIN
    select * from event;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_event_with_stadium_name` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_all_event_with_stadium_name`()
BEGIN
    SELECT e.*, s.stadium_name
    FROM event AS e
             JOIN stadium AS s ON s.stadium_id = e.stadium_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_host` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_all_host`()
BEGIN
    SELECT * FROM host;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_seat_id_from_stadium` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_all_seat_id_from_stadium`(IN stadium_ INT)
BEGIN
    SELECT seat_id FROM seat WHERE stadium_id = stadium_;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_stadium` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_all_stadium`()
BEGIN
    select * from stadium;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_ticket_information` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_all_ticket_information`()
BEGIN
    select t.ticket_id, e.event_date, s.seat_row, s.seat_number, st.stadium_name, e.event_name, e.event_id
    from ticket as t
             join seat as s on s.seat_id = t.seat_id
             join stadium as st on t.stadium_id = st.stadium_id
             join event as e on e.event_id = t.event_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_ticket_information_related_to_user` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_all_ticket_information_related_to_user`(IN user_id VARCHAR(255))
BEGIN
    select t.ticket_id,
           t.booking_date,
           t.price,
           t.first_name,
           t.last_name,
           t.seat_id,
           s.seat_row,
           s.seat_number,
           st.stadium_name,
           st.stadium_id,
           e.event_id,
           e.event_name
    from ticket as t
             join seat as s on s.seat_id = t.seat_id
             join stadium as st on s.stadium_id = st.stadium_id
             join event as e on e.event_id = t.event_id
    where t.buyer = user_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_user` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_all_user`()
BEGIN
    SELECT * FROM user;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_buy_ticket_info` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_buy_ticket_info`(IN event_id_ INT)
BEGIN
    SELECT t.price, s.seat_id, s.seat_row, s.seat_number, s.seat_type
    from ticket as t
             JOIN event as e on e.event_id = t.event_id
             JOIN seat as s on s.seat_id = t.seat_id
    WHERE t.buyer IS NULL
      AND e.event_id = event_id_;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_events_in_the_same_stadium` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_events_in_the_same_stadium`(IN stadium_id_ INT)
BEGIN
    SELECT *
    FROM event
    WHERE stadium_id = stadium_id_;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_event_id` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_event_id`(IN name_ VARCHAR(255), IN date_ DATE,
                                                          IN event_ ENUM ("Sport", "Concert", "Art & Theater", "Family", "Other"),
                                                          IN stadium_ INT, IN host_ VARCHAR(64))
BEGIN
    SELECT event_id
    FROM event
    WHERE event_name = name_
      AND event_date = date_
      AND event_type = event_
      AND stadium_id = stadium_
      AND host_username = host_;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_event_info` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_event_info`()
BEGIN
    select t.ticket_id,
           t.booking_date,
           st.stadium_name,
           e.event_id,
           e.event_name
    from ticket as t
             join event as e on e.event_id = t.event_id
             join stadium as st on s.stadium_id = e.stadium_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_seat_from_row_number_stadium` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_seat_from_row_number_stadium`(IN row_ INT, IN num_ INT, IN stadium_id_ INT)
BEGIN
    SELECT s.seat_id
    FROM seat AS s
             JOIN stadium AS st on s.stadium_id = st.stadium_id
    WHERE s.seat_row = row_
      AND s.seat_number = num_;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_stadium_id` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_stadium_id`(IN event_id_ INT)
BEGIN
    SELECT stadium_id
    FROM event
    WHERE event_id = event_id_;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_store_and_stadium_name` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_store_and_stadium_name`()
BEGIN
    SELECT s.*, st.stadium_name
    FROM store AS s
             JOIN stadium AS st ON s.stadium_id = st.stadium_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_ticket` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_ticket`(IN ticket_id_ INT)
BEGIN
    SELECT * FROM ticket WHERE ticket_id = ticket_id_;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `get_ticket_from_seat_stadium_event` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `get_ticket_from_seat_stadium_event`(IN seat_id_ INT, IN stadium_id_ INT, IN event_id_ INT)
BEGIN
    SELECT ticket_id
    FROM ticket
    WHERE seat_id = seat_id_
      AND stadium_id = stadium_id_
      AND event_id = event_id_;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `reset_ticket_trading_information` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `reset_ticket_trading_information`(IN ticket INT)
BEGIN
    UPDATE TICKET
    SET booking_date   = null,
        buyer          = null,
        seller         = null,
        buyer_account  = null,
        seller_account = null,
        first_name     = null,
        last_name      = null
    WHERE ticket_id = ticket;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `update_ticket` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `update_ticket`(IN booking_date_ DATE, IN first_name_ VARCHAR(255),
                                                           IN last_name_ VARCHAR(255), IN buyer_ VARCHAR(255),
                                                           IN ticket_id_ INT)
BEGIN
    UPDATE ticket
    SET booking_date = booking_date_,
        first_name   = first_name_,
        last_name    = last_name_,
        buyer        = buyer_
    WHERE ticket_id = ticket_id_;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `update_ticket_buyer` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `update_ticket_buyer`(IN first VARCHAR(255), IN last VARCHAR(255),
                                                                 IN buyer_acc VARCHAR(19), IN buy VARCHAR(255),
                                                                 IN ticket INT, IN date_ DATE)
BEGIN
    UPDATE ticket
    SET first_name    = first,
        last_name     = last,
        buyer_account = buyer_acc,
        buyer         = buy,
        booking_date  = date_
    WHERE ticket_id = ticket;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `update_ticket_name` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `update_ticket_name`(IN first VARCHAR(255), IN last VARCHAR(255), IN ticket INT)
BEGIN
    UPDATE ticket SET first_name = first, last_name = last WHERE ticket_id = ticket;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `update_ticket_seller` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `update_ticket_seller`(IN seller_acc VARCHAR(19), IN sell VARCHAR(255), IN ticket INT)
BEGIN
    UPDATE ticket
    SET seller_account = seller_acc,
        seller         = sell
    WHERE ticket_id = ticket;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2024-04-17 14:35:16
