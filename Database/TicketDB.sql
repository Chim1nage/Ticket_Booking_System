DROP DATABASE TicketDB;
CREATE DATABASE IF NOT EXISTS TicketDB;
USE TicketDB;

CREATE TABLE stadium (
    stadium_id 		INT AUTO_INCREMENT PRIMARY KEY,
    stadium_name 	VARCHAR(255) NOT NULL,
    capacity 		INT,
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
                       event_name    VARCHAR(255) NOT NULL,
                       event_date    DATE         NOT NULL,
                       event_type    ENUM("Sport", "Concert", "Art & Theater", "Family", "Other") NOT NULL,
                       stadium_id    INT          NOT NULL,
                       host_username VARCHAR(64)  NOT NULL,
    FOREIGN KEY (stadium_id) REFERENCES stadium(stadium_id)
		ON UPDATE CASCADE ON DELETE RESTRICT,
                       FOREIGN KEY (host_username) REFERENCES host (host_username)
		ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE seat (
                      seat_id           INT AUTO_INCREMENT,
                      seat_section      VARCHAR(32),
                      seat_row          INT NOT NULL,
                      seat_number       INT NOT NULL,
                      seat_type         ENUM("General Admission", "Box Seats", "Club Seats", "Suites", "Accessible Seats", "Standing Areas", "Other") NOT NULL,
                      stadium_id        INT,
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
                      user_birth_year YEAR NOT NULL
);

CREATE TABLE account(
    account_number  INT PRIMARY KEY,
    account_type    ENUM("Checking Account", "Savings Account", "Credit Card Account", "Debit Card Account")
);

CREATE TABLE account_user(
    account_number 	INT NOT NULL,
    user_username VARCHAR(255) NOT NULL,
    PRIMARY KEY (account_number, user_username),
    FOREIGN KEY (account_number) REFERENCES account(account_number)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (user_username) REFERENCES user (user_username)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ticket(
    ticket_id       INT PRIMARY KEY AUTO_INCREMENT,
    booking_date DATE,
    price           INT NOT NULL,
    first_name      VARCHAR(255) DEFAULT "",
    last_name       VARCHAR(255) DEFAULT "",
    seat_id         INT NOT NULL,
    stadium_id		INT NOT NULL,
    event_id        INT NOT NULL,
    buyer			VARCHAR(255),
    seller			VARCHAR(255),
    buyer_account	INT,
    seller_account	INT,
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

-- TRIGGERS
DELIMITER $$
CREATE TRIGGER check_buyer_and_seller_are_not_same_on_insert
	BEFORE INSERT ON ticket
    FOR EACH ROW
    BEGIN
		IF (NEW.buyer IS NOT NULL AND NEW.seller IS NOT NULL) AND (NEW.buyer = NEW.seller) THEN
			SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = "Buyer and Seller cannot be the same user";
		END IF;
	END$$
DELIMITER ;
		
        
-- PROCEDURES
DELIMITER
$$
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

DELIMITER
$$
CREATE PROCEDURE get_ticket_from_seat_stadium_event(IN seat_id_ INT, IN stadium_id_ INT, IN event_id_ INT)
BEGIN
SELECT ticket_id
FROM ticket
WHERE seat_id = seat_id_
  AND stadium_id = stadium_id_
  AND event_id = event_id_;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE update_ticket(IN booking_date_ DATE, IN first_name_ VARCHAR (255), IN last_name_ VARCHAR (255),
                               IN buyer_ VARCHAR (255), IN ticket_id_ INT)
BEGIN
UPDATE ticket
SET booking_date = booking_date_,
    first_name   = first_name_,
    last_name    = last_name_,
    buyer        = buyer_
WHERE ticket_id = ticket_id_;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE reset_ticket_trading_information(IN ticket INT)
BEGIN
UPDATE TICKET
SET booking_date   = null,
    buyer          = null,
    seller         = null,
    buyer_account  = null,
    seller_account = null
WHERE ticket_id = ticket;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE create_event(IN name_ VARCHAR (255), IN date_ DATE, IN event_ ENUM("Sport", "Concert", "Art & Theater",
                              "Family", "Other"), IN stadium_ INT, IN host_ VARCHAR (64))
BEGIN
INSERT INTO event(event_name, event_date, event_type, stadium_id, host_username)
VALUES (name_, date_, event_, stadium_, host_);
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE get_all_event()
BEGIN
select *
from event;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE get_all_event_with_stadium_name()
BEGIN
SELECT e.*, s.stadium_name
FROM event AS e
         JOIN stadium AS s ON s.stadium_id = e.stadium_id;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE get_event_id(IN name_ VARCHAR (255), IN date_ DATE, IN event_ ENUM("Sport", "Concert", "Art & Theater",
                              "Family", "Other"), IN stadium_ INT, IN host_ VARCHAR (64))
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

DELIMITER
$$
CREATE PROCEDURE delete_event(IN event_id_ INT)
BEGIN
DELETE
FROM event
WHERE event_id = event_id_;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE get_all_stadium()
BEGIN
select *
from stadium;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE get_all_seat_id_from_stadium(IN stadium_ INT)
BEGIN
SELECT seat_id
FROM seat
WHERE stadium_id = stadium_;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE get_all_empty_seat_from_stadium_for_event(IN event_id_ INT)
BEGIN
SELECT s.*
FROM seat s
         JOIN stadium AS st ON s.stadium_id = st.stadium_id
         JOIN event AS e ON e.stadium_id = st.stadium_id
         JOIN ticket AS t ON t.event_id = e.event_id
WHERE e.event_id = event_id_
  AND t.buyer = null;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE get_seat_from_row_number_stadium(IN row_ INT, IN num_ INT, IN stadium_id_ INT)
BEGIN
SELECT s.seat_id
FROM seat AS s
         JOIN stadium AS st on s.stadium_id = st.stadium_id
WHERE s.seat_row = row_
  AND s.seat_number = num_;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE create_ticket(IN price_ INT, IN seat_id_ INT, IN stadium_id_ INT, IN event_id_ INT)
BEGIN
INSERT INTO ticket(price, seat_id, stadium_id, event_id)
VALUES (price_, seat_id_, stadium_id_, event_id_);
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE get_ticket(IN ticket_id_ INT)
BEGIN
SELECT *
FROM ticket
WHERE ticket_id = ticket_id_;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE update_ticket_name(IN first VARCHAR (255), IN last VARCHAR (255), IN ticket INT)
BEGIN
UPDATE ticket
SET first_name = first, last_name = last
WHERE ticket_id = ticket;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE delete_all_ticket_related_to_event(IN event_id_ INT)
BEGIN
DELETE
FROM ticket
WHERE event_id = event_id_;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE get_field_from_table(IN field_name VARCHAR (255), IN table_name VARCHAR (255))
BEGIN
SELECT field_name
FROM table_name;
END$$
DELIMITER ;

DELIMITER
$$
CREATE PROCEDURE get_all_host()
BEGIN
SELECT *
FROM host;
END$$
DELIMITER ;