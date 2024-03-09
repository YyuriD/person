package telran.java51.person.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import telran.java51.person.dto.CityPopulationDto;
import telran.java51.person.model.Child;
import telran.java51.person.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
	Stream<Person> findByNameIgnoreCase(String name);

	Stream<Person> findByAddressCityIgnoreCase(@Param("cityName") String city);

	Stream<Person> findByBirthDateBetween(LocalDate from, LocalDate to);

	@Query("select new telran.java51.person.dto.CityPopulationDto(p.address.city, count(p)) from Person p group by p.address.city order by count(p) desc")
	List<CityPopulationDto> getCitiesPopulation();
	
	@Query("select c from Person c where c.class = ?1")		
	Stream<Person> findByType( Class type );
	
	@Query("select e from Person e where e.class = Employee AND e.salary >= ?1 AND e.salary < ?2")		
	Stream<Person> findBySalaryBetween(Integer from, Integer to);
	
	
	
}
