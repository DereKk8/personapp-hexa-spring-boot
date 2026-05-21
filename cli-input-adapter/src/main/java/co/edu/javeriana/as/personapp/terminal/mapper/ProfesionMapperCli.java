package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;

@Mapper
public class ProfesionMapperCli {

	public ProfesionModelCli fromDomainToAdapterCli(Profession profession) {
		ProfesionModelCli model = new ProfesionModelCli();
		model.setId(profession.getIdentification());
		model.setNom(profession.getName());
		model.setDes(profession.getDescription());
		return model;
	}

	public Profession fromAdapterToDomain(ProfesionModelCli model) {
		Profession profession = new Profession();
		profession.setIdentification(model.getId());
		profession.setName(model.getNom());
		profession.setDescription(model.getDes());
		return profession;
	}
}
