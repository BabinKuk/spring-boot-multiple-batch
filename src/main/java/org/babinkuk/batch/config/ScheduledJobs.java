package org.babinkuk.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Job Scheduling configuration
 */
@Configuration
@EnableScheduling
public class ScheduledJobs {

	private static final Logger log = LoggerFactory.getLogger(ScheduledJobs.class);
	
	@Autowired
	private Job jobOne;
	
	@Autowired
	private Job jobTwo;
	
	@Autowired
	private Job coffeeJob;
	
	@Autowired
	private JobLauncher jobLauncher;

	@Scheduled(cron = "0 */1 * * * *")  // Run every minute
	public void runJob1() throws Exception {
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("jobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		
		log.info("Executing sheduled job 1");
		jobLauncher.run(jobOne, jobParameters);
    }

	@Scheduled(fixedRate = 1000 * 60 * 3)  // Run every 3 minutes
	public void runJob2() throws Exception {
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("jobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		
		log.info("Executing sheduled job 2");
		jobLauncher.run(jobTwo, jobParameters);
	}
	
	@Scheduled(cron = "0 */1 * * * *")  // Run every minute
	public void runCoffeeJob() throws Exception {
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("jobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		
		log.info("Executing sheduled coffee job");
		jobLauncher.run(coffeeJob, jobParameters);
    }
}