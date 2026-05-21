package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;

@Mapper
public class PersonaMapperCli {

	public PersonaModelCli fromDomainToAdapterCli(Person person) {
		PersonaModelCli personaModelCli = new PersonaModelCli();
		personaModelCli.setCc(person.getIdentification());
		personaModelCli.setNombre(person.getFirstName());
		personaModelCli.setApellido(person.getLastName());
		personaModelCli.setGenero(person.getGender().toString());
		personaModelCli.setEdad(person.getAge());
		return personaModelCli;
	}

	public Person fromAdapterToDomain(PersonaModelCli modelo) {
		Person person = new Person();
		person.setIdentification(modelo.getCc());
		person.setFirstName(modelo.getNombre());
		person.setLastName(modelo.getApellido());
		person.setGender("M".equalsIgnoreCase(modelo.getGenero()) ? Gender.MALE
				: "F".equalsIgnoreCase(modelo.getGenero()) ? Gender.FEMALE : Gender.OTHER);
		person.setAge(modelo.getEdad());
		return person;
	}
}
