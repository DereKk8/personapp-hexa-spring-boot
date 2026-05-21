package co.edu.javeriana.as.personapp.terminal.adapter;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

	@Autowired
	@Qualifier("profesionOutputAdapterMaria")
	private ProfessionOutputPort profesionOutputPortMaria;

	@Autowired
	private ProfesionMapperCli profesionMapperCli;

	ProfessionInputPort professionInputPort;

	@PostConstruct
	public void init() {
		professionInputPort = new ProfessionUseCase(profesionOutputPortMaria);
	}

	public void historial() {
		log.info("Into historial ProfesionEntity in Input Adapter");
		professionInputPort.findAll().stream()
				.map(profesionMapperCli::fromDomainToAdapterCli)
				.forEach(System.out::println);
	}

	public void crearProfesion(ProfesionModelCli modelo) {
		try {
			ProfesionModelCli result = profesionMapperCli
					.fromDomainToAdapterCli(professionInputPort.create(profesionMapperCli.fromAdapterToDomain(modelo)));
			System.out.println("Profesion creada: " + result);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	public void obtenerProfesion(Integer id) {
		try {
			ProfesionModelCli result = profesionMapperCli
					.fromDomainToAdapterCli(professionInputPort.findOne(id));
			System.out.println("Profesion encontrada: " + result);
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void actualizarProfesion(Integer id, ProfesionModelCli modelo) {
		try {
			ProfesionModelCli result = profesionMapperCli.fromDomainToAdapterCli(
					professionInputPort.edit(id, profesionMapperCli.fromAdapterToDomain(modelo)));
			System.out.println("Profesion actualizada: " + result);
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void eliminarProfesion(Integer id) {
		try {
			Boolean result = professionInputPort.drop(id);
			System.out.println("Profesion eliminada: " + result);
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}
}
