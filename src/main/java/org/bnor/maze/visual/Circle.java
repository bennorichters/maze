package org.bnor.maze.visual;

final class Circle {

	private final PolarCoordinate center;
	private final int diameter;

	private Circle(PolarCoordinate center, int diameter) {
		if (center == null) {
			throw new NullPointerException("center");
		}
		
		if (diameter <= 0) {
			throw new IllegalArgumentException("diameter should be bigger than zero. diameter: " + diameter);
		}
		
		this.center = center;
		this.diameter = diameter;
	}
	
	static Circle create(PolarCoordinate center, int diameter) {
		return new Circle(center, diameter);
	}

	PolarCoordinate getCenter() {
		return center;
	}

	int getDiameter() {
		return diameter;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		
		result = prime * result + center.hashCode();
		result = prime * result + diameter;
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Circle)) {
			return false;
		}
		
		Circle other = (Circle) obj;
		return diameter == other.diameter && center.equals(other.center);
	}

	@Override
	public String toString() {
		return "Circle [center=" + center + ", diameter=" + diameter + "]";
	}
}
