/*CREATE TABLE IF NOT EXISTS `enquiry_call_list` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `created_at` datetime DEFAULT NULL,
    `updated_at` datetime DEFAULT NULL,
                             `archive` bit(1) DEFAULT NULL,
                             `call_check` bit(1) DEFAULT NULL,
                             `call_done` bit(1) DEFAULT NULL,
                             `contact_name` varchar(50) DEFAULT NULL,
                             `contact_number` varchar(30) DEFAULT NULL,
                             `email_check` bit(1) DEFAULT NULL,
                             `email_done` bit(1) DEFAULT NULL,
                             `query` varchar(2550) DEFAULT NULL,
                             `record_date` datetime DEFAULT NULL,
                             `ref_number` varchar(25) DEFAULT NULL,
                             `staff_name` varchar(25) NOT NULL,
    `client_id` bigint(11) DEFAULT NULL,
    `is_leads` bit(1) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert into `enquiry_call_list` (id, created_at, updated_at, archive, call_check, call_done, contact_name, contact_number, email_check, email_done, query, record_date, ref_number, client_id, is_leads, staff_name)
SELECT cl.id, created_at, updated_at, archive, call_check, call_done, contact_name, contact_number, email_check, email_done, query, record_date, ref_number, client_id, is_leads, s.staff_name
 FROM call_list cl, staff s where cl.staff_id = s.id;*/
 ALTER TABLE `call_list`
ADD COLUMN `staff_name` varchar(25) NOT NULL;

SET SQL_SAFE_UPDATES=0;
UPDATE call_list cl SET cl.staff_name = (select s.staff_name from staff s where s.id=cl.staff_id);
SET SQL_SAFE_UPDATES=1;

ALTER TABLE `call_list`
DROP FOREIGN KEY `FKb1riwwl23qhqiuckm7dh8to8r`,
DROP FOREIGN KEY `FK1mtsbur82frn64de7balymq9z`;
ALTER TABLE `call_list`
DROP COLUMN `staff_id`,
DROP INDEX `FK1mtsbur82frn64de7balymq9z` ;
;