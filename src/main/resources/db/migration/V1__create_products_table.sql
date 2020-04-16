CREATE TABLE IF NOT EXISTS products(
    id bigint auto_increment primary key,
    name varchar(30) not null unique ,
    price decimal(7,2) not null ,
    amount int not null
)