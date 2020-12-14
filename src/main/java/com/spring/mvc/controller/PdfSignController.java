package com.spring.mvc.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.mvc.service.PdfService;
import com.spring.mvc.domain.Image;
import com.spring.mvc.domain.Rectangle;

@Controller
public class PdfSignController {

	private int origRectangleWidth;
	private int origRectangleHeight;

	@Autowired
	public PdfSignController(@Value("${origRectangleWidth}") int origRectangleWidth,
			@Value("${origRectangleHeight}") int origRectangleHeight) {
		this.origRectangleWidth = origRectangleWidth;
		this.origRectangleHeight = origRectangleHeight;
	}
	
	@Autowired
	PdfService PdfService;

	@RequestMapping(value = "/")
	public String displayIndex() {

		return "index";
	}

	@RequestMapping(value = "/uploadPdfFile", method = RequestMethod.POST)
	public String uploadFileHandler(@RequestParam("file") MultipartFile multipartFile, Model model)
			throws FileNotFoundException, IOException {

		if (multipartFile.isEmpty() || !multipartFile.getContentType().equals("application/pdf")) {

			model.addAttribute("message", String.format("Please choose a PDF file."));

		} else {

			String encodedImgString = PdfService.savePdf(multipartFile);

			model.addAttribute("img", encodedImgString);
			model.addAttribute("message", String.format("The file was successfully uploaded."));
			model.addAttribute("load", "load");

			Rectangle origRectangle = new Rectangle(origRectangleWidth, origRectangleHeight);
			model.addAttribute("origRectangle", origRectangle);
		}

		return "index";
	}

	@RequestMapping(value = "/downloadPdfFile", method = RequestMethod.POST)
	public void downloadFileHandler(HttpServletResponse response, @RequestParam("rectangleWidth") Float rectangleWidth,

			@RequestParam("rectangleHeight") Float rectangleHeight, @RequestParam("imageWidth") Float imageWidth,

			@RequestParam("imageHeight") Float imageHeight, Model model) throws IOException {

		Image image = new Image(imageWidth, imageHeight);
		Rectangle rectangle = new Rectangle(origRectangleWidth, origRectangleHeight, rectangleWidth, rectangleHeight);

		byte[] byteArray = PdfService.getPdf(rectangle, image);

		response.setContentType("application/pdf");
		response.setHeader("content-disposition", "attachment; filename=" + "rectangledFile.pdf");
		response.setContentLength(byteArray.length);

		OutputStream os = response.getOutputStream();
		os.write(byteArray, 0, byteArray.length);
		os.close();
	}
}
