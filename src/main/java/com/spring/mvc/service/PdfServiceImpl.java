package com.spring.mvc.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.mvc.dao.PdfDAO;
import com.spring.mvc.domain.Image;
import com.spring.mvc.domain.Pdf;
import com.spring.mvc.domain.Rectangle;

@Service
public class PdfServiceImpl implements PdfService {

	Pdf pdf = new Pdf();

	private String tomcatLocation;
	private String uploadDirName;
	private int dpi;

	@Autowired
	PdfDAO PdfDao;

	@Autowired
	public PdfServiceImpl(@Value("${tomcatLocation}") String tomcatLocation,
			@Value("${uploadDirName}") String uploadDirName, @Value("${dpi}") int dpi) {

		this.tomcatLocation = tomcatLocation;
		this.uploadDirName = uploadDirName;
		this.dpi = dpi;
	}

	/**
	 * Method calling other methods to create a pdf byte array, target File,
	 * PDDocument, BufferedImage and encode the image to a string, eventually also
	 * calls DAOImpl to save the pdf to a local folder
	 *
	 * @param multipartFile representation of the uploaded pdf
	 * @return imgEncodedString string representation of the (last page) image
	 */
	@Override
	public String savePdf(MultipartFile multipartFile) throws IOException {

		byte[] origPdfByteArray = createByteArray(multipartFile);
		File targetFile = createTargetFile(multipartFile);
		PDDocument pDDocument = initPdDocument(origPdfByteArray);
		BufferedImage bufferedImage = createImage(pDDocument);
		String imgEncodedString = createEncodedImgString(bufferedImage);

		pdf.setPdfByteArray(origPdfByteArray);
		pdf.setPdDocument(pDDocument);

		PdfDao.savePdf(origPdfByteArray, targetFile);

		return imgEncodedString;
	}

	/**
	 * Method calling other methods to create a representation of the last page,
	 * resize the rectangle, place it to the pdf and create a byte array with data
	 * of the pdf with the rectangle; a new PDDocument with the original byte array
	 * is created to be able to repeat the process
	 *
	 * @param rectangle ...
	 * @param image     ...
	 * @return ...
	 */
	@Override
	public byte[] getPdf(Rectangle rectangle, Image image) throws IOException {

		PDDocument pdDocument = pdf.getPdDocument();
		PDPage page = getLastPage(pdDocument);
		PDRectangle mediaBox = page.getMediaBox();
		Rectangle resizedRectangle = resizeRectangle(mediaBox, rectangle, image);

		drawRectangleToPdf(pdDocument, page, resizedRectangle);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		pdDocument.save(byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream.toByteArray();

		// initiate a new PDDocument to create a blank Pdf representation to write to
		PDDocument newpDDocument = initPdDocument(pdf.getPdfByteArray());
		pdf.setPdDocument(newpDDocument);

		return byteArray;
	}

	/**
	 * Method storing data from a MultipartFile in a byte array
	 *
	 * @param multipartFile representation of the uploaded pdf
	 * @return origPdfByteArray byte array with orig pdf data
	 */
	@Override
	public byte[] createByteArray(MultipartFile multipartFile) throws IOException {

		byte[] origPdfByteArray = multipartFile.getBytes();
		return origPdfByteArray;
	}

	/**
	 * Method creating a representation of the pathway to the file where on the
	 * server the pdf will be stored
	 *
	 * @param multipartFile representation of the uploaded pdf
	 * @return targetFile represents the pathway to the parent directory and the
	 *         name of the pdf file
	 */
	@Override
	public File createTargetFile(MultipartFile multipartFile) {

		String fileName = multipartFile.getOriginalFilename();
		File targetFile = new File(
				System.getProperty(tomcatLocation) + File.separator + uploadDirName + File.separator + fileName);
		return targetFile;

	}

	/**
	 * Method loading a byte array to a PdfBox PDDocument
	 *
	 * @param origPdfByteArray byte array with orig pdf data
	 * @return pdDocument PDDocument used to create a buffered image, get the last
	 *         page and draw the resized rectangle
	 */
	@Override
	public PDDocument initPdDocument(byte[] byteArray) throws IOException {

		PDDocument pdDocument = org.apache.pdfbox.pdmodel.PDDocument.load(byteArray);
		return pdDocument;
	}

	/**
	 * Method rendering an image from a PDDocument
	 *
	 * @param pdDocument PDDocument used to create a buffered image
	 * @return bufferedImage an image of the last page to be sent to the view for
	 *         the rectangle placement
	 */
	@Override
	public BufferedImage createImage(PDDocument pdDocument) throws IOException {

		PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
		BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pdDocument.getNumberOfPages() - 1, dpi);
		return bufferedImage;
	}

	/**
	 * Method encoding the image into a Base64 string
	 *
	 * @param bufferedImage an image to be send to the view for the rectangle
	 *                      placement
	 * @return encodedString Base64 string representing the image of the last page
	 */
	@Override
	public String createEncodedImgString(BufferedImage bufferedImage) throws IOException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", os);
		String encodedString = Base64.getEncoder().encodeToString(os.toByteArray());
		return encodedString;
	}

	/**
	 * Method resizing the original rectangle to the pdf page dimensions
	 *
	 * @param mediaBox  pdf dimensions (width, height)
	 * @param rectangle original rectangle dimensions (width, height, x, y)
	 * @param image     image dimensions (width, height)
	 * @return resizedRectangle resized rectangle dimensions (width, height, x, y)
	 */
	@Override
	public Rectangle resizeRectangle(PDRectangle mediaBox, Rectangle rectangle, Image image) {

		float dpiRatio = dpi / dpi;
		double widthRatio = mediaBox.getWidth() / image.width;
		double heightRatio = mediaBox.getHeight() / image.height;

		Rectangle resizedRectangle = rectangle.multiplyBy(dpiRatio, widthRatio, heightRatio);
		return resizedRectangle;
	}

	/**
	 * Method creating a PDPage representation of the last page
	 *
	 * @param pdDocument PDDocument used to get the last page of the pdf
	 * @return lastPage PDPage representation of the last page
	 */
	@Override
	public PDPage getLastPage(PDDocument pdDocument) {

		PDPage lastPage = pdDocument.getPage(pdDocument.getNumberOfPages() - 1);
		return lastPage;

	}

	/**
	 * Method placing the rectangle onto the last page
	 *
	 * @param pdDocument       PDDocument used to place the resized rectangle onto
	 *                         the last page
	 * @param page             PDPage representation of the last page
	 * @param resizedRectangle resized rectangle dimensions (width, height, x, y)
	 */
	@Override
	public void drawRectangleToPdf(PDDocument pdDocument, PDPage page, Rectangle resizedRectangle) throws IOException {

		PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page, AppendMode.APPEND, true, true);
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.addRect((float) resizedRectangle.x, (float) resizedRectangle.y, resizedRectangle.width,
				resizedRectangle.height);
		contentStream.stroke();
		contentStream.close();
	}

	/**
	 * Method deleting the file and parent folder from the server
	 */
	@PreDestroy
	public void preDestroy() throws IOException {

		File file = new File(System.getProperty(tomcatLocation) + File.separator + uploadDirName);
		FileUtils.deleteDirectory(file);
	}
}
