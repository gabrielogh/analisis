CREATE TABLE category (
  id  int(11) /*DEFAULT NULL*/ auto_increment PRIMARY KEY,
  categ enum('Arte', 'Ciencia', 'Deporte', 'Entretenimiento', 'Geografía', 'Historia')
);