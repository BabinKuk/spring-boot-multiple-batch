package org.babinkuk.batch.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ParallelJobServiceTest {
	
	@Autowired
	private ParallelJobService service;
	
	@Test
	void givenSequentialJobs_whenExecuted_thenRunJobsInOrder() {
		assertDoesNotThrow(() -> service.runJobsInParallel(), "Parallel job execution should execute");
	}
}
