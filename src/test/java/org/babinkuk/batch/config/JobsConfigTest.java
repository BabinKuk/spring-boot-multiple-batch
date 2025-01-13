package org.babinkuk.batch.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JobsConfigTest {

	@Autowired
	private Job jobOne;
	
	@Autowired
	private Job jobTwo;
	
	@Test
	void givenJobsDefinitions_whenJobsLoaded_thenJobNamesShouldMatch() {
		assertNotNull(jobOne, "jobOne should be defined");
		assertEquals("jobOne", jobOne.getName());
		
		assertNotNull(jobTwo, "jobTwo should be defined");
		assertEquals("jobTwo", jobTwo.getName());
	}
}
