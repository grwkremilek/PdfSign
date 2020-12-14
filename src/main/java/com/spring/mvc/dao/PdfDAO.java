package com.spring.mvc.dao;

/**
 * PdfDAO interface
 *
 * Date: 13.12.20
 *
 * @author krenkz
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface PdfDAO {

	public void savePdf(byte[] pdfByteArray, File uploadDir) throws FileNotFoundException, IOException;

}
