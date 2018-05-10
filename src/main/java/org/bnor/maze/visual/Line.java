package org.bnor.maze.visual;

import org.bnor.euler.Fraction;

final class Line implements Mergeable<Line> {
	
	private static final Fraction ANGLE_360 = Fraction.valueOf(360);
	
	private final PolarCoordinate from;
	private final int endRadius;

	private Line(PolarCoordinate from, int endRadius) {
		if (from.getRadius() < 0) {
			throw new IllegalArgumentException("Radius of from should be greater than or equal to zero. from: " + from);
		}
		
		if ((from.getAngle().compareTo(Fraction.ZERO) < 0) || (from.getAngle().compareTo(ANGLE_360) >= 0)) {
			throw new IllegalArgumentException("Angle of from should be between zero (inclusive) and 360 (exclusive). from: " + from);
		}
		
		if (endRadius < 0) {
			throw new IllegalArgumentException("endRadius should be greater than or equal to from radius. from: " + from + ", endRadius: " + endRadius);
		}
		
		this.from = from;
		this.endRadius = endRadius;
	}
	
	static Line create(PolarCoordinate from, int endRadius) {
		return new Line(from, endRadius);
	}
	
	@Override
	public boolean isOn(PolarCoordinate point) {
		return point.getAngle().equals(from.getAngle()) && 
				(point.getRadius() >= from.getRadius()) && 
				(point.getRadius() <= endRadius);
	}
	
	@Override
	public boolean canMerge(Mergeable<Line> object) {
		Line other = (Line) object;
		return (isOn(other.from) || other.isOn(from));
	}
	
	@Override
	public Line merge(Mergeable<Line> object) {
		if (!canMerge(object)) {
			throw new IllegalArgumentException("this cannot be merged with other");
		}
		
		Line other = (Line) object;
		int startRadius = Math.min(from.getRadius(), other.from.getRadius());
		int calcEndRadius = Math.max(endRadius, other.endRadius);
		return new Line(PolarCoordinate.create(startRadius, from.getAngle()), calcEndRadius);
	}

	public PolarCoordinate getFrom() {
		return from;
	}
	
	public int getEndRadius() {
		return endRadius;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endRadius;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Line)) {
			return false;
		}
		
		Line other = (Line) obj;
		return endRadius == other.endRadius && from.equals(other.from);
	}

	@Override
	public String toString() {
		return "Line [from=" + from + ", endRadius=" + endRadius + "]";
	}
}
