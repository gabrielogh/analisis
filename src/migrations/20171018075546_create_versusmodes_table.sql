create table versusmodes (
  id int(11) auto_increment,
  game_p1_id int(11),
  game_p2_id int(11),
  p1_id int,
  p2_id int,
  turn int,
  question_number int,
  in_progress boolean,
  created_at datetime,
  updated_at datetime,
  primary key (id)
)ENGINE=InnoDB;