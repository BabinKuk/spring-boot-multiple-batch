package org.babinkuk.batch.listener;

import org.babinkuk.batch.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonJobCompletionNotificationListener implements JobExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonJobCompletionNotificationListener.class);

	private final PersonRepository repository;
	
	/**
	 * Job Completion
	 * providing some feedback when job finishes
	 * 
	 * @param jdbcTemplate
	 */
	@Autowired
	public PersonJobCompletionNotificationListener(PersonRepository repository) {
		this.repository = repository;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			LOGGER.info("!!! PERSON JOB FINISHED! Verify the results");
			
			repository.findAll().forEach(person -> LOGGER.info("Found < {} > in the database.", person));
		}
	}
}
