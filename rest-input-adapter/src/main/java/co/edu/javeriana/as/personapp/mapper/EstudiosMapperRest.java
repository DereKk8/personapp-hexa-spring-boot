package co.edu.javeriana.as.personapp.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;
import co.edu.javeriana.as.personapp.model.response.EstudiosResponse;

@Mapper
public class EstudiosMapperRest {

	public EstudiosResponse fromDomainToAdapterRest(Study study) {
		return new EstudiosResponse(
				study.getProfession() != null ? study.getProfession().getIdentification() : null,
				study.getPerson() != null ? study.getPerson().getIdentification() : null,
				study.getGraduationDate() != null ? study.getGraduationDate().toString() : null,
				study.getUniversityName(),
				"OK");
	}

	public Study fromAdapterToDomain(EstudiosRequest request) {
		Study study = new Study();
		if (request.getIdProf() != null) {
			Profession profession = new Profession();
			profession.setIdentification(request.getIdProf());
			study.setProfession(profession);
		}
		if (request.getCcPer() != null) {
			Person person = new Person();
			person.setIdentification(request.getCcPer());
			study.setPerson(person);
		}
		if (request.getGraduationDate() != null && !request.getGraduationDate().isEmpty()) {
			study.setGraduationDate(LocalDate.parse(request.getGraduationDate(), DateTimeFormatter.ISO_DATE));
		}
		study.setUniversityName(request.getUniversityName());
		return study;
	}
}
