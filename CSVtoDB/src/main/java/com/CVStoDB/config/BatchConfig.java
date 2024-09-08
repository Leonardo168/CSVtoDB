package com.CVStoDB.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.CVStoDB.model.Customer;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Bean
	public FlatFileItemReader<Customer> reader() {
		return new FlatFileItemReaderBuilder<Customer>()
				.linesToSkip(1)
				.name("csvItemReader")
				.resource(new ClassPathResource("clientes.csv"))
				.delimited()
				.delimiter(",")
				.names("nome", "cartela")
				.fieldSetMapper(FieldSet -> {
					return Customer.builder()
							.cartela(FieldSet.readInt("cartela"))
							.nome(FieldSet.readString("nome"))
							.build();
				})
				.build();
	}
	
	@Bean
	public JpaItemWriter<Customer> writer(EntityManagerFactory entityManagerFactory){
		return new JpaItemWriterBuilder<Customer>()
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
	
	@Bean
	public Step csvImporterStep(ItemReader<Customer> reader, ItemWriter<Customer> writer,
			JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("csvImporterStep", jobRepository)
				.<Customer, Customer>chunk(50, transactionManager)
				.reader(reader)
				.writer(writer)
				.allowStartIfComplete(true)
				.build();
	}
	
	@Bean
	public Job csvImporterJob(Step csvImporterStep, JobRepository jobRepository, importJobListener listener) {
		return new JobBuilder("csvImporterJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(csvImporterStep)
				.end()
				.build();
	}
}
