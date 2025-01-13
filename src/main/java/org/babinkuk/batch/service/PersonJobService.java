package org.babinkuk.batch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.babinkuk.batch.config.JobsConfig;
import org.babinkuk.batch.listener.PersonJobCompletionNotificationListener;
import org.babinkuk.batch.model.Coffee;
import org.babinkuk.batch.model.Person;
import org.babinkuk.batch.processor.CoffeeItemProcessor;
import org.babinkuk.batch.processor.PersonItemProcessor;
import org.babinkuk.batch.repository.PersonRepository;
import org.babinkuk.batch.writer.PersonItemWriter;
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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Dynamic Job Execution Configuration
 */
@Service
public class PersonJobService {
	
	private static final Logger log = LoggerFactory.getLogger(PersonJobService.class);
	
	private final JobRepository jobRepository;
	private final JobLauncher jobLauncher;
	private final PlatformTransactionManager transactionManager;
	private final PersonRepository repository;
	
	public PersonJobService(JobRepository jobRepository, JobLauncher jobLauncher, PlatformTransactionManager transactionManager, PersonRepository repository) {
		this.jobRepository = jobRepository;
		this.jobLauncher = jobLauncher;
		this.transactionManager = transactionManager;
		this.repository = repository;
	}

	public void createAndRunJob(Map<String, List<Person>> jobsData) throws Exception {
	    List<Job> jobs = new ArrayList<>();
	
	    // Create chunk-oriented jobs
		for (Map.Entry<String, List<Person>> entry : jobsData.entrySet()) {
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
	private Job createJob(String jobName, List<Person> data) {
		return new JobBuilder(jobName, jobRepository)
				.start(createStep(data))
				.listener(listener())
				.build();
	}
	
	/**
	 * batch job step definition
	 * 1. The reader() method reads items one at a time from the input list. 
	 * 2. Each item is passed to the processor(). The processed items are then collected into a chunk, with a chunk size defined as 10. 
	 * 3. Once a chunk is filled or there is no more data, all items in the chunk are passed to the writer(). 
	 * 4. The writer subsequently prints all items in the chunk to the console, and this process repeats until all items are processed.
	 * 
	 * @param data
	 * @return
	 */
	private Step createStep(List<Person> data) {
		return new StepBuilder("step", jobRepository)
				.<Person, Person> chunk(10, transactionManager)
				.reader(reader(data))
				.processor(processor())
				.writer(writer())
				.build();
	}	

	/**
	 * custom reader
	 * 
	 * @return
	 */
	private ItemReader<Person> reader(List<Person> data) {
		return new ListItemReader<>(data);
	}

	/**
	 * custom processor
	 * 
	 * @return
	 */
	private PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	/**
	 * custom writer
	 * 
	 * @return
	 */
	public PersonItemWriter writer() {
		return new PersonItemWriter(repository);
	}
	
	/**
	 * custom listener
	 * 
	 * @return
	 */
	private PersonJobCompletionNotificationListener listener() {
		return new PersonJobCompletionNotificationListener(repository);
	}

}
