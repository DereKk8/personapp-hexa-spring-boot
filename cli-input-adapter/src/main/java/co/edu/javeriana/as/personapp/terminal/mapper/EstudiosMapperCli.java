package co.edu.javeriana.as.personapp.terminal.mapper;

import java.time.LocalDate;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.EstudiosModelCli;

@Mapper
public class EstudiosMapperCli {

	public EstudiosModelCli fromDomainToAdapterCli(Study study) {
		EstudiosModelCli model = new EstudiosModelCli();
		model.setIdProf(study.getProfession() != null ? study.getProfession().getIdentification() : null);
		model.setCcPer(study.getPerson() != null ? study.getPerson().getIdentification() : null);
		model.setFecha(study.getGraduationDate() != null ? study.getGraduationDate().toString() : null);
		model.setUniver(study.getUniversityName());
		return model;
	}

	public Study fromAdapterToDomain(EstudiosModelCli model) {
		Study study = new Study();
		if (model.getIdProf() != null) {
			Profession profession = new Profession();
			profession.setIdentification(model.getIdProf());
			study.setProfession(profession);
		}
		if (model.getCcPer() != null) {
			Person person = new Person();
			person.setIdentification(model.getCcPer());
			study.setPerson(person);
		}
		if (model.getFecha() != null && !model.getFecha().isEmpty()) {
			study.setGraduationDate(LocalDate.parse(model.getFecha()));
		}
		study.setUniversityName(model.getUniver());
		return study;
	}
}
