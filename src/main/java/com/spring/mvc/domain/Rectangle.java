package com.spring.mvc.domain;

public final class Rectangle {

	// size
	public float width;
	public float height;

	// position
	public double x;
	public double y;

	public Rectangle(float width, float height) {
		super();
		this.width = width;
		this.height = height;
	}

	public Rectangle(float width, float height, double x, double y) {
		super();
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Rectangle multiplyBy(float dpiRatio, double widthRatio, double heightRatio) {
		return new Rectangle(this.width * dpiRatio, this.height * dpiRatio, this.x * widthRatio, this.y * heightRatio);
	}

	@Override
	public String toString() {
		return "Rectangle [width=" + width + ", height=" + height + ", x=" + x + ", y=" + y + "]";
	}
}
