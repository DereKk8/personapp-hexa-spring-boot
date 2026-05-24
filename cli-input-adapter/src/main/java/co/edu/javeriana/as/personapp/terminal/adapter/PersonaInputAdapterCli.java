package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	private boolean sinMotor() {
		if (personInputPort == null) {
			log.warn("Seleccione primero un motor de persistencia (MariaDB/MongoDB).");
			return true;
		}
		return false;
	}

	public void historial() {
		if (sinMotor()) return;
		try {
			List<PersonaModelCli> personas = personInputPort.findAll().stream()
					.map(personaMapperCli::fromDomainToAdapterCli)
					.collect(Collectors.toList());
			System.out.println("----- Personas (" + personas.size() + ") -----");
			if (personas.isEmpty()) {
				System.out.println("No hay personas registradas.");
			} else {
				personas.forEach(System.out::println);
			}
		} catch (Exception e) {
			log.warn("No se pudo obtener el listado: " + e.getMessage());
		}
	}

	public void contar() {
		if (sinMotor()) return;
		try {
			System.out.println("Total de personas: " + personInputPort.count());
		} catch (Exception e) {
			log.warn("No se pudo contar: " + e.getMessage());
		}
	}

	public void crearPersona(PersonaModelCli modelo) {
		if (sinMotor()) return;
		try {
			PersonaModelCli result = personaMapperCli.fromDomainToAdapterCli(
					personInputPort.create(personaMapperCli.fromAdapterToDomain(modelo)));
			System.out.println("Persona creada: " + result);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	public void obtenerPersona(Integer cc) {
		if (sinMotor()) return;
		try {
			PersonaModelCli result = personaMapperCli
					.fromDomainToAdapterCli(personInputPort.findOne(cc));
			System.out.println("Persona encontrada: " + result);
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void actualizarPersona(Integer cc, PersonaModelCli modelo) {
		if (sinMotor()) return;
		try {
			PersonaModelCli result = personaMapperCli.fromDomainToAdapterCli(
					personInputPort.edit(cc, personaMapperCli.fromAdapterToDomain(modelo)));
			System.out.println("Persona actualizada: " + result);
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void eliminarPersona(Integer cc) {
		if (sinMotor()) return;
		try {
			Boolean result = personInputPort.drop(cc);
			System.out.println("Persona eliminada: " + result);
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}
}
