package org.babinkuk.batch.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
public class ScheduledJobsTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledJobsTest.class);
	
	@Autowired
	private Job jobOne;
	
	@Autowired
	private Job jobTwo;
	
	@Autowired
	private Job coffeeJob;
	
	@Autowired
	private ScheduledJobs scheduledJobs;
	
	@Test
	void givenJobsDefinitions_whenJobsLoaded_thenJobNamesShouldMatch() {
		assertNotNull(jobOne, "jobOne should be defined");
		assertEquals("jobOne", jobOne.getName());
		
		assertNotNull(jobTwo, "jobTwo should be defined");
		assertEquals("jobTwo", jobTwo.getName());
		
		assertNotNull(coffeeJob, "coffeeJob should be defined");
		assertEquals("coffeeJob", coffeeJob.getName());
	}
	
	@Test
	void givenJobOne_whenExecuted_thenRunJobs() {
		assertDoesNotThrow(() -> scheduledJobs.runJob1(), "JobOne execution should execute");
	}
	
	@Test
	void givenJobTwo_whenExecuted_thenRunJobs() {
		assertDoesNotThrow(() -> scheduledJobs.runJob2(), "JobTwo execution should execute");
	}
	
	@Test
	void givenCoffeeJob_whenExecuted_thenRunJobs() {
		assertDoesNotThrow(() -> scheduledJobs.runCoffeeJob(), "CoffeeJob execution should execute");
	}
}
