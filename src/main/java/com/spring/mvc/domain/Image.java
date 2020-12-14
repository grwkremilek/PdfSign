package com.spring.mvc.domain;

public class Image {
	
	public double width;
	public double height;
	
	public Image() {
		super();
	}

	public Image(double width, double height) {
		super();
		this.width = width;
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
	public Image multiplyBy(double number1, double number2) {
		 return new Image(this.width * number1, this.height * number2);
	}

	@Override
	public String toString() {
		return "image [width=" + width + ", height=" + height + "]";
	}
}
