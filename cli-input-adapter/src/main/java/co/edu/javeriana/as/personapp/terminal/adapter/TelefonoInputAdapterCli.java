package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.terminal.mapper.TelefonoMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterCli {

	@Autowired
	@Qualifier("telefonoOutputAdapterMaria")
	private PhoneOutputPort telefonoOutputPortMaria;

	@Autowired
	private TelefonoMapperCli telefonoMapperCli;

	PhoneInputPort phoneInputPort;

	@PostConstruct
	public void init() {
		phoneInputPort = new PhoneUseCase(telefonoOutputPortMaria);
	}

	public void historial() {
		try {
			List<TelefonoModelCli> telefonos = phoneInputPort.findAll().stream()
					.map(telefonoMapperCli::fromDomainToAdapterCli)
					.collect(Collectors.toList());
			System.out.println("----- Telefonos (" + telefonos.size() + ") -----");
			if (telefonos.isEmpty()) {
				System.out.println("No hay telefonos registrados.");
			} else {
				telefonos.forEach(System.out::println);
			}
		} catch (Exception e) {
			log.warn("No se pudo obtener el listado: " + e.getMessage());
		}
	}

	public void contar() {
		try {
			System.out.println("Total de telefonos: " + phoneInputPort.count());
		} catch (Exception e) {
			log.warn("No se pudo contar: " + e.getMessage());
		}
	}

	public void crearTelefono(TelefonoModelCli modelo) {
		try {
			TelefonoModelCli result = telefonoMapperCli
					.fromDomainToAdapterCli(phoneInputPort.create(telefonoMapperCli.fromAdapterToDomain(modelo)));
			System.out.println("Telefono creado: " + result);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	public void obtenerTelefono(String number) {
		try {
			TelefonoModelCli result = telefonoMapperCli
					.fromDomainToAdapterCli(phoneInputPort.findOne(number));
			System.out.println("Telefono encontrado: " + result);
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void actualizarTelefono(String number, TelefonoModelCli modelo) {
		try {
			TelefonoModelCli result = telefonoMapperCli.fromDomainToAdapterCli(
					phoneInputPort.edit(number, telefonoMapperCli.fromAdapterToDomain(modelo)));
			System.out.println("Telefono actualizado: " + result);
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void eliminarTelefono(String number) {
		try {
			Boolean result = phoneInputPort.drop(number);
			System.out.println("Telefono eliminado: " + result);
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}
}
