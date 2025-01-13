package org.babinkuk.batch.processor;

import org.babinkuk.batch.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Custom batch processor
 * ItemProcessor interface provides a mechanism to apply some specific business logic during our job execution.
 * To keep things simple, this processor takes an input PErson object and transforms each of the properties to uppercase.
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonItemProcessor.class);
	
	@Override
	public Person process(final Person person) {
		Integer id = person.getId();
		String firstName = person.getFirstName().toUpperCase();
		String lastName = person.getLastName().toUpperCase();
		Integer age = person.getAge();
		Boolean active = person.getActive();
		
		Person newPerson = new Person(id, firstName, lastName, age, active);
		LOGGER.info("Converting ( {} ) into ( {} )", person, newPerson);
		
		return newPerson;
	}
}
