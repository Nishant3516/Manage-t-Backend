
package com.ssik.manageit.task;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ssik.manageit.service.BackupService;

@Component
public class EmailTask {

	@Autowired
	BackupService backupService;

	private final Logger log = LoggerFactory.getLogger(EmailTask.class);

//	@Scheduled(fixedDelay = 1000)
//	public void scheduleFixedDelayTask() {
//		log.info("Fixed delay task - " + System.currentTimeMillis() / 1000);
//		backupService.attemptScheduledDbBackup();
//	}

	// https://spring.io/blog/2020/11/10/new-in-spring-5-3-improved-cron-expressions
	@Scheduled(cron = "0 0 22 * * *")
	public void dailyMidnightDBBackUpTask() {
		log.info("Daily DB  - triggered at " + LocalDate.now().toString());
		backupService.attemptScheduledDbBackup();
	}

	@Scheduled(cron = "@midnight")
	public void dailyDBBackUpTask() {
		log.info("Midnight task   - triggered at " + LocalDate.now().toString());
		backupService.attemptScheduledDbBackup();
	}
}
