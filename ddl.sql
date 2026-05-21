CREATE SCHEMA IF NOT EXISTS `persona_db`;
USE `persona_db`;

CREATE TABLE IF NOT EXISTS `persona_db`.`profesion` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nom` VARCHAR(90) NOT NULL,
  `des` TEXT NULL DEFAULT NULL,
  CONSTRAINT `profesion_pk` PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `persona_db`.`persona` (
  `cc` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido` VARCHAR(45) NOT NULL,
  `genero` CHAR(1) NULL DEFAULT NULL,
  `edad` INT NULL DEFAULT NULL,
  CONSTRAINT `persona_pk` PRIMARY KEY (`cc`)
);

CREATE TABLE IF NOT EXISTS `persona_db`.`estudios` (
  `id_prof` INT NOT NULL,
  `cc_per` INT NOT NULL,
  `fecha` DATE NULL DEFAULT NULL,
  `univer` VARCHAR(50) NULL DEFAULT NULL,
  CONSTRAINT `estudios_pk` PRIMARY KEY (`id_prof`, `cc_per`),
  CONSTRAINT `estudio_persona_fk` FOREIGN KEY (`cc_per`) REFERENCES `persona_db`.`persona` (`cc`) ON DELETE RESTRICT,
  CONSTRAINT `estudio_profesion_fk` FOREIGN KEY (`id_prof`) REFERENCES `persona_db`.`profesion` (`id`) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS `persona_db`.`telefono` (
  `num` VARCHAR(15) NOT NULL,
  `oper` VARCHAR(45) NULL DEFAULT NULL,
  `duenio` INT NOT NULL,
  CONSTRAINT `telefono_pk` PRIMARY KEY (`num`),
  CONSTRAINT `telefono_persona_fk` FOREIGN KEY (`duenio`) REFERENCES `persona_db`.`persona` (`cc`) ON DELETE RESTRICT
);
