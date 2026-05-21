package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterRest {

	@Autowired
	@Qualifier("profesionOutputAdapterMaria")
	private ProfessionOutputPort profesionOutputPortMaria;

	@Autowired
	private ProfesionMapperRest profesionMapperRest;

	ProfessionInputPort professionInputPort;

	@PostConstruct
	public void init() {
		professionInputPort = new ProfessionUseCase(profesionOutputPortMaria);
	}

	public List<ProfesionResponse> historial() {
		log.info("Into historial ProfesionEntity in Input Adapter");
		return professionInputPort.findAll().stream()
				.map(profesionMapperRest::fromDomainToAdapterRest)
				.collect(Collectors.toList());
	}

	public ProfesionResponse crearProfesion(ProfesionRequest request) {
		try {
			return profesionMapperRest
					.fromDomainToAdapterRest(professionInputPort.create(profesionMapperRest.fromAdapterToDomain(request)));
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public ProfesionResponse obtenerProfesion(Integer id) {
		try {
			return profesionMapperRest.fromDomainToAdapterRest(professionInputPort.findOne(id));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public ProfesionResponse actualizarProfesion(Integer id, ProfesionRequest request) {
		try {
			return profesionMapperRest.fromDomainToAdapterRest(
					professionInputPort.edit(id, profesionMapperRest.fromAdapterToDomain(request)));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public ProfesionResponse eliminarProfesion(Integer id) {
		try {
			Boolean result = professionInputPort.drop(id);
			return new ProfesionResponse(id.toString(), "", "", result ? "Eliminado" : "Error");
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}
}
