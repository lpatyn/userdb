DROP TABLE IF EXISTS "user";
CREATE TABLE "user" (
   `id` INT AUTO_INCREMENT,
   `name` VARCHAR(50) NOT NULL,
   `last_name` VARCHAR(50) NOT NULL,
   `email` VARCHAR(70) NOT NULL,
   `birthdate` DATE NOT NULL,
   `password` VARCHAR(255),
   `creation_date` TIMESTAMP NOT NULL,
   `modification_date` TIMESTAMP NOT NULL,
   PRIMARY KEY (`id`)
);