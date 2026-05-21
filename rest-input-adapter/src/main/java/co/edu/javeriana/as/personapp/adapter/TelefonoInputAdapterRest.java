package co.edu.javeriana.as.personapp.adapter;

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
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterRest {

	@Autowired
	@Qualifier("telefonoOutputAdapterMaria")
	private PhoneOutputPort telefonoOutputPortMaria;

	@Autowired
	private TelefonoMapperRest telefonoMapperRest;

	PhoneInputPort phoneInputPort;

	@PostConstruct
	public void init() {
		phoneInputPort = new PhoneUseCase(telefonoOutputPortMaria);
	}

	public List<TelefonoResponse> historial() {
		log.info("Into historial TelefonoEntity in Input Adapter");
		return phoneInputPort.findAll().stream()
				.map(telefonoMapperRest::fromDomainToAdapterRest)
				.collect(Collectors.toList());
	}

	public TelefonoResponse crearTelefono(TelefonoRequest request) {
		try {
			return telefonoMapperRest
					.fromDomainToAdapterRest(phoneInputPort.create(telefonoMapperRest.fromAdapterToDomain(request)));
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public TelefonoResponse obtenerTelefono(String number) {
		try {
			return telefonoMapperRest.fromDomainToAdapterRest(phoneInputPort.findOne(number));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public TelefonoResponse actualizarTelefono(String number, TelefonoRequest request) {
		try {
			return telefonoMapperRest.fromDomainToAdapterRest(
					phoneInputPort.edit(number, telefonoMapperRest.fromAdapterToDomain(request)));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public TelefonoResponse eliminarTelefono(String number) {
		try {
			Boolean result = phoneInputPort.drop(number);
			return new TelefonoResponse(number, "", null, result ? "Eliminado" : "Error");
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}
}
