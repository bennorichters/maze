package org.bnor.maze;

import java.util.HashSet;
import java.util.Set;

import org.bnor.maze.Step.StepType;

public enum ArcDirection implements Direction {
	CLOCKWISE, COUNTERCLOCKWISE;
	
	@Override
	public Set<Step> possibleSteps(CircleCoordinate from) {
		Set<Step> result = new HashSet<>();

		if (from.equals(CircleCoordinate.ORIGIN)) {
			return result;
		}
		
		CircleCoordinate to = boundaryNeighbour(from);
		result.add(new Step(to, StepType.ARC, this == ArcDirection.CLOCKWISE ? to : from));
		
		return result;
	}

	@Override
	public boolean hasBoundaryNeighbour(CircleCoordinate coordinate) {
		return true;
	}

	@Override
	public CircleCoordinate boundaryNeighbour(CircleCoordinate coordinate) {
		int circle = coordinate.getCircle();
		int arcIndex = coordinate.getArcIndex();
		int totalArcs = CircleCoordinate.calcTotalArcs(coordinate.getCircle());
		
		return (this == CLOCKWISE) ?
				CircleCoordinate.create(circle, (arcIndex == totalArcs - 1) ? 0 : arcIndex + 1) :
				CircleCoordinate.create(circle, (arcIndex == 0) ? totalArcs - 1 : arcIndex - 1); 
	}

	@Override
	public void close(CircleCoordinate coordinate, Set<CircleCoordinate> closedArcs, Set<CircleCoordinate> closedLines) {
		CircleCoordinate toClose = (this == CLOCKWISE) ? coordinate : COUNTERCLOCKWISE.boundaryNeighbour(coordinate);
		
		closedArcs.add(toClose);
	}
}
