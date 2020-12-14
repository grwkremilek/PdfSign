import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.spring.mvc.dao.PdfDAOImpl;

public class PdfDAOUnitTests {

	PdfDAOImpl PdfDAOImpl = new PdfDAOImpl();

	private byte[] byteArray;
	private Path workingDir;
	private Path pdfTestFile;

	@Before
	public void init() throws IOException {

		this.workingDir = Path.of("", "src/test/resources");
		this.pdfTestFile = workingDir.resolve("test.pdf");
		this.byteArray = Files.readAllBytes(pdfTestFile);
	}

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void shouldsavePdf() throws IOException {

		final File tempFile = folder.newFile("testFile.txt");
		PdfDAOImpl.savePdf(byteArray, tempFile);

		assertTrue(tempFile.length() != 0);
	}
}
