import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import com.spring.mvc.domain.Image;
import com.spring.mvc.domain.Rectangle;
import com.spring.mvc.service.PdfServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PdfServiceUnitTests {

	PdfServiceImpl pdfServiceImpl = new PdfServiceImpl("catalina.home", "testDir", 72);

	Rectangle origRectangle;
	Image image;

	private Path workingDir;
	private Path pdfTestFile;
	private byte[] byteArray;
	private PDDocument pdDocument;

	@Before
	public void init() throws IOException {

		this.origRectangle = new Rectangle(160f, 40f, 426.0, 793.0);
		this.image = new Image(595.0, 841.0);

		this.workingDir = Path.of("", "src/test/resources");
		this.pdfTestFile = workingDir.resolve("test.pdf");
		this.byteArray = Files.readAllBytes(pdfTestFile);
		this.pdDocument = org.apache.pdfbox.pdmodel.PDDocument.load(byteArray);
	}

	@Test
	public void shouldSavePdf() {

		// to test or not to test?

	}

	@Test
	public void shouldGetPdf() {

		// to test or not to test?
	}

	@Test
	public void shouldCreateByteArray() throws IOException {

		MockMultipartFile uploadFile = new MockMultipartFile("test.pdf", "test".getBytes());
		assertTrue(pdfServiceImpl.createByteArray(uploadFile) instanceof byte[]);
	}

	@Test
	public void shouldCreateTargetFile() throws IOException {

		MockMultipartFile uploadFile = new MockMultipartFile("test.pdf", "test".getBytes());
		assertTrue(pdfServiceImpl.createTargetFile(uploadFile) instanceof File);
	}

	@Test
	public void shouldInitPdDocument() throws IOException {

		assertTrue(pdfServiceImpl.initPdDocument(byteArray) instanceof PDDocument);
	}

	@Test
	public void shouldCreateImage() throws IOException {

		assertTrue(pdfServiceImpl.createImage(pdDocument) instanceof BufferedImage);
	}

	@Test
	public void shouldCreateEncodedImgString() throws IOException {

		String target = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAC0lEQVR4XmNgAAIAAAUAAQYUdaMAAAAASUVORK5CYII=";
		BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

		assertEquals(pdfServiceImpl.createEncodedImgString(bufferedImage), target);
	}

	@Test
	public void shouldResizeRectangle() {

		PDRectangle mediaBox = new PDRectangle(0f, 0f, 595.30396f, 841.8898f);
		Rectangle target = new Rectangle(160f, 40f, 426.2176216189601, 793.8389869354284);
		Rectangle result = pdfServiceImpl.resizeRectangle(mediaBox, origRectangle, image);

		assertTrue(new ReflectionEquals(target).matches(result));
	}

	@Test
	public void shouldGetLastPage() throws IOException {

		assertTrue(pdfServiceImpl.getLastPage(pdDocument) instanceof PDPage);
	}

	@Test
	public void shouldDrawRectangleToPdf() throws IOException {

		PDPage page = pdDocument.getPage(pdDocument.getNumberOfPages() - 1);
		pdfServiceImpl.drawRectangleToPdf(pdDocument, page, origRectangle);
		byte[] outputByteArray = savePDDocument();

		assertTrue(!Arrays.equals(byteArray, outputByteArray));
	}

	private byte[] savePDDocument() throws IOException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		pdDocument.save(byteArrayOutputStream);
		pdDocument.close();
		return byteArrayOutputStream.toByteArray();
	}
}
