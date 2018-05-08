package org.bnor.maze;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public final class MazeFactory {

	private final int circles;

	private final Set<CircleCoordinate> free;
	private final Set<CircleCoordinate> closedArcs;
	private final Set<CircleCoordinate> closedLines;

	MazeFactory(int circles) {
		this.circles = circles;
		this.free = allCoordinates(circles);
		this.closedArcs = new HashSet<>();
		this.closedLines = new HashSet<>();
	}

	private static Set<CircleCoordinate> allCoordinates(int circles) {
		Set<CircleCoordinate> result = new HashSet<>();
		for (int circle = 1; circle <= circles; circle++) {
			result.addAll(coordinatesForCircle(circle));
		}

		return result;
	}

	private static Set<CircleCoordinate> coordinatesForCircle(int circle) {
		Set<CircleCoordinate> result = new HashSet<>();
		for (int arc = 0; arc < CircleCoordinate.calcTotalArcs(circle); arc++) {
			result.add(CircleCoordinate.create(circle, arc));
		}

		return result;
	}

	Maze createClosedOuter() {
		outerClosed();
		finish();

		return new Maze(circles, closedArcs, closedLines);
	}
	
	Maze createTraditional() {
		outerCircleWIthRandomGap();
		firstBoundaryFromInnerToOuter();
		finish();
		
		return new Maze(circles, closedArcs, closedLines);
	}

	private void outerCircleWIthRandomGap() {
		free.removeAll(circleWithRandomGap(circles));
	}

	private void firstBoundaryFromInnerToOuter() {
		CircleCoordinate start = randomOnInnerCircle();
		Direction direction = LineDirection.OUT;
		CircleCoordinate next = direction.boundaryNeighbour(start);
		closedLines.add(start);

		new Boundary(innerCircle()).create(next);
	}

	private Set<CircleCoordinate> innerCircle() {
		return circleWithRandomGap(1);
	}

	private void finish() {
		while (!free.isEmpty()) {
			new Boundary().create(free.iterator().next());
		}
	}

	private static CircleCoordinate randomOnInnerCircle() {
		int calcTotalArcs = CircleCoordinate.calcTotalArcs(1);
		int arc = ThreadLocalRandom.current().nextInt(0, calcTotalArcs);
		return CircleCoordinate.create(1, arc);
	}

	private Set<CircleCoordinate> circleWithRandomGap(int circle) {
		Set<CircleCoordinate> used = new HashSet<>();

		int totalArcs = CircleCoordinate.calcTotalArcs(circle);
		int gap = ThreadLocalRandom.current().nextInt(0, totalArcs);

		for (int i = 0; i < totalArcs; i++) {
			CircleCoordinate coordinate = CircleCoordinate.create(circle, i);
			if (i != gap) {
				closedArcs.add(coordinate);
			}

			used.add(coordinate);
		}

		return used;
	}

	private void outerClosed() {
		for (int i = 0; i < CircleCoordinate.calcTotalArcs(circles); i++) {
			CircleCoordinate coordinate = CircleCoordinate.create(circles, i);
			closedArcs.add(coordinate);
			free.remove(coordinate);
		}
	}
	

	private final class Boundary {
		private final Set<CircleCoordinate> used;
		private final Set<CircleCoordinate> deadEnds;

		Boundary() {
			this(new HashSet<>());
		}

		Boundary(Set<CircleCoordinate> used) {
			this.used = used;
			this.deadEnds = new HashSet<>();
		}

		void create(CircleCoordinate start) {
			CircleCoordinate next = start;
			while (free.contains(next)) {
				used.add(next);
				next = findNext(next, possibleDirections(next));
			}

			free.removeAll(used);
		}

		private CircleCoordinate findNext(CircleCoordinate current, List<Direction> options) {
			if (options.isEmpty()) {
				deadEnds.add(current);
				return findPlaceToFork();
			} 

			Direction direction = chooseRandom(options);
			direction.close(current, closedArcs, closedLines);
			return direction.boundaryNeighbour(current);
		}

		private CircleCoordinate findPlaceToFork() {
			Set<CircleCoordinate> options = new HashSet<>(used);
			options.removeAll(deadEnds);

			return options.stream()
					.max((c1, c2) -> c1.getCircle() - c2.getCircle())
					.get();
		}
		
		private Direction chooseRandom(List<Direction> options) {
			return options.get(ThreadLocalRandom.current().nextInt(0, options.size()));
		}

		private List<Direction> possibleDirections(CircleCoordinate coordinate) {
			return Direction.ALL.stream()
					.filter(d -> d.hasBoundaryNeighbour(coordinate) && !used.contains(d.boundaryNeighbour(coordinate)))
					.collect(Collectors.toList());
		}
	}
}
