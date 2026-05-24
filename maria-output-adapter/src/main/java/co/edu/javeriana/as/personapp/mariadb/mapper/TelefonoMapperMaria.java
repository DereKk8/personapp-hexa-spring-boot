package co.edu.javeriana.as.personapp.mariadb.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;

@Mapper
public class TelefonoMapperMaria {

	public TelefonoEntity fromDomainToAdapter(Phone phone) {
		TelefonoEntity telefonoEntity = new TelefonoEntity();
		telefonoEntity.setNum(phone.getNumber());
		telefonoEntity.setOper(phone.getCompany());
		if (phone.getOwner() != null) {
			PersonaEntity duenio = new PersonaEntity();
			duenio.setCc(phone.getOwner().getIdentification());
			telefonoEntity.setDuenio(duenio);
		}
		return telefonoEntity;
	}

	public Phone fromAdapterToDomain(TelefonoEntity telefonoEntity) {
		Phone phone = new Phone();
		phone.setNumber(telefonoEntity.getNum());
		phone.setCompany(telefonoEntity.getOper());
		if (telefonoEntity.getDuenio() != null) {
			Person owner = new Person();
			owner.setIdentification(telefonoEntity.getDuenio().getCc());
			phone.setOwner(owner);
		}
		return phone;
	}
}