package com.spring.mvc.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class PdfDAOImpl implements PdfDAO {

	/**
	 * Method creating a parent directory and saving the original pdf file on the server
	 *
	 * @param pdfByteArray	stores the contents of the uploaded pdf file
	 * @param targetFile	represents the pathway to the parent directory and the name of the pdf file
	 */
	@Override
	public void savePdf(byte[] pdfByteArray, File targetFile) throws IOException {
		
		File parent = targetFile.getParentFile();

		if (!parent.exists() && !parent.mkdirs()) {
			throw new IllegalStateException("Couldn't create the directory: " + parent);
		}
		
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(targetFile));
		stream.write(pdfByteArray);
		stream.close();
	}
}
