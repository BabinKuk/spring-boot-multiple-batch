package org.babinkuk.batch.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.babinkuk.batch.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PersonJobServiceTest {
	
	@Autowired
	private PersonJobService service;

	@Test
	void givenJobData_whenJobsCreated_thenJobsRunSeccessfully() throws Exception {
		
		Person person1 = new Person(1, "ime1", "prezime1", 10, false);
		Person person2 = new Person(2, "ime2", "prezime2", 20, true);
		Person person3 = new Person(3, "ime3", "prezime3", 30, true);
		
		Map<String, List<Person>> jobsData = new HashMap<>();
		jobsData.put("personJob1", Arrays.asList(person1));
		jobsData.put("personJob2", Arrays.asList(person2, person3));
		
		assertDoesNotThrow(() -> service.createAndRunJob(jobsData), "Person job creation and execution should run successfully");
	}
}
