package org.babinkuk.batch.service;

import java.util.List;

import org.babinkuk.batch.common.ApiResponse;
import org.babinkuk.batch.model.Persons;
import org.springframework.batch.core.Job;

public interface BatchService {
	
	public ApiResponse runJob(Job job) throws Exception;
	
	public ApiResponse runJob(Persons persons) throws Exception;
	
}
