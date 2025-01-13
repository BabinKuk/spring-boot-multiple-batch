package org.babinkuk.batch.listener;

import org.babinkuk.batch.model.Coffee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CoffeeJobCompletionNotificationListener implements JobExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoffeeJobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	/**
	 * Job Completion
	 * providing some feedback when job finishes
	 * 
	 * @param jdbcTemplate
	 */
	@Autowired
	public CoffeeJobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			LOGGER.info("!!! JOB FINISHED! Verify the results");
			
			// trivial query to check that each coffee item was stored in the database successfully
			String query = "SELECT brand, origin, characteristics FROM coffee";
			jdbcTemplate.query(query, (rs, row) -> new Coffee(rs.getString(1), rs.getString(2), rs.getString(3)))
				.forEach(coffee -> LOGGER.info("Found < {} > in the database.", coffee));
		}
	}
}
