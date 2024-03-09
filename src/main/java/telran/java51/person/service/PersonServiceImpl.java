package telran.java51.person.service;

import java.time.LocalDate;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java51.person.dao.PersonRepository;
import telran.java51.person.dto.AddressDto;
import telran.java51.person.dto.ChildDto;
import telran.java51.person.dto.CityPopulationDto;
import telran.java51.person.dto.EmployeeDto;
import telran.java51.person.dto.PersonDto;
import telran.java51.person.dto.exceptions.PersonNotFoundException;
import telran.java51.person.model.Address;
import telran.java51.person.model.Child;
import telran.java51.person.model.Employee;
import telran.java51.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {

	final PersonRepository personRepository;
	final ModelMapper modelMapper;

	@Transactional
	@Override
	public Boolean addPerson(PersonDto personDto) {
		if (personRepository.existsById(personDto.getId())) {
			return false;
		}

		if (personDto instanceof ChildDto childDto) {
			personRepository.save(modelMapper.map(personDto, Child.class));
		} else if (personDto instanceof EmployeeDto employeeDto) {
			personRepository.save(modelMapper.map(personDto, Employee.class));
		} else {
			personRepository.save(modelMapper.map(personDto, Person.class));
		}
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		System.out.println(person.getClass().getSimpleName());
		return getMappedPersonDto(person);
	}

	@Transactional
	@Override
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException());
		personRepository.delete(person);
		return getMappedPersonDto(person);
	}

	@Transactional
	@Override
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException());
		person.setName(name);
		return getMappedPersonDto(person);
	}

	@Transactional
	@Override
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException());
		person.setAddress(modelMapper.map(addressDto, Address.class));
		return getMappedPersonDto(person);
	}

	@Transactional(readOnly = true)
	@Override
	public Iterable<PersonDto> findPersonsByCity(String city) {
		return personRepository.findByAddressCityIgnoreCase(city).map(p -> getMappedPersonDto(p))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public Iterable<PersonDto> findPersonsByName(String name) {
		return personRepository.findByNameIgnoreCase(name).map(p -> getMappedPersonDto(p))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge) {
		LocalDate from = LocalDate.now().minusYears(maxAge);
		LocalDate to = LocalDate.now().minusYears(minAge);
		return personRepository.findByBirthDateBetween(from, to).map(p -> getMappedPersonDto(p))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<CityPopulationDto> getCitiesPopulation() {
		return personRepository.getCitiesPopulation();
	}

	@Transactional(readOnly = true)
	@Override
	public Iterable<PersonDto> getChildren() {
		return personRepository.findByType(Child.class).map(p -> getMappedPersonDto(p))
				.collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	@Override
	public Iterable<PersonDto> findEmployeesBySalary(Integer from, Integer to) {	
		return personRepository.findBySalaryBetween(from, to).map(p -> getMappedPersonDto(p))
				.collect(Collectors.toList());
	}
	
	
	@Transactional
	@Override
	public void run(String... args) throws Exception {
		if (personRepository.count() == 0) {
			Person person = new Person(1000, "John", LocalDate.of(1985, 3, 11),
					new Address("Tel Aviv", "Ben Gvirol", 81));
			Child child = new Child(2000, "Mosche", LocalDate.of(2018, 7, 5), new Address("Ashkelon", "Bar Kohva", 21),
					"Shalom");
			Employee employee = new Employee(3000, "Sarah", LocalDate.of(1995, 11, 23),
					new Address("Rehovot", "Herzl", 7), "Motorola", 20_000);
			personRepository.save(person);
			personRepository.save(child);
			personRepository.save(employee);
		}

	}

	private PersonDto getMappedPersonDto(Person person) {
		if (person instanceof Child child) {
			return modelMapper.map(child, ChildDto.class);
		}
		if (person instanceof Employee employee) {
			return modelMapper.map(employee, EmployeeDto.class);
		}
		return modelMapper.map(person, PersonDto.class);
	}

	

}
