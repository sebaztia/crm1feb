ALTER TABLE `client`
ADD COLUMN `deceased` TINYINT NULL AFTER `cleared`;

ALTER TABLE `call_list`
ADD COLUMN `client_id` bigint NULL AFTER `ref_number`;