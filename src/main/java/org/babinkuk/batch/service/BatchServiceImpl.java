package org.babinkuk.batch.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.babinkuk.batch.common.ApiResponse;
import org.babinkuk.batch.model.Person;
import org.babinkuk.batch.model.Persons;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class BatchServiceImpl implements BatchService {

	private final Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private PersonJobService personJobService;

	public BatchServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ApiResponse runJob(Job job) throws Exception {
		
		ApiResponse response = new ApiResponse();
		response.setStatus(HttpStatus.OK);
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("jobName", job.getName() + String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		
		response.setMessage("Batch Job " + job.getName() + " started with JobExecutionId: " + jobExecution.getId());
		
		return response;
	}

	@Override
	public ApiResponse runJob(Persons persons) throws Exception {

		ApiResponse response = new ApiResponse();
		response.setStatus(HttpStatus.OK);
		
		if (persons.getPersonList().isEmpty()) {
			throw new Exception("Person object is null");
		}
		
		Map<String, List<Person>> jobsData = new HashMap<>();
		jobsData.put("personJob", persons.getPersonList());
		
		personJobService.createAndRunJob(jobsData);
		
		response.setMessage("Person Batch Job started");
		
		return response;
	}
}
