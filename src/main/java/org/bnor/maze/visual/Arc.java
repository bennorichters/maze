package org.bnor.maze.visual;

import org.bnor.euler.Fraction;

final class Arc implements Mergeable<Arc> {
	
	private static final Fraction ANGLE_360 = Fraction.valueOf(360);

	private final PolarCoordinate from;
	private final Fraction span;
	private final Fraction endAngle;
	private final boolean throughZero;
	
	private Arc(PolarCoordinate from, Fraction span) {
		if (from.getRadius() < 0) {
			throw new IllegalArgumentException("Radius of from should be greater than or equal to zero. from: " + from);
		}
		
		if ((from.getAngle().compareTo(Fraction.ZERO) < 0) || (from.getAngle().compareTo(ANGLE_360) >= 0)) {
			throw new IllegalArgumentException("Angle of from should be between zero (inclusive) and 360 (exclusive). from: " + from);
		}
		
		if ((span.compareTo(Fraction.ZERO) <= 0) || (span.compareTo(ANGLE_360) > 0)) {
			throw new IllegalArgumentException("span should be between zero (exclusive) and 360 (inclusive). span: " + span);
		}
		
		this.from = from;
		this.span = span;

		boolean calcThroughZero = false;
		Fraction calcEndAngle = from.getAngle().add(span);
		while (calcEndAngle.compareTo(ANGLE_360) >= 0) {
			calcThroughZero = true;
			calcEndAngle = calcEndAngle.subtract(ANGLE_360);
		}
		
		this.throughZero = calcThroughZero;
		this.endAngle = calcEndAngle;
	}
	
	static Arc create(PolarCoordinate from, Fraction span) {
		return (span.equals(ANGLE_360)) ? new Arc(PolarCoordinate.create(from.getRadius(), Fraction.ZERO), ANGLE_360) : new Arc(from, span);
	}
	
	PolarCoordinate getFrom() {
		return from;
	}

	Fraction getSpan() {
		return span;
	}

	Fraction getEndAngle() {
		return endAngle;
	}

	@Override
	public boolean isOn(PolarCoordinate point) {
		if (getFrom().getRadius() != point.getRadius()) {
			return false;
		}
				
		if (throughZero) {
			return (getFrom().getAngle().compareTo(point.getAngle()) <= 0) || (getEndAngle().compareTo(point.getAngle()) >= 0); 
		}
		
		return (getFrom().getAngle().compareTo(point.getAngle()) <= 0) && (getEndAngle().compareTo(point.getAngle()) >= 0); 
	}
	
	@Override
	public boolean canMerge(Mergeable<Arc> obj) {
		Arc other = (Arc) obj;
		return isOn(other.getFrom()) || other.isOn(getFrom());
	}
	
	@Override
	public Arc merge(Mergeable<Arc> object) {
		if (isFullCircle()) {
			return this;
		}
		
		Arc other = (Arc) object;
		
		if (other.isFullCircle()) {
			return other;
		}
		
		if (!canMerge(other)) {
			throw new IllegalArgumentException("cannot merge with other.");
		}
		
		PolarCoordinate mergedFrom = isOn(other.getFrom()) ? from : other.from;
		
		Fraction mergedSpan1 = calcSpan(mergedFrom.getAngle(), endAngle);
		Fraction mergedSpan2 = calcSpan(mergedFrom.getAngle(), other.endAngle);
		
		if (mergedSpan1.equals(Fraction.ZERO) || mergedSpan2.equals(Fraction.ZERO)) {
			return create(PolarCoordinate.create(from.getRadius(), Fraction.ZERO), ANGLE_360);
		}
		
		Fraction mergedSpan = mergedSpan1.compareTo(mergedSpan2) >= 0 ? mergedSpan1 : mergedSpan2;
		
		return create(mergedFrom, mergedSpan);
	}

	private boolean isFullCircle() {
		return span.equals(ANGLE_360);
	}
	
	private static Fraction calcSpan(Fraction start, Fraction end) {
		Fraction endNormalized = (end.compareTo(start) < 0) ? ANGLE_360.add(end) : end;
		return endNormalized.subtract(start);
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		
		result = prime * result + from.hashCode();
		result = prime * result + endAngle.hashCode();
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Arc)) {
			return false;
		}
		
		Arc other = (Arc) obj;
		return from.equals(other.from) && endAngle.equals(other.endAngle);
	}

	@Override
	public String toString() {
		return "Arc [from=" + from + ", span=" + span + ", endAngle=" + endAngle + ", throughZero=" + throughZero + "]";
	}
}
