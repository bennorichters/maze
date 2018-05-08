package org.bnor.maze;

import java.util.Set;

interface Direction {
	
	final static Set<Direction> ALL = Set.of(
			LineDirection.IN, LineDirection.OUT, ArcDirection.CLOCKWISE, ArcDirection.COUNTERCLOCKWISE);

	Set<Step> possibleSteps(CircleCoordinate coordinate);

	boolean hasBoundaryNeighbour(CircleCoordinate coordinate);
	
	CircleCoordinate boundaryNeighbour(CircleCoordinate coordinate);

	void close(CircleCoordinate next, Set<CircleCoordinate> closedArcs, Set<CircleCoordinate> closedLines);
}
