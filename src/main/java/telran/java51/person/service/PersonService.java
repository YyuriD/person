package telran.java51.person.service;

import telran.java51.person.dto.AddressDto;
import telran.java51.person.dto.CityPopulationDto;
import telran.java51.person.dto.PersonDto;

public interface PersonService {
	Boolean addPerson(PersonDto personDto);

	PersonDto findPersonById(Integer id);

	PersonDto removePerson(Integer id);

	PersonDto updatePersonName(Integer id, String name);

	PersonDto updatePersonAddress(Integer id, AddressDto addressDto);

	Iterable<PersonDto> findPersonsByCity(String city);

	Iterable<PersonDto> findPersonsByName(String name);

	Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge);

	Iterable<CityPopulationDto> getCitiesPopulation();

	Iterable<PersonDto> getChildren();

	Iterable<PersonDto> findEmployeesBySalary(Integer from, Integer to);
	
}
