package org.bnor.maze.visual;

import org.bnor.euler.Fraction;

public interface Canvas {

	void circle(PolarCoordinate center, int diameter);
	void line(PolarCoordinate from, int length);
	void arc(PolarCoordinate from, Fraction span);
}
