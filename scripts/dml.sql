USE `persona_db`;

INSERT INTO `persona_db`.`profesion` (`id`, `nom`, `des`) VALUES
(1, 'Ingeniero de Sistemas', 'Diseno y desarrollo de software'),
(2, 'Medico General', 'Atencion primaria en salud'),
(3, 'Abogado', 'Asesoria juridica y litigios'),
(4, 'Arquitecto', 'Diseno de espacios y construcciones'),
(5, 'Contador Publico', 'Gestion contable y tributaria');

INSERT INTO `persona_db`.`persona` (`cc`, `nombre`, `apellido`, `genero`, `edad`) VALUES
(1001, 'Ana', 'Lopez', 'F', 28),
(1002, 'Luis', 'Gomez', 'M', 35),
(1003, 'Maria', 'Perez', 'F', 24),
(1004, 'Carlos', 'Rodriguez', 'M', 42),
(1005, 'Sofia', 'Martinez', 'F', 31);

INSERT INTO `persona_db`.`estudios` (`id_prof`, `cc_per`, `fecha`, `univer`) VALUES
(1, 1001, '2018-06-15', 'Universidad Nacional'),
(2, 1002, '2015-12-01', 'Universidad de los Andes'),
(3, 1003, '2020-07-20', 'Universidad Externado'),
(4, 1004, '2010-05-10', 'Universidad Javeriana'),
(5, 1005, '2017-11-25', 'Universidad del Rosario');

INSERT INTO `persona_db`.`telefono` (`num`, `oper`, `duenio`) VALUES
('3001112233', 'Claro', 1001),
('3014445566', 'Movistar', 1002),
('3027778899', 'Tigo', 1003),
('3030001122', 'WOM', 1004),
('3043334455', 'ETB', 1005);
