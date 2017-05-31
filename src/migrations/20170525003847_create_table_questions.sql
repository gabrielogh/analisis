create table questions (
  id  int(11) auto_increment,
  description  varchar(100),
  a1  varchar(50),
  a2  varchar(50),
  a3  varchar(50),
  a4  varchar(50),
  category_id int,
  correct_a int,
  created_at datetime,
  updated_at datetime,
  primary key (id)
)ENGINE=InnoDB;