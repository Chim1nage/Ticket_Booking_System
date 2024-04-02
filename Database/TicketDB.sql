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
    host_id 		INT PRIMARY KEY,
    host_first_name VARCHAR(255) NOT NULL,
    host_last_name 	VARCHAR(255) NOT NULL,
    host_birth_date DATE NOT NULL
);

CREATE TABLE event (
    event_id	INT AUTO_INCREMENT PRIMARY KEY,
    event_name 	VARCHAR(255) NOT NULL,
    event_date 	DATE NOT NULL,
    event_type 	ENUM("Sport", "Concert", "Art & Theater", "Family", "Other") NOT NULL,
    stadium_id	INT NOT NULL,
    host_id		INT NOT NULL,
    FOREIGN KEY (stadium_id) REFERENCES stadium(stadium_id)
		ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY (host_id) REFERENCES host(host_id)
		ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE seat (
    seat_id     	INT AUTO_INCREMENT,
    seat_section	VARCHAR(32),
    seat_row    	INT	NOT NULL,
    seat_number 	INT NOT NULL,
    seat_type   	ENUM("General Admission", "Box Seats", "Club Seats", "Suites", "Accessible Seats", "Standing Areas", "Other") NOT NULL,
    stadium_id  	INT,
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
    user_name 			VARCHAR(255) PRIMARY KEY,
    user_password 		VARCHAR(255) NOT NULL,
    user_email 			VARCHAR(255) UNIQUE NOT NULL,
    user_phone_number 	INT UNIQUE NOT NULL,
    user_birth_year 	YEAR
);

CREATE TABLE account(
    account_number  INT PRIMARY KEY,
    account_type    ENUM("Checking Account", "Savings Account", "Credit Card Account", "Debit Card Account")
);

CREATE TABLE account_user(
    account_number 	INT NOT NULL,
    user_name   	VARCHAR(255) NOT NULL,
    PRIMARY KEY(account_number, user_name),
    FOREIGN KEY (account_number) REFERENCES account(account_number)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (user_name) REFERENCES user(user_name)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ticket(
    ticket_id       INT PRIMARY KEY AUTO_INCREMENT,
    booking_date    DATE NOT NULL,
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
	FOREIGN KEY (seat_id) REFERENCES seat(seat_id)
        on UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY (stadium_id) REFERENCES stadium(stadium_id)
        on UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (event_id) REFERENCES event(event_id)
        on UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY (buyer) REFERENCES user(user_name)
        on UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY (seller) REFERENCES user(user_name)
        on UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY (buyer_account) REFERENCES account(account_number)
        on UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY (seller_account) REFERENCES account(account_number)
        on UPDATE CASCADE ON DELETE RESTRICT
);


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