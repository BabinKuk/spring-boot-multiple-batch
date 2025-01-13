package org.babinkuk.batch.config;

import javax.sql.DataSource;

import org.babinkuk.batch.listener.CoffeeJobCompletionNotificationListener;
import org.babinkuk.batch.model.Coffee;
import org.babinkuk.batch.processor.CoffeeItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Job Configuration
 */
@Configuration
public class CoffeeJobConfiguration {

	@Value("${file.input}")
    private String fileInput;

	/**
	 * implements TaskExecutor using virtual threads providing a lightweight high-performance alternative to traditional threads,
	 * providing scalable and efficient execution of parallel tasks
	 * 
	 * @return
	 */
	@Bean
	public VirtualThreadTaskExecutor taskExecutor() {
		return new VirtualThreadTaskExecutor("virtual-thread-executor");
	}

	/**
	 * Reader bean</br>
	 * Looks for a file and parses each line item into a Coffee object
	 * 
	 * @return
	 */
	@Bean
	public FlatFileItemReader<Coffee> reader() {
		return new FlatFileItemReaderBuilder<Coffee>()
				.name("coffeeItemReader")
				.resource(new ClassPathResource(fileInput))
				.delimited()
				.names(new String[] { "brand", "origin", "characteristics" })
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Coffee>() {{
					setTargetType(Coffee.class);
				}})
				.build();
	}

	/**
	 * Writer bean</br>
	 * insert a single coffee item into database
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean
	public JdbcBatchItemWriter<Coffee> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Coffee>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO coffee (brand, origin, characteristics) VALUES (:brand, :origin, :characteristics)")
				.dataSource(dataSource)
				.build();
	}

	/**
	 * custom processor
	 * 
	 * @return
	 */
	@Bean
	public CoffeeItemProcessor processor() {
		return new CoffeeItemProcessor();
	}

	/**
	 * batch job definition
	 * contains an id using the built-in RunIdIncrementer class and custom JobCompletionNotificationListener to get notified when the job completes
	 * 
	 * @param jobRepository
	 * @param listener
	 * @param coffeeStep
	 * @return
	 */
	@Bean
	public Job coffeeJob(JobRepository jobRepository, CoffeeJobCompletionNotificationListener listener, Step coffeeStep) {
		return new JobBuilder("coffeeJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(coffeeStep)
				.end()
				.build();
	}

	/**
	 * Batch job step implementation
	 * 1. write up to ten records at a time using the chunk(10) declaration.
	 * 2. read in the coffee data using reader bean, which we set using the reader method
	 * 3. pass each coffee item to a custom processor where we apply some custom business logic
	 * 4. finally, write each coffee item to the database using the writer
	 * 
	 * @param jobRepository
	 * @param transactionManager
	 * @param writer
	 * @param taskExecutor
	 * @return
	 */
	@Bean
	public Step coffeeStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Coffee> writer, VirtualThreadTaskExecutor taskExecutor) {
		return new StepBuilder("coffeeStep", jobRepository)
				.<Coffee, Coffee> chunk(10, transactionManager)
				.reader(reader())
				.processor(processor())
				.writer(writer)
				.taskExecutor(taskExecutor)
				.build();
	}
}
