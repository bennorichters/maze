package org.bnor.maze;

import org.bnor.euler.Fraction;

public final class CircleCoordinate {
	
	private static final int ANGLE_FULL_CIRCLE = 360;

	private static final Fraction FRACTION_ANGLE_FULL_CIRCLE = Fraction.valueOf(ANGLE_FULL_CIRCLE);

	// An arc with this angle (60 degrees) is the first arc which angle is a divider of the
	// full circle (360 degrees) and has a length greater than the radius of the circle.
	// (The angle for which the length of the arc equals the radius is 1 rad ~ 57.3 degrees.) 
	private static final int ANGLE_STEP_FIRST_CIRCLE = 60;
	private static final int ARCS_FIRST_CIRCLE = ANGLE_FULL_CIRCLE / ANGLE_STEP_FIRST_CIRCLE;

	public static final CircleCoordinate ORIGIN = create(0, 0);
	
	private final int circle;
	private final int arcIndex;
	private final Fraction angle;

	private CircleCoordinate(int circle, int arcIndex, Fraction angle) {
		if (circle < 0) {
			throw new IllegalArgumentException("circle should be greater than or equal to zero. circle: " + circle);
		}
		
		if (arcIndex < 0) {
			throw new IllegalArgumentException("arcIndex should be greater than or equal to zero, arcIndex: " + arcIndex);
		}

		if (arcIndex > (calcTotalArcs(circle) - 1)) {
			throw new IllegalArgumentException("arcIndex too big for " + circle + ", arcIndex: " + arcIndex);
		}
		
		this.circle = circle;
		this.arcIndex = arcIndex;
		this.angle = angle;
	}
	
	public static CircleCoordinate create(int circle, int arcIndex) {
		Fraction angle = calcAngleStep(circle).multiply(Fraction.valueOf(arcIndex));
		return new CircleCoordinate(circle, arcIndex, angle);
	}

	static CircleCoordinate create(int circle, Fraction angle) {
		Fraction step = calcAngleStep(circle);
		Fraction arcIndexFraction = angle.divide(step);
		
		if (arcIndexFraction.getDenominator().intValue() != 1) {
			throw new IllegalArgumentException("no such angle for circle. " + circle + ", angle: " + angle);
		}
		
		int arcIndex = arcIndexFraction.getNumerator().intValue();
		return new CircleCoordinate(circle, arcIndex, angle);
	}

	public int getCircle() {
		return circle;
	}

	public Fraction getAngle() {
		return angle;
	}
	
	int getArcIndex() {
		return arcIndex;
	}
	
	int calcTotalArcs() {
		return calcTotalArcs(circle);
	}
	
	public static int calcTotalArcs(int circle) {
		return roundDownToPowerOf2(circle) * ARCS_FIRST_CIRCLE;
	}

	public static Fraction calcAngleStep(int circle) {
		if (circle == 0) {
			return FRACTION_ANGLE_FULL_CIRCLE;
		}
		
		int powerOf2 = roundDownToPowerOf2(circle);
		return Fraction.valueOf(ANGLE_STEP_FIRST_CIRCLE, powerOf2);
	}

	private static int roundDownToPowerOf2(int in) {
		int rest = in;
		int out = 1;

		while (rest > 1) {
			rest >>= 1;
			out <<= 1;
		}

		return out;
	}

	@Override
	public String toString() {
		return "[" + circle + ", " + arcIndex + ", " + angle + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + circle;
		result = prime * result + arcIndex;

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CircleCoordinate)) {
			return false;
		}

		CircleCoordinate other = (CircleCoordinate) obj;
		return other.circle == circle && other.arcIndex == arcIndex;
	}
}
