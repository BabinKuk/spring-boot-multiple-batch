package org.babinkuk.batch.controller;
//
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.babinkuk.batch.common.ApiResponse;
import org.babinkuk.batch.common.Echo;
import org.babinkuk.batch.common.ProducesJson;
import org.babinkuk.batch.model.Person;
import org.babinkuk.batch.model.Persons;
import org.babinkuk.batch.service.BatchService;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

@RestController
@RequestMapping("/batch")
public class CommonController {
	
	private final Logger log = LogManager.getLogger(getClass());
	
	// services
	private BatchService service;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private Job jobOne;

	@Autowired
	private Job jobTwo;

	@Autowired
	public CommonController(BatchService service) {
		this.service = service;
	}
	
	// expose GET "/echo"
	@GetMapping("/echo")
	@ProducesJson
	public Echo echo() {
		return new Echo();
	}
	
	// expose GET "/config"
	@GetMapping("/config")
	//@ProducesJson
	public ResponseEntity<ApiResponse> getAppConfig() {
		
		Map<String, Object> propertyMap = new HashMap<String, Object>();
		propertyMap.put("application name", environment.getProperty("spring.application.name", "Unknown"));
		propertyMap.put("version", environment.getProperty("spring.application.version", "Unknown"));
		propertyMap.put("author", environment.getProperty("spring.application.author", "Unknown"));
		
		return new ApiResponse(HttpStatus.OK, null, propertyMap).toEntity();
	}
	
	@PostMapping("/one")
	public ResponseEntity<ApiResponse> runJobOne() throws Exception {
		return ResponseEntity.of(Optional.ofNullable(service.runJob(jobOne)));
	}

	@PostMapping("/two")
	public ResponseEntity<ApiResponse> runJobTwo() throws Exception {
		return ResponseEntity.of(Optional.ofNullable(service.runJob(jobTwo)));
	}
	
	@PostMapping("/person")
	public ResponseEntity<ApiResponse> runPersonJob(@RequestBody Persons persons) throws Exception {
		return ResponseEntity.of(Optional.ofNullable(service.runJob(persons)));
	}
}
