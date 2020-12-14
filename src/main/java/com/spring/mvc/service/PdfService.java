package com.spring.mvc.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.web.multipart.MultipartFile;

import com.spring.mvc.domain.Image;
import com.spring.mvc.domain.Rectangle;

/**
 * PdfService interface
 *
 * Date: 13.12.20
 *
 * @author krenkz
 */
public interface PdfService {

	public String savePdf(MultipartFile multipartFile) throws FileNotFoundException, IOException;

	public byte[] getPdf(Rectangle rectangle, Image image) throws IOException;

	public byte[] createByteArray(MultipartFile file) throws IOException;

	public File createTargetFile(MultipartFile multipartFile);

	public PDDocument initPdDocument(byte[] byteArray) throws IOException;

	public BufferedImage createImage(PDDocument PDDocument) throws IOException;

	public String createEncodedImgString(BufferedImage bufferedImage) throws IOException;

	public Rectangle resizeRectangle(PDRectangle mediaBox, Rectangle rectangle, Image image);

	public PDPage getLastPage(PDDocument pDDocument);

	public void drawRectangleToPdf(PDDocument pdDocument, PDPage page, Rectangle rectangle) throws IOException;

}
