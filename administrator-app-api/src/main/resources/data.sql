INSERT INTO hospital.user_table (dtype, email, username, password, first_name, last_name, enabled, last_password_reset_date) values ('ADMIN','lukabanjac@gmail.com', 'luka', '$2a$10$b9ThKr7Ge.65AMY1v39mNO.Ah.kehgUt1CLcL0xkN4LPTM7omGaRy', 'Luka', 'Banjac', true, '2020-10-01 21:58:58.508');
INSERT INTO hospital.user_table (dtype, email, username, password, first_name, last_name,  enabled, last_password_reset_date) values ('ADMIN','obicniadmin@gmail.com', 'nikola', '$2a$10$b9ThKr7Ge.65AMY1v39mNO.Ah.kehgUt1CLcL0xkN4LPTM7omGaRy', 'Nikola', 'Nikolic',  true, '2020-10-01 21:58:58.508');


INSERT INTO hospital.authority (name) values('ROLE_SUPERADMIN');
INSERT INTO hospital.authority (name) values('ROLE_ADMIN');

INSERT INTO hospital.permission (name) values('hasCertificate');



INSERT INTO hospital.user_authority (user_id, authority_id) values (1, 1);

INSERT INTO hospital.user_permission (user_id, permission_id) values (1, 1);