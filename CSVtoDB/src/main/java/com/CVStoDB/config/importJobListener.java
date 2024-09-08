package com.CVStoDB.config;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class importJobListener implements JobExecutionListener{

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("Job {} started", jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("Job {} completed", jobExecution.getJobInstance().getJobName());
		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
			log.info("Job {} failed", jobExecution.getJobInstance().getJobName());
		}
	}

	
}
