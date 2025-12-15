drop database if exists bilabonnementdb;
create database if not exists bilabonnementdb;

use bilabonnementdb;

create table users (
        user_id int auto_increment primary key,
        username varchar(50) not null,
user_password varchar(50) not null,
user_role varchar(50) not null
        );

create table car (
        car_id int auto_increment primary key,
        vin_number varchar(255),
car_brand varchar(255),
car_model varchar(255),
current_km int,
trim_level int,
car_status varchar(255),
car_rent_price int,
car_total_price int,
reg_tax int,
co2_emission int,
active_status boolean default false
        );

create table customer (
        customer_id int auto_increment primary key,
        full_name varchar(255),
email varchar(255) unique,
phone_number varchar(255),
address varchar(255)
);

create table rentalContracts (
        contract_id int auto_increment primary key,
        start_date date,
        end_date date,
        current_km int,
        included_km int,
        car_rent_price int,
        pickup_location varchar(255),
dropoff_location varchar(255),

car_id int,
user_id int,
customer_id int,

foreign key (car_id) references car(car_id),
foreign key (customer_id) references customer(customer_id),
foreign key (user_id) references users(user_id)
        );

create table damageReport (
        damage_id int auto_increment primary key,
        text_description varchar(500),
damage_price int,

car_id int,
customer_id int,

foreign key (car_id) references car(car_id),
foreign key (customer_id) references customer(customer_id)
        );

insert into customer(customer_id, full_name, email, phone_number, address)
values
        (1, 'Jaron Kahalani', 'jk@ek.dk', 12345678, 'Torbenvej 29c'),
(2, 'Cay Larsen', 'cahl@ek.dk', 11223344, 'Fredensborgvej 11'),
        (3, 'Aniko Zsigri', 'aniz@ek.dk', 55667788, 'Søvtorvsvej 9');




insert into users (username, user_password, user_role)
values
        ('Jihad', 'Jihad123', 'Dataregistrator'),
('Raya', 'Raya123', 'Skadebehandler'),
        ('Julius', 'Julius123', 'Forretningsudvikler');

insert into car (vin_number, car_brand, car_model, current_km, trim_level,
                 car_status, car_rent_price, car_total_price, reg_tax, co2_emission, active_status)
values
        ('VIN001', 'Toyota', 'Corolla', 50000, 1, 'OK', 2500, 200000, 15000, 90, FALSE),
('VIN002', 'Tesla', 'Model 3', 30000, 2, 'OK', 4200, 350000, 20000, 0, FALSE),
        ('VIN003', 'BMW', '320i', 70000, 3, 'OK', 3100, 280000, 18000, 140, FALSE);

insert into rentalContracts (start_date, end_date, current_km, included_km, car_rent_price, pickup_location, dropoff_location, car_id, user_id, customer_id
                             )
values

        ('2025-01-01', '2025-06-01', 50000, 15000, 2500, 'Kapelvej 1', 'Kapelvej 1', 1, 1, 1),

('2025-02-15', '2025-08-15', 30000, 20000, 4200, 'Ordrupvej 88', 'Ordrupvej 88', 2, 2, 2);