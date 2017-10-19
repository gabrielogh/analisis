create table versusmodes (
  id int(11) auto_increment,
  game_p1_id int(11),
  game_p2_id int(11),
  turn int,
  in_progress boolean,
  created_at datetime,
  updated_at datetime,
  primary key (id)
)ENGINE=InnoDB;