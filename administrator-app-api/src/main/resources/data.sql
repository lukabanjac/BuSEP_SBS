INSERT INTO hospital.user_table (dtype, email, username, password, first_name, last_name, enabled, last_password_reset_date) values ('ADMIN','lukabanjac@gmail.com', 'luka', '$2a$10$b9ThKr7Ge.65AMY1v39mNO.Ah.kehgUt1CLcL0xkN4LPTM7omGaRy', 'Luka', 'Banjac', true, '2020-10-01 21:58:58.508');
INSERT INTO hospital.user_table (dtype, email, username, password, first_name, last_name,  enabled, last_password_reset_date) values ('ADMIN','obicniadmin@gmail.com', 'nikola', '$2a$10$b9ThKr7Ge.65AMY1v39mNO.Ah.kehgUt1CLcL0xkN4LPTM7omGaRy', 'Nikola', 'Nikolic',  true, '2020-10-01 21:58:58.508');
INSERT INTO hospital.user_table (dtype, email, username, password, first_name, last_name,  enabled, last_password_reset_date) values ('DOCTOR','doctor@gmail.com', 'milan', '$2a$10$daGUpNNSswb3J6CBUcCEnuUxwc2tWvAd760ho1ByFkyUUozFMXKYO', 'Milan', 'Milanovic',  true, '2020-10-01 21:58:58.508');

INSERT INTO hospital.authority (name) values('ROLE_SUPERADMIN');
INSERT INTO hospital.authority (name) values('ROLE_ADMIN');
INSERT INTO hospital.authority (name) values('ROLE_DOCTOR');

INSERT INTO hospital.permission (name) values('hasCertificate');




INSERT INTO hospital.user_authority (user_id, authority_id) values (1, 1);
INSERT INTO hospital.user_authority (user_id, authority_id) values (2, 2);
INSERT INTO hospital.user_authority (user_id, authority_id) values (3, 3);

INSERT INTO hospital.user_permission (user_id, permission_id) values (1, 1);



-- secretWords: tajniKljuc1, tajniKljuc2, tajniKljuc3..
INSERT INTO hospital.trusted_organization (country, city, organization, organization_unit, secret_word_1, secret_word_2, secret_word_3, admin_id) values ('Srb', 'Beograd', 'Vlada RS', 'Ministarstvo Zdravlja', '$2a$10$aQuKX9jc3HcYfzq1heWi9euEyjYejyDXF7Gw/ut8.VIn6ByJiG1WO', '$2a$10$ED6Kdeb4vmRhCRvDzleXy.zq5z5t28lW92wJNGAC64Z0vqNx0C.um', '$2a$10$v4C1P1pwFMbSAiJktQ7oWecI3k.AfWlwT/ZTQWhYn4YdN26Id3XmK', 1);

-- secretWords: key1, key2, key3
INSERT INTO hospital.trusted_organization (country, city, organization, organization_unit, secret_word_1, secret_word_2, secret_word_3, admin_id) values ('Srb', 'Beograd', 'Ministarstvo Zdravlja', 'Klinicki Centar Srbije', '$2a$10$9fziu7eB/O/rj0Z/FbYXyeg8nGMY541mCBYq8cknlyzGDBzpaAp4K
', '$2a$10$yrVyRG/OUZMg7DhORJLHMuUEwXFHmdeIjhGMQu1PnRFxWiLq1cu7C
', '$2a$10$K6MKgMUCTjOJsbdc42LiZuImkuvqWjAX0uok5JGmr9WxFIg8aES5S', 2);



INSERT INTO hospital.certificate (serial_number, ca_serial_number, is_ca, type, cert_file_path, key_store_file_path, trust_store_file_path, revoked, revoked_at, revoke_reason, issued_at, expiring_at, user_id) values
 ('1631229639199', '1631229639199', true, 0, 'src\main\resources\storage\ROOT\cert_1631229639199.pem', 'src\main\resources\storage\ROOT\keyStore_1631229639199.p12', 'src\main\resources\storage\ROOT\trustStore_1631229639199.p12', false, null, null, '2021-09-09 23:20:39','2022-09-09 23:20:39', 1);


INSERT INTO hospital.certificate_request (city, country, issuer_serial_number,organization, organization_unit,rejected_reason,status, user_id)
 values ('Beograd','Srb','1631229639199','Vlada RS','Ministarstvo Zdravlja',null,1,2);


INSERT INTO hospital.device (id, name, certificate_id, trusted_organization_id) values (1, 'rengen', 1, 2);
