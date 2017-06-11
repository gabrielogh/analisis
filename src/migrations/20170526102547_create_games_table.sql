create table games (
  id int(11) auto_increment,
  user_id int(11),
  question_number int,
  in_progress boolean,
  corrects int,
  incorrects int,
  created_at datetime,
  updated_at datetime,
  primary key (id)
)ENGINE=InnoDB;