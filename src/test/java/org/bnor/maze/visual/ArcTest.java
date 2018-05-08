package org.bnor.maze.visual;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bnor.euler.Fraction;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

@SuppressWarnings("static-method")
public class ArcTest {

	@Test
	public void fullCircle() {
		Arc arc = create(100, 350, 360);
		
		assertEquals(PolarCoordinate.create(100, Fraction.ZERO), arc.getFrom());
		assertEquals(Fraction.valueOf(360), arc.getSpan());
		assertEquals(Fraction.ZERO, arc.getEndAngle());
	}

	@Test
	public void isOn_simple() {
		PolarCoordinate point = PolarCoordinate.create(100, Fraction.valueOf(45));
		Arc arc = Arc.create(point, Fraction.valueOf(90));

		assertTrue(arc.isOn(PolarCoordinate.create(100, Fraction.valueOf(45))));
		assertTrue(arc.isOn(PolarCoordinate.create(100, Fraction.valueOf(50))));
		assertTrue(arc.isOn(PolarCoordinate.create(100, Fraction.valueOf(89))));
		assertTrue(arc.isOn(PolarCoordinate.create(100, Fraction.valueOf(90))));
		assertTrue(arc.isOn(PolarCoordinate.create(100, Fraction.valueOf(91))));
		assertTrue(arc.isOn(PolarCoordinate.create(100, Fraction.valueOf(135))));

		assertFalse(arc.isOn(PolarCoordinate.create(101, Fraction.valueOf(45))));
		assertFalse(arc.isOn(PolarCoordinate.create(101, Fraction.valueOf(50))));
		assertFalse(arc.isOn(PolarCoordinate.create(101, Fraction.valueOf(89))));
		assertFalse(arc.isOn(PolarCoordinate.create(101, Fraction.valueOf(90))));
		assertFalse(arc.isOn(PolarCoordinate.create(101, Fraction.valueOf(91))));
		assertFalse(arc.isOn(PolarCoordinate.create(101, Fraction.valueOf(135))));
	}
	
	@Test
	public void isOn_accross306() {
		PolarCoordinate point = PolarCoordinate.create(100, Fraction.valueOf(350));
		Arc arc = Arc.create(point, Fraction.valueOf(20));
		
		assertTrue(arc.isOn(PolarCoordinate.create(100, Fraction.valueOf(0))));
		assertTrue(arc.isOn(PolarCoordinate.create(100, Fraction.valueOf(10))));
		assertTrue(arc.isOn(PolarCoordinate.create(100, Fraction.valueOf(350))));
		assertTrue(arc.isOn(PolarCoordinate.create(100, Fraction.valueOf(355))));

		assertFalse(arc.isOn(PolarCoordinate.create(100, Fraction.valueOf(11))));
		assertFalse(arc.isOn(PolarCoordinate.create(100, Fraction.valueOf(349))));
	}
	
	@Test
	public void canMerge() {
		assertFalse(create(100, 0, 50).canMerge(create(100, 60, 10)));
		assertFalse(create(100, 0, 50).canMerge(create(99, 10, 10)));
		
		assertFalse(create(100, 350, 20).canMerge(create(100, 15, 10)));
	}
	
	@Test
	public void merge_simple1() {
		Arc arc1 = create(100, 100, 20);
		Arc arc2 = create(100, 110, 20);
		
		Arc merge = arc1.merge(arc2);
		assertTrue(merge.equals(arc2.merge(arc1)));
		assertEquals(Fraction.valueOf(100), merge.getFrom().getAngle());
		assertEquals(100, merge.getFrom().getRadius());
		assertEquals(Fraction.valueOf(130), merge.getEndAngle());
	}
	
	@Test
	public void merge_simple2() {
		Arc arc1 = create(100, 10, 10);
		Arc arc2 = create(100, 15, 20);
		
		Arc merge = arc1.merge(arc2);
		assertTrue(merge.equals(arc2.merge(arc1)));
		assertEquals(100, merge.getFrom().getRadius());
		assertEquals(Fraction.valueOf(10), merge.getFrom().getAngle());
		assertEquals(Fraction.valueOf(35), merge.getEndAngle());
	}
	
	@Test
	public void merge_throughZero1() {
		Arc arc1 = create(100, 350, 20);
		Arc arc2 = create(100, 355, 20);
		
		Arc merge = arc1.merge(arc2);
		assertTrue(merge.equals(arc2.merge(arc1)));
		assertEquals(Fraction.valueOf(350), merge.getFrom().getAngle());
		assertEquals(100, merge.getFrom().getRadius());
		assertEquals(Fraction.valueOf(15), merge.getEndAngle());
	}

	@Test
	public void merge_throughZero2() {
		Arc arc1 = create(100, 350, 20);
		Arc arc2 = create(100, 5, 30);
		
		Arc merge = arc1.merge(arc2);
		assertTrue(merge.equals(arc2.merge(arc1)));
		assertEquals(Fraction.valueOf(350), merge.getFrom().getAngle());
		assertEquals(100, merge.getFrom().getRadius());
		assertEquals(Fraction.valueOf(35), merge.getEndAngle());
	}
	
	@Test
	public void merge_overlap() {
		Arc arc1 = create(100, 10, 20);
		Arc arc2 = create(100, 15, 5);
		
		Arc merge = arc1.merge(arc2);
		assertTrue(merge.equals(arc2.merge(arc1)));
		assertTrue(merge.equals(arc1));
	}
	
	@Test
	public void merge_fullCircle() {
		Arc arc1 = create(100, 10, 20);
		Arc arc2 = create(100, 30, 340);
		
		Arc merge = arc1.merge(arc2);
		assertTrue(merge.equals(arc2.merge(arc1)));
		
		assertEquals(Fraction.valueOf(0), merge.getFrom().getAngle());
		assertEquals(100, merge.getFrom().getRadius());
		assertEquals(Fraction.valueOf(0), merge.getEndAngle());
	}
	
	@Test
	public void equalsContract() {
	    EqualsVerifier.forClass(Arc.class)
	    		.withIgnoredFields("span", "throughZero")
	    		.suppress(Warning.NULL_FIELDS)
	    		.verify();
	}
	
	private static Arc create(int radius, int startAngle, int span) {
		return Arc.create(PolarCoordinate.create(radius, Fraction.valueOf(startAngle)), Fraction.valueOf(span));
	}
}
