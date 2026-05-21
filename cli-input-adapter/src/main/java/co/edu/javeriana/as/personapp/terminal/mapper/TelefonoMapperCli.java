package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;

@Mapper
public class TelefonoMapperCli {

	public TelefonoModelCli fromDomainToAdapterCli(Phone phone) {
		TelefonoModelCli model = new TelefonoModelCli();
		model.setNum(phone.getNumber());
		model.setOper(phone.getCompany());
		model.setDuenio(phone.getOwner() != null ? phone.getOwner().getIdentification() : null);
		return model;
	}

	public Phone fromAdapterToDomain(TelefonoModelCli model) {
		Phone phone = new Phone();
		phone.setNumber(model.getNum());
		phone.setCompany(model.getOper());
		if (model.getDuenio() != null) {
			Person owner = new Person();
			owner.setIdentification(model.getDuenio());
			phone.setOwner(owner);
		}
		return phone;
	}
}
