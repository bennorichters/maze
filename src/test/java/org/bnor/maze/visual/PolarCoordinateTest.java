package org.bnor.maze.visual;

import org.bnor.maze.visual.PolarCoordinate;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

@SuppressWarnings("static-method")
public class PolarCoordinateTest {
	
	@Test
	public void equalsContract() {
	    EqualsVerifier.forClass(PolarCoordinate.class)
	    		.suppress(Warning.NULL_FIELDS)
	    		.verify();
	}
}
