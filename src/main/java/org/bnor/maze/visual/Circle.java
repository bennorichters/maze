package org.bnor.maze.visual;

final class Circle {

	private final PolarCoordinate center;
	private final int radius;

	private Circle(PolarCoordinate center, int radius) {
		if (center == null) {
			throw new NullPointerException("center");
		}
		
		if (radius <= 0) {
			throw new IllegalArgumentException("radius should be bigger than zero. radius: " + radius);
		}
		
		this.center = center;
		this.radius = radius;
	}
	
	static Circle create(PolarCoordinate center, int radius) {
		return new Circle(center, radius);
	}

	PolarCoordinate getCenter() {
		return center;
	}

	int getRadius() {
		return radius;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		
		result = prime * result + ((center == null) ? 0 : center.hashCode());
		result = prime * result + radius;
		
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
		return radius == other.radius && center.equals(other.center);
	}

	@Override
	public String toString() {
		return "Circle [center=" + center + ", radius=" + radius + "]";
	}
}
