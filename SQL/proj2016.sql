
create table users(
	user_name varchar(15) primary key,
    user_password varchar(20) not null,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    creation_date timestamp default current_timestamp,
    user_email varchar(100) not null,
    type_user int
);

create table end_users(
	user_name varchar(15),
    end_user_id int primary key auto_increment,
    isAnonymous tinyint(1),
    foreign key (user_name) references users (user_name)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

create table customer_reps(
	user_name varchar(15),
    cr_id int primary key auto_increment,
    foreign key (user_name) references users (user_name)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

create table administrators(
	user_name varchar(15),
    admin_id int primary key auto_increment,
    foreign key (user_name) references users (user_name)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

create table supervises(
	admin_id int not null,
    cr_id int not null,
    foreign key (admin_id) references administrators (admin_id)
		ON UPDATE CASCADE,
    foreign key (cr_id) references customer_reps (cr_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

create table items(
	item_id int primary key auto_increment,
    item_name varchar(100) not null,
    item_RAM mediumint,
    item_storage int,
    item_plat_version float not null,
    item_company varchar(20) not null,
    camera_q int,
    dim_x float,
    dim_y float,
    dim_display float,
    dim_thickness float,
    description varchar(400),
    weight float,
    image varchar(200),
    battery_time float
);

create table active_auctions(
	auction_id int primary key auto_increment,
    seller_id int not null,
    item_id int not null,
    start_date timestamp default current_timestamp,
    end_date timestamp not null,
    public_increment int check (public_increment>0),
    secret_min_price int check (secret_min_price>0),
    winning_bid_id int,

    foreign key (seller_id) references end_users (end_user_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE,
    foreign key (item_id) references items (item_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

create table auction_history(
	auction_id int primary key auto_increment,
    seller_id int not null,
    item_id int not null,
    start_date timestamp default current_timestamp,
    end_date timestamp not null,
    public_increment int check (public_increment>0),
    secret_min_price int check (secret_min_price>0),
    winning_bid_id int,

    foreign key (seller_id) references end_users (end_user_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE,
    foreign key (item_id) references items (item_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

create table active_bids(
	bid_id int primary key auto_increment,
    auction_id int not null,
    buyer_id int not null,
    bid_date timestamp default current_timestamp,
    bid_amount int not null,
    bid_upper_limit int,
    
    foreign key (buyer_id) references end_users (end_user_id)
		ON UPDATE CASCADE,
    foreign key (auction_id) references active_auctions (auction_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

create table active_bid_input(
	bid_id int primary key auto_increment,
    auction_id int not null,
    buyer_id int not null,
    bid_date timestamp default current_timestamp,
    bid_amount int not null,
    bid_upper_limit int,
    
    foreign key (buyer_id) references end_users (end_user_id)
		ON UPDATE CASCADE,
    foreign key (auction_id) references active_auctions (auction_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

create table bid_history(
	bid_id int primary key auto_increment,
    auction_id int not null,
    buyer_id int not null,
    bid_date timestamp default current_timestamp,
    bid_amount int not null,
    bid_upper_limit int,
    
    foreign key (buyer_id) references end_users (end_user_id)
		ON UPDATE CASCADE,
    foreign key (auction_id) references auction_history (auction_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

create table tickets(
	ticket_id int primary key auto_increment,
    end_user_id int,
    message_header varchar(100),
    message_body varchar(500),
    ticket_date timestamp default current_timestamp,
    is_open tinyint(1),
    answered tinyint(1),
    
    foreign key (end_user_id) references end_users (end_user_id)
		ON UPDATE CASCADE
);

create table emails(
	email_id int primary key auto_increment,
    email_from varchar(15) not null,
    email_to varchar(15) not null,
    email_subject varchar(100),
    email_content varchar(500),  
    email_date timestamp default current_timestamp,
    
    foreign key (email_from) references users (user_name)
		ON UPDATE CASCADE,
    foreign key (email_to) references users (user_name)
		ON UPDATE CASCADE
);

create table wish_lists(
	wish_list_id int primary key auto_increment,
	end_user_id int,
    phone_plat_version float not null,
    phone_company varchar(20) not null,
    
    UNIQUE(end_user_id, phone_plat_version, phone_company),
    foreign key (end_user_id) references end_users (end_user_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

ALTER TABLE active_auctions ADD FOREIGN KEY (winning_bid_id) REFERENCES active_bids (bid_id);

/* CLOSING TICKETS ON DELETE FOR END_USERS */
drop trigger closeticket;

DELIMITER $$
CREATE TRIGGER closeticket BEFORE DELETE ON end_users 
FOR EACH ROW
BEGIN
	UPDATE tickets SET is_open=false where OLD.end_user_id=end_user_id;
END; $$
DELIMITER ;

drop event auction_time_out;
/* TIMING OUT AUCTIONS */
DELIMITER $$
CREATE
event auction_time_out
on SCHEDULE EVERY 30 SECOND STARTS NOW()
DO BEGIN
	DECLARE id int;
    DECLARE finished int default 0;
    DECLARE cur1 cursor for (select auction_id from active_auctions where end_date<=current_timestamp());
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
    OPEN cur1;
    do_loop : LOOP
		FETCH cur1 INTO id;
        If finished=1
			then
				LEAVE do_loop;
		END IF;
		insert into auction_history
			select * from active_auctions where auction_id=id;
		insert into bid_history
			select * from active_bids where auction_id=id;
			
		delete from active_auctions where auction_id=id;   
    END LOOP;
    CLOSE cur1;
END; $$
DELIMITER ;

set GLOBAL event_scheduler=ON;

drop trigger email_auctions;
DELIMITER $$
CREATE TRIGGER email_auctions AFTER DELETE ON active_auctions
FOR EACH ROW
BEGIN
	DECLARE seller varchar(15);
    DECLARE buyer varchar(15);
    
	select s.user_name into seller from auction_history ah, end_users s
				where ah.seller_id=s.end_user_id
                AND ah.auction_id = OLD.auction_id;
                
	select b.user_name into buyer from auction_history ah, end_users b, bid_history bh
				where ah.winning_bid_id=bh.bid_id
                AND bh.buyer_id=b.end_user_id
                AND ah.auction_id = OLD.auction_id;
	
    if(seller is not null)
    then
		insert into emails(email_from, email_to, email_subject, email_content) values
			("yabe", seller, "Auction Finished!", "Your Auction has ended");
	end if;
    
    if(buyer is not null)
    then
		insert into emails(email_from, email_to, email_subject, email_content) values
			("yabe", buyer, "Won Auction!", "You won an Auction!");
	end if;
END; $$
DELIMITER ;
/* NEW THINGS */

/* make sure you add active_bid_input */

insert into users (user_name, user_password, first_name, last_name, user_email, type_user) values
("yabe", "yabe", "Yabe", "Administrator", "admin@yabe.com", 3),
("crep", "crep", "Customer", "Representative", "crep@yabe.com", 2);

insert into administrators(user_name) values("yabe");
insert into customer_reps(user_name) values("crep");
/* UPDATE TO THE EVENT */

/* CALCULATING WINNING BID BASED ON UPPER_LIMIT 
 * Assumes new bid number is already higher than current winning bid
 */
drop trigger winning_bid;
drop trigger fight_between_bids;

DELIMITER $$
CREATE TRIGGER fight_between_bids AFTER INSERT ON active_bid_input
FOR EACH ROW	
BEGIN
	DECLARE increment INT;
	DECLARE current_buyer_id INT;
    DECLARE current_buyer_name varchar(15);
    DECLARE new_buyer_name varchar(15);
    DECLARE current_upper INT;
    DECLARE current_bid INT;
    DECLARE new_upper INT;
	
    SET increment = (select public_increment from active_auctions where auction_id=NEW.auction_id);
    select b.bid_upper_limit, b.buyer_id, b.bid_amount
		INTO current_upper, current_buyer_id, current_bid
		from active_bids b 
		inner join active_auctions a on b.bid_id=a.winning_bid_id 
		where a.auction_id=NEW.auction_id LIMIT 1;

	IF(current_upper is null OR current_bid is null) THEN
		SET current_upper = 0;
        SET current_bid = 0;
        SET current_buyer_id = 0;
    END IF;
    
    SET new_upper = (select NEW.bid_upper_limit);
    
    set current_upper = current_upper - ((current_upper) % increment);
	set new_upper = new_upper - ((new_upper) % increment);

    IF(NEW.bid_amount >= current_bid + increment)
    THEN
		INSERT INTO active_bids(auction_id, buyer_id, bid_amount, bid_upper_limit)
				VALUES(NEW.auction_id, NEW.buyer_id, NEW.bid_amount, NEW.bid_upper_limit);
		/* if current_upper is higher than the next increment, AND if the buyer_id are not the same */
		IF(current_upper >= NEW.bid_amount+increment AND current_buyer_id <> NEW.buyer_id)
		THEN
			IF(current_upper > new_upper)
			THEN
				/* CURRENT BID WINS */
				SELECT user_name INTO new_buyer_name FROM end_users WHERE NEW.buyer_id=end_user_id;
                INSERT INTO active_bids(auction_id, buyer_id, bid_amount, bid_upper_limit)
					VALUES(NEW.auction_id, current_buyer_id, new_upper+increment, current_upper);
				INSERT INTO emails(email_from, email_to, email_subject, email_content) values
					("yabe", new_buyer_name, "Outbid!", CONCAT("You've been outbid on Auction", CAST(NEW.auction_id AS CHAR)));
			ELSEIF(current_upper = new_upper)
			THEN
				/* CURRENT BID WINS */
                SELECT user_name INTO new_buyer_name FROM end_users WHERE NEW.buyer_id=end_user_id;
				INSERT INTO active_bids(auction_id, buyer_id, bid_amount, bid_upper_limit)
					VALUES(NEW.auction_id, current_buyer_id, current_upper, current_upper);
				INSERT INTO emails(email_from, email_to, email_subject, email_content) values
					("yabe", new_buyer_name, "Outbid!", CONCAT("You've been outbid on Auction", CAST(NEW.auction_id AS CHAR)));
                    
			ELSE
				/* NEW BID WINS */
                SELECT user_name INTO current_buyer_name FROM end_users WHERE current_buyer_id=end_user_id;
				INSERT INTO active_bids(auction_id, buyer_id, bid_amount, bid_upper_limit)
					VALUES(NEW.auction_id, NEW.buyer_id, current_upper+increment, new_upper);
				INSERT INTO emails(email_from, email_to, email_subject, email_content) values
					("yabe", current_buyer_name, "Outbid!", CONCAT("You've been outbid on Auction", CAST(NEW.auction_id AS CHAR)));
			END IF;
		END IF;
	ELSE
		/*  NEW BID WINS */
        if(current_buyer_id is not null) then
			SELECT user_name INTO current_buyer_name FROM end_users WHERE current_buyer_id=end_user_id;
			INSERT INTO emails(email_from, email_to, email_subject, email_content) values
				("yabe", current_buyer_name, "Outbid!", CONCAT("You've been outbid on Auction", CAST(NEW.auction_id AS CHAR)));
		end if;
	END IF;
END; $$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER winning_bid AFTER INSERT ON active_bids
FOR EACH ROW
BEGIN
	UPDATE active_auctions SET winning_bid_id=NEW.bid_id where auction_id=NEW.auction_id;
END; $$
DELIMITER ;

/* Search for Auctions */
select * from active_auctions a 
inner join end_users e on a.seller_id=e.end_user_id
inner join users u on e.user_name=u.user_name
inner join items i on a.item_id=i.item_id;

/* Search for Bids */
select * from active_bids b
inner join end_users e on b.buyer_id=e.end_user_id
inner join users u on e.user_name=u.user_name
where b.auction_id=1;
    
/* UPDATED FIGHT TRIGGER */    
    