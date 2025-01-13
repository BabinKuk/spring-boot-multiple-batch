package org.babinkuk.batch.writer;

import org.babinkuk.batch.model.Person;
import org.babinkuk.batch.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class PersonItemWriter implements ItemWriter<Person> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonItemWriter.class);
	
	private final PersonRepository repository;
	
	public PersonItemWriter(PersonRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public void write(Chunk<? extends Person> chunk) throws Exception {
		chunk.forEach(item -> {
			LOGGER.info("Writing " + item.toString());
			
			// save to db
			repository.save(item);
		});
	}

}
