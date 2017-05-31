CREATE TABLE users (
  id  int(11) auto_increment,
  username  varchar(20),
  password	varchar(8),
  mail	varchar(30),
  i_questions int,
  c_questions int,
  score int,
  created_at datetime,
  updated_at datetime,
  primary key(id)
)ENGINE=InnoDB;