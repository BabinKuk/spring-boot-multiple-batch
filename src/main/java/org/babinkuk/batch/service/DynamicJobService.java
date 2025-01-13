package org.babinkuk.batch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.babinkuk.batch.config.JobsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Dynamic Job Execution Configuration
 */
@Service
public class DynamicJobService {
	
	private static final Logger log = LoggerFactory.getLogger(DynamicJobService.class);
	
	private final JobRepository jobRepository;
	private final JobLauncher jobLauncher;
	private final PlatformTransactionManager transactionManager;

	public DynamicJobService(JobRepository jobRepository, JobLauncher jobLauncher, PlatformTransactionManager transactionManager) {
		this.jobRepository = jobRepository;
		this.jobLauncher = jobLauncher;
		this.transactionManager = transactionManager;
	}

	/**
	 * generate and launch jobs based on jobsData 
	 * 
	 * @param jobsData
	 * @throws Exception
	 */
	public void createAndRunJob(Map<String, List<String>> jobsData) throws Exception {
	    List<Job> jobs = new ArrayList<>();
	
	    // Create chunk-oriented jobs
		for (Map.Entry<String, List<String>> entry : jobsData.entrySet()) {
			if (entry.getValue() instanceof List) {
				jobs.add(createJob(entry.getKey(), entry.getValue()));
			}
		}
		
		// Run all jobs
		for (Job job : jobs) {
			JobParameters jobParameters = new JobParametersBuilder()
	    		.addString("jobID", String.valueOf(System.currentTimeMillis()))
	    		.toJobParameters();
	        jobLauncher.run(job, jobParameters);
	    }
	}
	
	/**
	 * batch job definition
	 * 
	 * @param jobName
	 * @param data
	 * @return
	 */
	private Job createJob(String jobName, List<String> data) {
		return new JobBuilder(jobName, jobRepository)
				.start(createStep(data))
				.build();
	}
	
	/**
	 * batch job step definition
	 * 1. The reader() method reads items one at a time from the input list. 
	 * 2. Each item is passed to the processor(), which converts the first letter of the item to uppercase. The processed items are then collected into a chunk, with a chunk size defined as 10. 
	 * 3. Once a chunk is filled or there is no more data, all items in the chunk are passed to the writer(). 
	 * 4. The writer subsequently prints all items in the chunk to the console, and this process repeats until all items are processed.
	 * 
	 * @param data
	 * @return
	 */
	private Step createStep(List<String> data) {
		return new StepBuilder("step", jobRepository)
				.<String, String> chunk(10, transactionManager)
				.reader(new ListItemReader<>(data))
				.processor(item -> item.toUpperCase())
				.writer(items -> items.forEach(log::info))
				.build();
	}
}
