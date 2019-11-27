INSERT INTO clients(birthday, created_at, email, first_last_name, name, phone, second_last_name) VALUES ('1995-05-12', '2019-11-26', '1laura20@hotmail.com', 'Campos', 'Laura', '0000000000', 'Bautista');

INSERT INTO users (user_name, password, enabled, email) VALUES ('andres','$2a$10$C3Uln5uqnzx/GswADURJGOIdBqYrly9731fnwKDaUdBkt/M3qvtLq',1,'profesor@bolsadeideas.com');
INSERT INTO users (user_name, password, enabled, email) VALUES ('admin','$2a$10$RmdEsvEfhI7Rcm9f/uZXPebZVCcPC7ZXZwV51efAvMAp1rIaRAfPK',1,'jhon.doe@bolsadeideas.com');

INSERT INTO roles  (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO user_role (id_user, id_role) VALUES (1, 1);
INSERT INTO user_role (id_user, id_role) VALUES (2, 2);
INSERT INTO user_role (id_user, id_role) VALUES (2, 1);