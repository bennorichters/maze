package org.bnor.maze.visual;

import org.bnor.euler.Fraction;

final class PolarCoordinate {
	
	private static final Fraction ANGLE_TOO_BIG = Fraction.valueOf(360);
	
	private final int radius;
	private final Fraction angle;

	private PolarCoordinate(int radius, Fraction angle) {
		if (radius < 0) {
			throw new IllegalArgumentException("radius should be greater than or equal to zero. radius: " + radius);
		}
		
		if ((angle.compareTo(Fraction.ZERO) < 0) || (angle.compareTo(ANGLE_TOO_BIG) >= 0)) {
			throw new IllegalArgumentException("Angle too big, should be between zero (inclusive) and 360 (exclusive) degrees. angle: " + angle);
		}
		
		
		this.radius = radius;
		this.angle = angle;
	}

	static PolarCoordinate create(int radius, Fraction angle) {
		return new PolarCoordinate(radius, angle);
	}
	
	int getRadius() {
		return radius;
	}

	Fraction getAngle() {
		return angle;
	}

	PolarCoordinate extrapolate(int length) {
		return new PolarCoordinate(radius + length, angle);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + radius;
		result = prime * result + angle.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PolarCoordinate)) {
			return false;
		}
		
		PolarCoordinate other = (PolarCoordinate) obj;
		return (radius == other.radius) && angle.equals(other.angle);
	}

	@Override
	public String toString() {
		return "[" + radius + ", " + angle + "]";
	}
}
