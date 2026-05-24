package co.edu.javeriana.as.personapp.terminal.adapter;

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
import co.edu.javeriana.as.personapp.terminal.mapper.EstudiosMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudiosModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudiosInputAdapterCli {

	@Autowired
	@Qualifier("estudiosOutputAdapterMaria")
	private StudyOutputPort estudiosOutputPortMaria;

	@Autowired
	private EstudiosMapperCli estudiosMapperCli;

	StudyInputPort studyInputPort;

	@PostConstruct
	public void init() {
		studyInputPort = new StudyUseCase(estudiosOutputPortMaria);
	}

	public void historial() {
		try {
			List<EstudiosModelCli> estudios = studyInputPort.findAll().stream()
					.map(estudiosMapperCli::fromDomainToAdapterCli)
					.collect(Collectors.toList());
			System.out.println("----- Estudios (" + estudios.size() + ") -----");
			if (estudios.isEmpty()) {
				System.out.println("No hay estudios registrados.");
			} else {
				estudios.forEach(System.out::println);
			}
		} catch (Exception e) {
			log.warn("No se pudo obtener el listado: " + e.getMessage());
		}
	}

	public void contar() {
		try {
			System.out.println("Total de estudios: " + studyInputPort.count());
		} catch (Exception e) {
			log.warn("No se pudo contar: " + e.getMessage());
		}
	}

	public void crearEstudio(EstudiosModelCli modelo) {
		try {
			EstudiosModelCli result = estudiosMapperCli
					.fromDomainToAdapterCli(studyInputPort.create(estudiosMapperCli.fromAdapterToDomain(modelo)));
			System.out.println("Estudio creado: " + result);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	public void obtenerEstudio(Integer idProf, Integer ccPer) {
		try {
			EstudiosModelCli result = estudiosMapperCli
					.fromDomainToAdapterCli(studyInputPort.findOne(idProf, ccPer));
			System.out.println("Estudio encontrado: " + result);
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void actualizarEstudio(Integer idProf, Integer ccPer, EstudiosModelCli modelo) {
		try {
			EstudiosModelCli result = estudiosMapperCli.fromDomainToAdapterCli(
					studyInputPort.edit(idProf, ccPer, estudiosMapperCli.fromAdapterToDomain(modelo)));
			System.out.println("Estudio actualizado: " + result);
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void eliminarEstudio(Integer idProf, Integer ccPer) {
		try {
			Boolean result = studyInputPort.drop(idProf, ccPer);
			System.out.println("Estudio eliminado: " + result);
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}
}
