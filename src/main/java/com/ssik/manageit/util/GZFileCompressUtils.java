package com.ssik.manageit.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class GZFileCompressUtils {

	private static final String EXTENSION = "gz";
	private static final int BUFFER_SIZE = 1024;

	public String compress(String inputFilePath, String outputFilePath) throws IOException {

		byte[] buffer = new byte[BUFFER_SIZE];

		try (FileInputStream fileInputStream = new FileInputStream(inputFilePath);
				GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new FileOutputStream(outputFilePath))) {

			int len;

			while ((len = fileInputStream.read(buffer)) > 0) {
				gzipOutputStream.write(buffer, 0, len);
			}

			return outputFilePath;

		}

	}

	public String decompress(String filePath) throws IOException {

		if (!StringUtils.endsWith(filePath, "." + EXTENSION)) {
			// throw new ErrorCodeException(ErrorCodes.INVALID_FILE_EXTENSION);
		}

		byte[] buffer = new byte[1024];

		String compressedFileName = filePath.substring(filePath.lastIndexOf("/") + 1);

		String folder = filePath.substring(0, filePath.lastIndexOf("/"));

		String uncompressedFileName = compressedFileName.replace("." + EXTENSION, "");

		String destinationPath = folder + "/" + uncompressedFileName;

		try (GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(filePath));
				FileOutputStream fileOutputStream = new FileOutputStream(destinationPath)) {

			int len;
			while ((len = gzipInputStream.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, len);
			}

			return destinationPath;

		}

	}

}
