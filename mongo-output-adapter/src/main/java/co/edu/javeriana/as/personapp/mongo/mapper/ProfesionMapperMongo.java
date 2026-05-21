package co.edu.javeriana.as.personapp.mongo.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;

@Mapper
public class ProfesionMapperMongo {

	public ProfesionDocument fromDomainToAdapter(Profession profession) {
		ProfesionDocument profesionDocument = new ProfesionDocument();
		profesionDocument.setId(profession.getIdentification());
		profesionDocument.setNom(profession.getName());
		profesionDocument.setDes(validateDes(profession.getDescription()));
		return profesionDocument;
	}

	private String validateDes(String description) {
		return description != null ? description : "";
	}

	public Profession fromAdapterToDomain(ProfesionDocument profesionDocument) {
		Profession profession = new Profession();
		profession.setIdentification(profesionDocument.getId());
		profession.setName(profesionDocument.getNom());
		profession.setDescription(validateDescription(profesionDocument.getDes()));
		return profession;
	}

	private String validateDescription(String des) {
		return des != null ? des : "";
	}
}
