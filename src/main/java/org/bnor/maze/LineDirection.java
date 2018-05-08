package org.bnor.maze;

import java.util.HashSet;
import java.util.Set;

import org.bnor.maze.Step.StepType;

enum LineDirection implements Direction {
	IN, OUT;

	@Override
	public Set<Step> possibleSteps(CircleCoordinate from) {
		Set<Step> result = new HashSet<>();

		int circle = from.getCircle();
		int arcIndex = from.getArcIndex();

		if (circle == 0) {
			if (this != IN) {
				for (int arc = 0; arc < CircleCoordinate.calcTotalArcs(0); arc++) {
					CircleCoordinate to = CircleCoordinate.create(1, arc);
					result.add(new Step(to, StepType.LINE, to));
				}
			}
		} else if ((circle == 1) && (this == IN)) {
			result.add(new Step(CircleCoordinate.ORIGIN, StepType.LINE, from));
		} else if (this == IN) {
			if (smallerCircleHasSameNumberOfArcs(circle) || smallerCircleHasSameAngle(arcIndex)) {
				CircleCoordinate to = CircleCoordinate.create(circle - 1, from.getAngle());
				result.add(new Step(to, StepType.LINE, from));
			} else {
				CircleCoordinate to = CircleCoordinate.create(circle - 1, arcIndex / 2);
				result.add(new Step(to, StepType.LINE, from));
			}
		} else {
			if (smallerCircleHasSameNumberOfArcs(circle + 1)) {
				CircleCoordinate to = CircleCoordinate.create(circle + 1, arcIndex);
				result.add(new Step(to, StepType.LINE, to));
			} else {
				CircleCoordinate to1 = CircleCoordinate.create(circle + 1, arcIndex * 2);
				result.add(new Step(to1, StepType.LINE, to1));
				CircleCoordinate to2 = CircleCoordinate.create(circle + 1, arcIndex * 2 + 1);
				result.add(new Step(to2, StepType.LINE, to2));
			}
		}

		return result;
	}

	@Override
	public boolean hasBoundaryNeighbour(CircleCoordinate coordinate) {
		if (this == OUT) {
			return true;
		}

		int circle = coordinate.getCircle();
		int arcIndex = coordinate.getArcIndex();

		return notSmallestCircle(circle) && (smallerCircleHasSameNumberOfArcs(circle) || smallerCircleHasSameAngle(arcIndex));
	}

	private static boolean smallerCircleHasSameAngle(int arcIndex) {
		return arcIndex % 2 == 0;
	}

	private static boolean smallerCircleHasSameNumberOfArcs(int circle) {
		return !isPowerOf2(circle);
	}

	private static boolean notSmallestCircle(int circle) {
		return circle > 1;
	}

	private static boolean isPowerOf2(int n) {
		return (n & (n - 1)) == 0;
	}

	@Override
	public CircleCoordinate boundaryNeighbour(CircleCoordinate coordinate) {
		int neighbourCircle = coordinate.getCircle() + ((this == IN) ? -1 : 1);

		return CircleCoordinate.create(neighbourCircle, coordinate.getAngle());
	}

	@Override
	public void close(CircleCoordinate coordinate, Set<CircleCoordinate> closedArcs, Set<CircleCoordinate> closedLines) {
		CircleCoordinate toClose = (this == OUT) ? coordinate : boundaryNeighbour(coordinate);
		closedLines.add(toClose);
	}
}
