package org.babinkuk.batch.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DynamicJobServiceTest {
	
	@Autowired
	private DynamicJobService dynamicJobService;

	@Test
	void givenJobData_whenJobsCreated_thenJobsRunSeccessfully() throws Exception {
		Map<String, List<String>> jobsData = new HashMap<>();
		jobsData.put("chunkJob1", Arrays.asList("data1", "data2", "data3"));
		jobsData.put("chunkJob2", Arrays.asList("data4", "data5", "data6"));
		
		assertDoesNotThrow(() -> dynamicJobService.createAndRunJob(jobsData), "Dynamic job creation and execution should run successfully");
	}
}
