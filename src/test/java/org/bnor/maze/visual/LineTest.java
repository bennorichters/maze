package org.bnor.maze.visual;

import org.bnor.euler.Fraction;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import static org.junit.Assert.*;

@SuppressWarnings("static-method")
public class LineTest {

	@Test
	public void isOn_simple() {
		Line line = Line.create(PolarCoordinate.create(10, Fraction.ZERO), 110);
		
		assertTrue(line.isOn(PolarCoordinate.create(10, Fraction.ZERO)));
		assertTrue(line.isOn(PolarCoordinate.create(11, Fraction.ZERO)));
		assertTrue(line.isOn(PolarCoordinate.create(50, Fraction.ZERO)));
		assertTrue(line.isOn(PolarCoordinate.create(99, Fraction.ZERO)));
		assertTrue(line.isOn(PolarCoordinate.create(110, Fraction.ZERO)));

		assertFalse(line.isOn(PolarCoordinate.create(9, Fraction.ZERO)));
		assertFalse(line.isOn(PolarCoordinate.create(111, Fraction.ZERO)));
		
		assertFalse(line.isOn(PolarCoordinate.create(10, Fraction.ONE)));
		assertFalse(line.isOn(PolarCoordinate.create(11, Fraction.ONE)));
		assertFalse(line.isOn(PolarCoordinate.create(50, Fraction.ONE)));
		assertFalse(line.isOn(PolarCoordinate.create(99, Fraction.ONE)));
		assertFalse(line.isOn(PolarCoordinate.create(110, Fraction.ONE)));
	}
	
	@Test
	public void canMerge() {
		Line line = Line.create(PolarCoordinate.create(10, Fraction.ZERO), 110);
		
		assertTrue(line.canMerge(Line.create(PolarCoordinate.create(10, Fraction.ZERO), 110)));
		assertTrue(line.canMerge(Line.create(PolarCoordinate.create(10, Fraction.ZERO), 90)));
		assertTrue(line.canMerge(Line.create(PolarCoordinate.create(0, Fraction.ZERO), 90)));
		assertTrue(line.canMerge(Line.create(PolarCoordinate.create(0, Fraction.ZERO), 120)));
		assertTrue(line.canMerge(Line.create(PolarCoordinate.create(10, Fraction.ZERO), 120)));
		
		assertFalse(line.canMerge(Line.create(PolarCoordinate.create(10, Fraction.valueOf(1)), 110)));
		assertFalse(line.canMerge(Line.create(PolarCoordinate.create(10, Fraction.valueOf(1)), 90)));
		assertFalse(line.canMerge(Line.create(PolarCoordinate.create(0, Fraction.valueOf(1)), 90)));
		assertFalse(line.canMerge(Line.create(PolarCoordinate.create(0, Fraction.valueOf(1)), 120)));
		assertFalse(line.canMerge(Line.create(PolarCoordinate.create(10, Fraction.valueOf(1)), 120)));
		
		assertFalse(line.canMerge(Line.create(PolarCoordinate.create(120, Fraction.ZERO), 110)));
		
		assertTrue(line.canMerge(Line.create(PolarCoordinate.create(50, Fraction.ZERO), 300)));
	}
	
	@Test
	public void merge() {
		Line line = Line.create(PolarCoordinate.create(10, Fraction.ZERO), 110);
		
		assertEquals(line, line.merge(line));
		assertEquals(Line.create(PolarCoordinate.create(10, Fraction.ZERO), 120), line.merge(Line.create(PolarCoordinate.create(100, Fraction.ZERO), 120)));
		assertEquals(Line.create(PolarCoordinate.create(0, Fraction.ZERO), 110), line.merge(Line.create(PolarCoordinate.create(0, Fraction.ZERO), 80)));
		
		assertEquals(Line.create(PolarCoordinate.create(10, Fraction.ZERO), 300), line.merge(Line.create(PolarCoordinate.create(50, Fraction.ZERO), 300)));
	}
	
	@Test
	public void equalsContract() {
	    EqualsVerifier.forClass(Line.class)
	    		.suppress(Warning.NULL_FIELDS)
	    		.verify();
	}
}
