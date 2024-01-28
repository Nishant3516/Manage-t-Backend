package com.ssik.manageit.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ssik.manageit.util.GZFileCompressUtils;

@Component
@Scope("singleton")
public class BackupService {

	private final Logger log = LoggerFactory.getLogger(BackupService.class);

	public static final String BACKUP_SUFFIX = "_backup.sql.gz";
	public static final String BACKUP_DIR = "/tmp";

	private static final Logger LOG = LoggerFactory.getLogger(BackupService.class);

	private static final String COMPRESSED_FILE_EXTENSION = "gz";
	private static final String TMP_SUFFIX = "_tmp";

	private static final String LOCAL_ADDRESS = "127.0.0.1"; // NOSONAR not configurable

	private Date lastScheduledDbBackupDateTime;

	private int scheduledDbBackupAttempts;

	private boolean isBackupInProgress;

	private boolean isRestoreInProgress;

	@Autowired
	GZFileCompressUtils gzFileCompressUtils;

	@Value("${spring.datasource.username}")
	private String dbUserName;
	@Value("${spring.datasource.password}")
	private String dbUserPwd;
	@Value("${spring.datasource.url}")
	private String dbUrl;
	@Value("${manageit.pgdumppath}")
	private String pgDumpLocation;
	@Value("${manageit.toemails}")
	private String toAddresses;

	@Value("${manageit.mailsubject}")
	private String mailSubject;

	@Autowired
	AuditLogService auditLogService;
//    @Autowired
//    MailSender mailSender;
	@Autowired
	MailService mailService;
	String dbName;

	public void attemptScheduledDbBackup() { // NOSONAR (The Cyclomatic Complexity of this method is 11 which is greater
												// than 10 authorized)

		if (this.isBackupInProgress || this.isRestoreInProgress) {
			return;
		}

		this.isBackupInProgress = true;

		LOG.info("Starting db backup...");

		scheduledDbBackupAttempts++;

		try {
			this.backupDb();

		} catch (Exception e) {
			// If the last attempt fails, raise a high priority alert
			// and update the last execution time to stop retrying for the day
			log.error("Error taking backup ", e);

		} finally {
			this.isBackupInProgress = false;
		}

	}

	public boolean getIsBackupInProgress() {
		return this.isBackupInProgress;
	}

	public boolean getIsRestoreInProgress() {
		return this.isRestoreInProgress;
	}

	// package level access for testing
	void backupDb() throws IOException {

		// this.checkBackupConfiguration();
		dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1, dbUrl.length());

		File backupWorkingDir = new File(BACKUP_DIR + "/ssikbackups");
		if (!backupWorkingDir.exists()) {
			LOG.info("The local backup directory {} does not exist - creating it", backupWorkingDir);
			backupWorkingDir.mkdir(); // NOSONAR ignoring the return value - it doesn't matter
		}

		String outputFilePath = backupWorkingDir + "/" + this.buildBackupFileName("ssik_" + LocalDate.now().toString());
		this.tryDeleteFile(outputFilePath);

		String command = this.buildBackupDbCommand(dbUserName, dbName, pgDumpLocation);

		executeDbCommand(null, outputFilePath, dbUserPwd, command);

		String compressedFilePath = this.compressFile(outputFilePath);
		// Delete the local copy of the un-compressed backup file
		this.deleteFile(outputFilePath);

		// this.uploadBackupFileToRemoteServer(outputFilePath);
		// send mail
		File[] attachments = new File[1];
		File attachment = null;
		FileInputStream backUpInputStream = null;
		String toList;
		String subject;
		String templateName;
		String dateToday = "";
		final Map<String, Object> templateData = new HashMap<>();
		try {
			attachment = new File(compressedFilePath);
			backUpInputStream = new FileInputStream(compressedFilePath);
			attachments[0] = attachment;
			templateName = "daily-backup";
			toList = toAddresses;
			DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
			dateToday = dateFormatter.format(new Date());
			subject = mailSubject + " dated" + dateToday;
// sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml,final File... attachments)
			mailService.sendEmail(toList, subject, "DB Back up file", true, true, attachments);
			// throw new ErrorCodeException(ErrorCodes.EMAIL_FAILED);
			// }
		} catch (Exception e) {
			log.error("Error creating backup and sending mail ", e);
			throw new RuntimeException("Error creating backup and sending it as attachment");

		} finally {
			IOUtils.closeQuietly(backUpInputStream);
		}

	}

	/**
	 * Executes a system command with an optional input file, and captures the
	 * output streams. If the command fails, an ErrorCodeException is thrown with
	 * the error stream
	 * 
	 * @param inputFilePath  null or path of file to stream as input to command
	 * @param outputFilePath null or path of file to write output from command
	 * @param dbPassword
	 * @param command
	 * @throws IOException
	 */
	void executeDbCommand(String inputFilePath, String outputFilePath, String dbPassword, String command)
			throws IOException {
		String printableCommand = command.replace(dbPassword, "*****");
		LOG.debug("Executing command... [command: {}]", printableCommand);

		CommandLine commandLine = CommandLine.parse(command);
		DefaultExecutor executor = new DefaultExecutor();
		ByteArrayOutputStream commandOutputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream errorOutputStream = new ByteArrayOutputStream();
		FileOutputStream fileOutputStream = null;
		FileInputStream fileInputStream = null;
		if (inputFilePath != null) {
			fileInputStream = new FileInputStream(inputFilePath);
		}
		PumpStreamHandler streamHandler = new PumpStreamHandler(commandOutputStream, errorOutputStream,
				fileInputStream);
		Map<String, String> environment = new HashMap<>(System.getenv());
		environment.put("PGPASSWORD", dbPassword);
		executor.setStreamHandler(streamHandler);
		try {
			executor.execute(commandLine, environment);
			if (outputFilePath != null) {
				fileOutputStream = new FileOutputStream(outputFilePath);
				commandOutputStream.writeTo(fileOutputStream);
			}
		} catch (ExecuteException e) {
			String errorString = new String(errorOutputStream.toByteArray(), Charset.forName(CharEncoding.UTF_8));
			LOG.error("The command returned an unexpected exit value [exitValue: {}, errorMessage: {}]",
					e.getExitValue(), errorString, e);
			throw new RuntimeException("Error running DB command");
		} catch (Exception e) {
			LOG.error("The command returned an unexpected exit value ", e);
			throw new RuntimeException("Error running DB command");
		} finally {
			this.tryCloseOutputStream(commandOutputStream);
			this.tryCloseOutputStream(errorOutputStream);
			this.tryCloseOutputStream(fileOutputStream);
			this.tryCloseInputStream(fileInputStream);
		}
	}

	private void tryCloseOutputStream(OutputStream stream) {
		try {
			if (stream != null) {
				stream.close();
			}
		} catch (IOException e) {
			LOG.error("Failed to close the output stream", e);
		}
	}

	private void tryCloseInputStream(InputStream inputStream) {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (IOException e) {
			LOG.error("Failed to close the input stream", e);
		}
	}

	private String buildBackupDbCommand(String username, String dbName, String pgDumpLocation) {
		return String.format(pgDumpLocation + " -U %s %s -h %s", username, dbName, LOCAL_ADDRESS);
	}

	private String buildRestoreDbCommand(String username, String dbName) {
		return String.format("psql -U %s %s -h %s", username, dbName, LOCAL_ADDRESS);
	}

	private boolean hasDbBackupRunToday() {

		DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

		if (lastScheduledDbBackupDateTime == null) {
			return false;
		}

		try {
			Date today = dateFormatter.parse(dateFormatter.format(new Date()));
			Date lastRunDate = dateFormatter.parse(dateFormatter.format(lastScheduledDbBackupDateTime));

			return !today.after(lastRunDate);

		} catch (ParseException e) {
			return true;
		}

	}

	// This method compares if the time part of the current dateTime is past the
	// scheduled backup time

	private String compressFile(String filePath) throws IOException {

		String tempCompressedFilePath = this.addTmpSuffixToFileName(filePath);

		this.tryDeleteFile(tempCompressedFilePath);

		// Compress the backup file
		String compressedTempFilePath = this.gzFileCompressUtils.compress(filePath, tempCompressedFilePath);

		String compressedFilePath = this.removeTmpSuffixFromFileName(compressedTempFilePath);

		Path compressedTempFile = Paths.get(compressedTempFilePath);
		Path compressedFile = Paths.get(compressedFilePath);

		this.tryDeleteFile(compressedFilePath);

		this.renameFile(compressedTempFile, compressedFile);

		LOG.debug("Database backup file successfully compressed. [path: {}]", compressedFilePath);

		return compressedFilePath;

	}

	// package level access for testing
	void renameFile(Path originalPath, Path newPath) throws IOException {
		Files.move(originalPath, newPath);
	}

	private String addTmpSuffixToFileName(String filePath) {
		return filePath.substring(0, filePath.lastIndexOf(".")) + TMP_SUFFIX
				+ filePath.substring(filePath.lastIndexOf(".")) + "." + COMPRESSED_FILE_EXTENSION;
	}

	private String removeTmpSuffixFromFileName(String filePath) {

		String tempFilePath = filePath.substring(0, filePath.lastIndexOf("/"));

		String tempFileName = filePath.substring(filePath.lastIndexOf("/") + 1);

		String fileName = "";

		if ("_tmp".equals(
				tempFileName.substring(tempFileName.indexOf(".") - TMP_SUFFIX.length(), tempFileName.indexOf(".")))) {
			fileName = tempFileName.substring(0, tempFileName.indexOf(".") - TMP_SUFFIX.length())
					+ tempFileName.substring(tempFileName.indexOf("."));
		}

		return tempFilePath + "/" + fileName;
	}

	// package level access for testing
	void deleteFile(String filePath) throws IOException {
		Files.delete(Paths.get(filePath));
	}

	// package level access for testing
	void tryDeleteFile(String filePath) {
		LOG.debug("deleting {}", filePath);
		FileUtils.deleteQuietly(new File(filePath));
	}

	private String buildBackupFileName(String backupLocation) {
		String location = backupLocation;

		return location + "_" + dbName + "_backup.sql";
	}

}
