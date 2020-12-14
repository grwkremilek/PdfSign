package com.spring.mvc.domain;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PreDestroy;

import org.apache.pdfbox.pdmodel.PDDocument;

public class Pdf {

	private PDDocument pdDocument;
	byte[] pdfByteArray;

	public Pdf() {
		super();
	}

	public Pdf(PDDocument pdDocument, byte[] pdfByteArray) {
		super();
		this.pdDocument = pdDocument;
		this.pdfByteArray = pdfByteArray;
	}

	public PDDocument getPdDocument() {
		return pdDocument;
	}

	public void setPdDocument(PDDocument pdDocument) {
		this.pdDocument = pdDocument;
	}

	public byte[] getPdfByteArray() {
		return pdfByteArray;
	}

	public void setPdfByteArray(byte[] pdfByteArray) {
		this.pdfByteArray = pdfByteArray;
	}

	@Override
	public String toString() {
		return "Pdf [pdDocument=" + pdDocument + ", pdfByteArray=" + Arrays.toString(pdfByteArray) + "]";
	}

	@PreDestroy
	public void preDestroy() throws IOException {

		pdDocument.close();

	}

}
