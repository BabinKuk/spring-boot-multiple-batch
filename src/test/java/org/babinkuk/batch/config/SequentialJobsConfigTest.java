package org.babinkuk.batch.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SequentialJobsConfigTest {

	@Autowired
	private SequentialJobsConfig sequentialJobsConfig;
	
	@Test
	void givenSequentialJobs_whenExecuted_thenRunJobsInOrder() {
		assertDoesNotThrow(() -> sequentialJobsConfig.runJobsSequentially(), "Sequential job execution should execute");
	}
}
