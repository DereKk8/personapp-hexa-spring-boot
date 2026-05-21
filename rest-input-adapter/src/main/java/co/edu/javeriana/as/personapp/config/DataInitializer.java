package co.edu.javeriana.as.personapp.config;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.PersonaRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private ProfesionRepositoryMaria profesionRepository;

    @Autowired
    private PersonaRepositoryMaria personaRepository;

    @Autowired
    private EstudiosRepositoryMaria estudiosRepository;

    @Autowired
    private TelefonoRepositoryMaria telefonoRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (profesionRepository.count() == 0) {
            log.info("Insertando datos semilla de profesiones...");
            ProfesionEntity p1 = new ProfesionEntity(1, "Ingeniero de Sistemas");
            p1.setDes("Diseno y desarrollo de software");
            profesionRepository.save(p1);
            ProfesionEntity p2 = new ProfesionEntity(2, "Medico General");
            p2.setDes("Atencion primaria en salud");
            profesionRepository.save(p2);
            ProfesionEntity p3 = new ProfesionEntity(3, "Abogado");
            p3.setDes("Asesoria juridica y litigios");
            profesionRepository.save(p3);
            ProfesionEntity p4 = new ProfesionEntity(4, "Arquitecto");
            p4.setDes("Diseno de espacios y construcciones");
            profesionRepository.save(p4);
            ProfesionEntity p5 = new ProfesionEntity(5, "Contador Publico");
            p5.setDes("Gestion contable y tributaria");
            profesionRepository.save(p5);
        }

        if (personaRepository.count() == 0) {
            log.info("Insertando datos semilla de personas...");
            PersonaEntity pe1 = new PersonaEntity(1001, "Ana", "Lopez", 'F');
            pe1.setEdad(28);
            personaRepository.save(pe1);
            PersonaEntity pe2 = new PersonaEntity(1002, "Luis", "Gomez", 'M');
            pe2.setEdad(35);
            personaRepository.save(pe2);
            PersonaEntity pe3 = new PersonaEntity(1003, "Maria", "Perez", 'F');
            pe3.setEdad(24);
            personaRepository.save(pe3);
            PersonaEntity pe4 = new PersonaEntity(1004, "Carlos", "Rodriguez", 'M');
            pe4.setEdad(42);
            personaRepository.save(pe4);
            PersonaEntity pe5 = new PersonaEntity(1005, "Sofia", "Martinez", 'F');
            pe5.setEdad(31);
            personaRepository.save(pe5);
        }

        if (estudiosRepository.count() == 0) {
            log.info("Insertando datos semilla de estudios...");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                estudiosRepository.save(crearEstudio(1, 1001, sdf.parse("2018-06-15"), "Universidad Nacional"));
                estudiosRepository.save(crearEstudio(2, 1002, sdf.parse("2015-12-01"), "Universidad de los Andes"));
                estudiosRepository.save(crearEstudio(3, 1003, sdf.parse("2020-07-20"), "Universidad Externado"));
                estudiosRepository.save(crearEstudio(4, 1004, sdf.parse("2010-05-10"), "Universidad Javeriana"));
                estudiosRepository.save(crearEstudio(5, 1005, sdf.parse("2017-11-25"), "Universidad del Rosario"));
            } catch (Exception e) {
                log.warn("Error al insertar estudios: " + e.getMessage());
            }
        }

        if (telefonoRepository.count() == 0) {
            log.info("Insertando datos semilla de telefonos...");
            telefonoRepository.save(crearTelefono("3001112233", "Claro", 1001));
            telefonoRepository.save(crearTelefono("3014445566", "Movistar", 1002));
            telefonoRepository.save(crearTelefono("3027778899", "Tigo", 1003));
            telefonoRepository.save(crearTelefono("3030001122", "WOM", 1004));
            telefonoRepository.save(crearTelefono("3043334455", "ETB", 1005));
        }

        log.info("Verificacion de datos semilla completada.");
    }

    private EstudiosEntity crearEstudio(int idProf, int ccPer, Date fecha, String univer) {
        EstudiosEntityPK pk = new EstudiosEntityPK(idProf, ccPer);
        EstudiosEntity estudio = new EstudiosEntity(pk);
        estudio.setFecha(fecha);
        estudio.setUniver(univer);
        ProfesionEntity profesion = new ProfesionEntity(idProf);
        PersonaEntity persona = new PersonaEntity(ccPer);
        estudio.setProfesion(profesion);
        estudio.setPersona(persona);
        return estudio;
    }

    private TelefonoEntity crearTelefono(String num, String oper, int duenioCc) {
        TelefonoEntity telefono = new TelefonoEntity(num, oper);
        PersonaEntity duenio = new PersonaEntity(duenioCc);
        telefono.setDuenio(duenio);
        return telefono;
    }
}
