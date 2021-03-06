package org.bnor.maze.visual;

import static org.junit.Assert.assertTrue;

import org.bnor.euler.Fraction;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

@SuppressWarnings("static-method")
public class DrawingTest {

	@Test
	public void simple_arcs() {
		Drawing d1 = new Drawing();
		d1.addArc(arc(100, 10, 10));
		d1.addArc(arc(100, 15, 20));
		
		Drawing d2 = new Drawing();
		d2.addArc(arc(100, 10, 25));
		
		assertTrue(d1.equals(d2));
	}
	
	@Test
	public void simple_lines() {
		Drawing d1 = new Drawing();
		d1.addLine(line(100, 45, 110));
		d1.addLine(line(105, 45, 120));
		
		Drawing d2 = new Drawing();
		d2.addLine(line(100, 45, 120));
		
		assertTrue(d1.equals(d2));
	}
	
	@Test
	public void simple_circles() {
		Circle circle1 = circle(100, 45, 50);
		Circle circle2 = circle(110, 45, 50);

		Drawing d1 = new Drawing();
		d1.addCircle(circle1);
		d1.addCircle(circle2);
		d1.addCircle(circle1);

		Drawing d2 = new Drawing();
		d2.addCircle(circle2);
		d2.addCircle(circle1);
		
		assertTrue(d1.equals(d2));
	}

	@Test
	public void equalsContract() {
	    EqualsVerifier.forClass(Drawing.class)
	    		.suppress(Warning.NULL_FIELDS)
	    		.verify();
	}

	private static Line line(int radius, int angle, int endRadius) {
		return Line.create(PolarCoordinate.create(radius, Fraction.valueOf(angle)), endRadius);
	}

	private static Arc arc(int radius, int startAngle, int span) {
		return Arc.create(PolarCoordinate.create(radius, Fraction.valueOf(startAngle)), Fraction.valueOf(span));
	}
	
	private static Circle circle(int radiusCenter, int angleCenter, int diameter) {
		return Circle.create(PolarCoordinate.create(radiusCenter, Fraction.valueOf(angleCenter)), diameter);
	}
}
