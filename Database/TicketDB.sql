CREATE DATABASE IF NOT EXISTS ticketdb;
USE ticketdb;

CREATE TABLE stadium (
    stadium_id 		INT AUTO_INCREMENT PRIMARY KEY,
    stadium_name 	VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    address_line_1	VARCHAR(255) NOT NULL,
    address_line_2	VARCHAR(255),
    city			VARCHAR(64) NOT NULL,
    state			VARCHAR(64) NOT NULL,
    country			VARCHAR(64) NOT NULL,
    zip_code		INT NOT NULL,
    UNIQUE(address_line_1, address_line_2, city, state, country, zip_code)
);

CREATE TABLE host (
                      host_username     VARCHAR(64) PRIMARY KEY,
                      host_password     VARCHAR(64)        NOT NULL,
                      host_email        VARCHAR(64) UNIQUE NOT NULL,
                      host_phone_number VARCHAR(32) UNIQUE NOT NULL,
                      host_first_name   VARCHAR(255)       NOT NULL,
                      host_last_name    VARCHAR(255)       NOT NULL,
                      host_birth_date   DATE               NOT NULL
);

CREATE TABLE event (
                       event_id      INT AUTO_INCREMENT PRIMARY KEY,
                       event_name    VARCHAR(255)                                                  NOT NULL,
                       event_date    DATE                                                          NOT NULL,
                       event_type    ENUM ("Sport", "Concert", "Art & Theater", "Family", "Other") NOT NULL,
                       stadium_id    INT                                                           NOT NULL,
                       host_username VARCHAR(64)                                                   NOT NULL,
    FOREIGN KEY (stadium_id) REFERENCES stadium(stadium_id)
		ON UPDATE CASCADE ON DELETE RESTRICT,
                       FOREIGN KEY (host_username) REFERENCES host (host_username)
		ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE seat (
                      seat_id      INT AUTO_INCREMENT,
                      seat_section VARCHAR(32),
                      seat_row     INT                                                                                                            NOT NULL,
                      seat_number  INT                                                                                                            NOT NULL,
                      seat_type    ENUM ("General Admission", "Box Seats", "Club Seats", "Suites", "Accessible Seats", "Standing Areas", "Other") NOT NULL,
                      stadium_id   INT,
    PRIMARY KEY(seat_id, stadium_id),
    FOREIGN KEY (stadium_id) REFERENCES stadium(stadium_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE store (
    store_id 	INT	AUTO_INCREMENT,
    store_name 	VARCHAR(255) NOT NULL,
    store_type 	ENUM("Merchandise", "Food and Beverage", "Souvenir", "Fan Shops", "Other") NOT NULL,
    stadium_id	INT NOT NULL,
    PRIMARY KEY (store_id, stadium_id),
    FOREIGN KEY (stadium_id) REFERENCES stadium(stadium_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE user (
                      user_username     VARCHAR(64) PRIMARY KEY,
                      user_password     VARCHAR(64)        NOT NULL,
    user_email 			VARCHAR(255) UNIQUE NOT NULL,
                      user_phone_number VARCHAR(32) UNIQUE NOT NULL,
                      user_birth_year   YEAR               NOT NULL
);

CREATE TABLE account(
                        account_number VARCHAR(19) PRIMARY KEY,
    account_type    ENUM("Checking Account", "Savings Account", "Credit Card Account", "Debit Card Account")
);

CREATE TABLE account_user(
                             account_number VARCHAR(19)  NOT NULL,
                             user_username  VARCHAR(255) NOT NULL,
    PRIMARY KEY (account_number, user_username),
    FOREIGN KEY (account_number) REFERENCES account(account_number)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (user_username) REFERENCES user (user_username)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ticket(
    ticket_id       INT PRIMARY KEY AUTO_INCREMENT,
    booking_date   DATE,
    price           INT NOT NULL,
    first_name      VARCHAR(255) DEFAULT "",
    last_name       VARCHAR(255) DEFAULT "",
    seat_id         INT NOT NULL,
    stadium_id		INT NOT NULL,
    event_id        INT NOT NULL,
    buyer			VARCHAR(255),
    seller			VARCHAR(255),
    buyer_account  VARCHAR(19),
    seller_account VARCHAR(19),
    UNIQUE (seat_id, stadium_id, event_id),
	FOREIGN KEY (seat_id) REFERENCES seat(seat_id)
        on UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY (stadium_id) REFERENCES stadium(stadium_id)
        on UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (event_id) REFERENCES event(event_id)
        on UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (buyer) REFERENCES user (user_username)
        on UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (seller) REFERENCES user (user_username)
        on UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY (buyer_account) REFERENCES account(account_number)
        on UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY (seller_account) REFERENCES account(account_number)
        on UPDATE CASCADE ON DELETE RESTRICT
);

-- INSERT VALUES INTO EXISTING TABLES
-- Add admin host value
INSERT INTO host
VALUES ("admin", "admin", "admin@hotmail.com", "1234567890", "admin", "admin", "2000-01-01"),
       ("admin2", "admin2", "admin2@hotmail.com", "1234567891", "admin2", "admin2", "2000-02-03");
    
    
-- Add admin user value
INSERT INTO user
VALUES ("admin", "admin", "admin@hotmail.com", "1234567890", 2005),
       ("admin2", "admin2", "admin2@hotmail.com", "1234567891", 2019);
    
-- Add stadium value
INSERT INTO stadium(stadium_name, capacity, address_line_1, city, state, country, zip_code)
VALUES ("Trafford Stadium", 74310, "Sir Matt Busby Way", "Manchester", "MN", "United Kindom", "12345"),
       ("Crypto Arena", 20000, "1111 S Figueroa St", "Los Angelas", "CA", "United States", "90015");
    
-- Add store value
INSERT INTO store(store_name, store_type, stadium_id)
VALUES ("Burger King", "Food and Beverage", 1);
    
-- Add Event value
INSERT INTO event(event_name, event_date, event_type, stadium_id, host_username)
VALUES ("Manchester United vs Machester City", "2024-06-12", "Sport", 1, "admin"),
       ("Gathering", "2024-08-19", "Family", 2, "admin");
    
-- Add seat value
INSERT INTO seat(seat_section, seat_row, seat_number, seat_type, stadium_id)
VALUES ("Level 1", 1, 1, "General Admission", 1),
       ("Level 2", 2, 1, "Box Seats", 1),
       ("Level 3", 3, 1, "Club Seats", 1),
       ("Level 4", 4, 1, "Suites", 1);
    
-- Add Ticket values
INSERT INTO ticket(price, seat_id, stadium_id, event_id)
VALUES (50, 1, 1, 1),
       (100, 2, 1, 1),
       (150, 3, 1, 1),
       (200, 4, 1, 1),
       (250, 1, 2, 2);
    
-- Add Account Values
INSERT INTO account
VALUES (0000111122223333, "Checking Account");
    
-- Add Account User connection
INSERT INTO account_user
VALUES (0000111122223333, "admin"),
       (0000111122223333, "admin2");
    
-- Add Ticket related to user values
UPDATE ticket
SET buyer         = "admin",
    buyer_account = 0000111122223333,
    first_name    = "Jonathan",
    last_name     = "Q",
    booking_date  = "2020-10-10"
WHERE ticket_id = 1;
UPDATE ticket
SET buyer         = "admin",
    buyer_account = 0000111122223333,
    first_name    = "Patrick",
    last_name     = "J",
    booking_date  = "2020-05-12"
WHERE ticket_id = 2;

-- TRIGGERS
DELIMITER $$
CREATE TRIGGER check_buyer_and_seller_are_not_same_on_insert
    BEFORE INSERT
    ON ticket
    FOR EACH ROW
BEGIN
    IF (NEW.buyer IS NOT NULL AND NEW.seller IS NOT NULL) AND (NEW.buyer = NEW.seller) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = "Buyer and Seller cannot be the same user";
    END IF;
END$$
DELIMITER ;
		
        
-- --------------------------------------- PROCEDURES
DELIMITER $$
CREATE PROCEDURE get_all_ticket_information()
BEGIN
    select t.ticket_id, e.event_date, s.seat_row, s.seat_number, st.stadium_name, e.event_name, e.event_id
    from ticket as t
             join seat as s on s.seat_id = t.seat_id
             join stadium as st on t.stadium_id = st.stadium_id
             join event as e on e.event_id = t.event_id;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_all_ticket_information_related_to_user(IN user_id VARCHAR (255))
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
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_event_info()
BEGIN
    select t.ticket_id,
           t.booking_date,
           st.stadium_name,
           e.event_id,
           e.event_name
    from ticket as t
             join event as e on e.event_id = t.event_id
             join stadium as st on s.stadium_id = e.stadium_id;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_buy_ticket_info(IN event_id_ INT)
BEGIN
    SELECT t.price, s.seat_id, s.seat_row, s.seat_number, s.seat_type
    from ticket as t
             JOIN event as e on e.event_id = t.event_id
             JOIN seat as s on s.seat_id = t.seat_id
    WHERE t.buyer IS NULL
      AND e.event_id = event_id_;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_ticket_from_seat_stadium_event(IN seat_id_ INT, IN stadium_id_ INT, IN event_id_ INT)
BEGIN
    SELECT ticket_id
    FROM ticket
    WHERE seat_id = seat_id_
      AND stadium_id = stadium_id_
      AND event_id = event_id_;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE update_ticket(IN booking_date_ DATE, IN first_name_ VARCHAR(255), IN last_name_ VARCHAR(255),
                               IN buyer_ VARCHAR(255), IN ticket_id_ INT)
BEGIN
    UPDATE ticket
    SET booking_date = booking_date_,
    first_name   = first_name_,
    last_name    = last_name_,
    buyer        = buyer_
    WHERE ticket_id = ticket_id_;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE reset_ticket_trading_information(IN ticket INT)
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
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE create_event(IN name_ VARCHAR(255), IN date_ DATE,
                              IN event_ ENUM ("Sport", "Concert", "Art & Theater", "Family", "Other"), IN stadium_ INT,
                              IN host_ VARCHAR(64))
BEGIN
    INSERT INTO event(event_name, event_date, event_type, stadium_id, host_username)
    VALUES (name_, date_, event_, stadium_, host_);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_all_event()
BEGIN
    select * from event;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_all_event_with_stadium_name()
BEGIN
    SELECT e.*, s.stadium_name
    FROM event AS e
         JOIN stadium AS s ON s.stadium_id = e.stadium_id;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_stadium_id(IN event_id_ INT)
BEGIN
    SELECT stadium_id
    FROM event
    WHERE event_id = event_id_;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_event_id(IN name_ VARCHAR(255), IN date_ DATE,
                              IN event_ ENUM ("Sport", "Concert", "Art & Theater", "Family", "Other"), IN stadium_ INT,
                              IN host_ VARCHAR(64))
BEGIN
    SELECT event_id
    FROM event
    WHERE event_name = name_
      AND event_date = date_
      AND event_type = event_
      AND stadium_id = stadium_
      AND host_username = host_;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_events_in_the_same_stadium(IN stadium_id_ INT)
BEGIN
    SELECT *
    FROM event
    WHERE stadium_id = stadium_id_;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE delete_event(IN event_id_ INT)
BEGIN
    DELETE FROM event WHERE event_id = event_id_;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE create_stadium(IN name VARCHAR(255), IN capacity INT, IN address_1 VARCHAR(255),
                                IN address_2 VARCHAR(255), IN city VARCHAR(64), IN state VARCHAR(64),
                                IN country VARCHAR(64), IN zip_code int)
BEGIN
    INSERT INTO stadium(stadium_name, capacity, address_line_1, address_line_2, city, state, country,
                        zip_code) VALUE (name, capacity, address_1, address_2, city, state, country, zip_code);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_all_stadium()
BEGIN
    select * from stadium;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_all_seat_id_from_stadium(IN stadium_ INT)
BEGIN
    SELECT seat_id FROM seat WHERE stadium_id = stadium_;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE delete_stadium(IN id INT)
BEGIN
    DELETE FROM stadium WHERE stadium_id = id;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_all_empty_seat_from_stadium_for_event(IN event_id_ INT)
BEGIN
    SELECT s.*
    FROM ticket t
             JOIN seat s ON t.seat_id = s.seat_id AND t.stadium_id = s.stadium_id
    WHERE t.event_id = event_id_
      AND t.buyer IS NULL;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_seat_from_row_number_stadium(IN row_ INT, IN num_ INT, IN stadium_id_ INT)
BEGIN
    SELECT s.seat_id
    FROM seat AS s
         JOIN stadium AS st on s.stadium_id = st.stadium_id
    WHERE s.seat_row = row_
      AND s.seat_number = num_;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE create_ticket(IN price_ INT, IN seat_id_ INT, IN stadium_id_ INT, IN event_id_ INT)
BEGIN
    INSERT INTO ticket(price, seat_id, stadium_id, event_id)
    VALUES (price_, seat_id_, stadium_id_, event_id_);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_ticket(IN ticket_id_ INT)
BEGIN
    SELECT * FROM ticket WHERE ticket_id = ticket_id_;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE update_ticket_name(IN first VARCHAR (255), IN last VARCHAR (255), IN ticket INT)
BEGIN
    UPDATE ticket SET first_name = first, last_name = last WHERE ticket_id = ticket;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE update_ticket_buyer(IN first VARCHAR(255), IN last VARCHAR(255),
                                     IN buyer_acc VARCHAR(19), IN buy VARCHAR(255), IN ticket INT, IN date_ DATE)
BEGIN
    UPDATE ticket
    SET first_name    = first,
        last_name     = last,
        buyer_account = buyer_acc,
        buyer         = buy,
        booking_date  = date_
    WHERE ticket_id = ticket;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE update_ticket_seller(IN seller_acc VARCHAR(19), IN sell VARCHAR(255), IN ticket INT)
BEGIN
    UPDATE ticket
    SET seller_account = seller_acc,
        seller         = sell
    WHERE ticket_id = ticket;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE delete_all_ticket_related_to_event(IN event_id_ INT)
BEGIN
    DELETE FROM ticket WHERE event_id = event_id_;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_all_host()
BEGIN
    SELECT * FROM host;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE add_host(IN username VARCHAR(64), IN password VARCHAR(64), IN email VARCHAR(64), IN phone VARCHAR(32),
                          IN first VARCHAR(255), IN last VARCHAR(255), IN birth DATE)
BEGIN
    INSERT INTO host VALUES (username, password, email, phone, first, last, birth);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_all_user()
BEGIN
    SELECT * FROM user;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE add_user(IN username VARCHAR(64), IN password VARCHAR(64), IN email VARCHAR(64), IN phone VARCHAR(32),
                          IN birth YEAR)
BEGIN
    INSERT INTO user VALUES (username, password, email, phone, birth);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE delete_user(IN id INT)
BEGIN
    DELETE FROM user WHERE user_username = id;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_store_and_stadium_name()
BEGIN
    SELECT s.*, st.stadium_name
    FROM store AS s
             JOIN stadium AS st ON s.stadium_id = st.stadium_id;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE add_store(IN name VARCHAR(255),
                           IN type ENUM ("Merchandise", "Food and Beverage", "Souvenir", "Fan Shops", "Other"),
                           IN stadium INT)
BEGIN
    INSERT INTO store(store_name, store_type, stadium_id) VALUES (name, type, stadium);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE delete_store(IN id INT)
BEGIN
    DELETE FROM store WHERE store_id = id;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE create_seats(IN row_ INT, IN num INT,
                              IN type_ ENUM ("General Admission", "Box Seats", "Club Seats", "Suites", "Accessible Seats", "Standing Areas", "Other"),
                              IN id INT)
BEGIN
    INSERT INTO seat(seat_row, seat_number, seat_type, stadium_id) VALUES (row_, num, type_, id);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE create_account(IN account_ VARCHAR(19),
                                IN type_ ENUM ("Checking Account", "Savings Account", "Credit Card Account", "Debit Card Account"))
BEGIN
    INSERT INTO account VALUES (account_, type_);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE create_account_user(IN account_ VARCHAR(19), IN username_ VARCHAR(64))
BEGIN
    INSERT INTO account_user VALUES (account_, username_);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_account_number(IN user_ VARCHAR(64))
BEGIN
    SELECT au.account_number
    FROM user AS u
             JOIN account_user AS au ON u.user_username = au.user_username
    WHERE u.user_username = user_;
END$$
DELIMITER ;