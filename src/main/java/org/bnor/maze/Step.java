package org.bnor.maze;

import java.util.HashSet;
import java.util.Set;

final class Step {

	enum StepType {
		ARC, LINE
	}
	
	private final CircleCoordinate to;
	private final StepType type;
	private final CircleCoordinate boundaryToCross;
	
	Step(CircleCoordinate to, StepType type, CircleCoordinate boundaryToCross) {
		this.to = to;
		this.type = type;
		this.boundaryToCross = boundaryToCross;
	}

	static Set<Step> allFor(CircleCoordinate from) {		
		Set<Step> result = new HashSet<>();

		for (Direction direction : Direction.ALL) {
			result.addAll(direction.possibleSteps(from));
		}

		return result;
	}

	CircleCoordinate getTo() {
		return to;
	}

	StepType getType() {
		return type;
	}

	CircleCoordinate getBoundaryToCross() {
		return boundaryToCross;
	}

	@Override
	public String toString() {
		return getTo() + ", " + getType() + ", " + getBoundaryToCross();
	}
}
