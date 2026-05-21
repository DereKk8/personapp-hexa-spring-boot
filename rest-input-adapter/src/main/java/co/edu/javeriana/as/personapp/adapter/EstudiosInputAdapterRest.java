package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.mapper.EstudiosMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;
import co.edu.javeriana.as.personapp.model.response.EstudiosResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudiosInputAdapterRest {

	@Autowired
	@Qualifier("estudiosOutputAdapterMaria")
	private StudyOutputPort estudiosOutputPortMaria;

	@Autowired
	private EstudiosMapperRest estudiosMapperRest;

	StudyInputPort studyInputPort;

	@PostConstruct
	public void init() {
		studyInputPort = new StudyUseCase(estudiosOutputPortMaria);
	}

	public List<EstudiosResponse> historial() {
		log.info("Into historial EstudiosEntity in Input Adapter");
		return studyInputPort.findAll().stream()
				.map(estudiosMapperRest::fromDomainToAdapterRest)
				.collect(Collectors.toList());
	}

	public EstudiosResponse crearEstudio(EstudiosRequest request) {
		try {
			return estudiosMapperRest
					.fromDomainToAdapterRest(studyInputPort.create(estudiosMapperRest.fromAdapterToDomain(request)));
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public EstudiosResponse obtenerEstudio(Integer idProf, Integer ccPer) {
		try {
			return estudiosMapperRest.fromDomainToAdapterRest(studyInputPort.findOne(idProf, ccPer));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public EstudiosResponse actualizarEstudio(Integer idProf, Integer ccPer, EstudiosRequest request) {
		try {
			return estudiosMapperRest.fromDomainToAdapterRest(
					studyInputPort.edit(idProf, ccPer, estudiosMapperRest.fromAdapterToDomain(request)));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public EstudiosResponse eliminarEstudio(Integer idProf, Integer ccPer) {
		try {
			Boolean result = studyInputPort.drop(idProf, ccPer);
			return new EstudiosResponse(idProf, ccPer, "", "", result ? "Eliminado" : "Error");
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}
}
