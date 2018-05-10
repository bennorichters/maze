package org.bnor.maze.visual;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

@SuppressWarnings("static-method")
public class CircleTest {
	
	@Test
	public void equalsContract() {
	    EqualsVerifier.forClass(Circle.class)
	    		.suppress(Warning.NULL_FIELDS)
	    		.verify();
	}
}
