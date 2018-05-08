package org.bnor.maze;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Maze {

	private final int circles;
	private final Set<CircleCoordinate> closedArcs;
	private final Set<CircleCoordinate> closedLines;

	Maze(int circles, Set<CircleCoordinate> closedArcs, Set<CircleCoordinate> closedLines) {
		this.circles = circles;
		this.closedArcs = Collections.unmodifiableSet(new HashSet<>(closedArcs));
		this.closedLines = Collections.unmodifiableSet(new HashSet<>(closedLines));
	}

	public int getCircles() {
		return circles;
	}

	public boolean isArcOpen(CircleCoordinate coordinate) {
		return !closedArcs.contains(coordinate);
	}

	public boolean isLineOpen(CircleCoordinate coordinate) {
		return !closedLines.contains(coordinate);
	}
	
	public CircleCoordinate gapInOuterCircle() {
		for (int arc = 0; arc < CircleCoordinate.calcTotalArcs(circles); arc++) {
			CircleCoordinate candidate = CircleCoordinate.create(circles, arc);
			if (isArcOpen(candidate)) {
				return candidate;
			}
		}
		
		return null;
	}
}
