create table games (
  id int(11) auto_increment,
  user_id int(11),
  question_number int,
  in_progress boolean,
  vs_mode boolean,
  current_question_id int,
  current_question_state boolean,
  corrects int,
  incorrects int,
  created_at datetime,
  updated_at datetime,
  primary key (id)
)ENGINE=InnoDB;