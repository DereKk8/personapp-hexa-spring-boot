package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;

@Mapper
public class ProfesionMapperRest {

	public ProfesionResponse fromDomainToAdapterRest(Profession profession) {
		return new ProfesionResponse(
				profession.getIdentification().toString(),
				profession.getName(),
				profession.getDescription(),
				"OK");
	}

	public Profession fromAdapterToDomain(ProfesionRequest request) {
		Profession profession = new Profession();
		profession.setIdentification(request.getId() != null ? Integer.valueOf(request.getId()) : null);
		profession.setName(request.getName());
		profession.setDescription(request.getDescription());
		return profession;
	}
}
