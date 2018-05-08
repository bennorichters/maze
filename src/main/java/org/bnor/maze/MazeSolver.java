package org.bnor.maze;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bnor.maze.Step.StepType;

public final class MazeSolver {

	private final Maze maze;

	public MazeSolver(Maze maze) {
		this.maze = maze;
	}

	public List<CircleCoordinate> solve(CircleCoordinate start, CircleCoordinate end) {
		LinkedList<CircleCoordinate> result = new LinkedList<>();
		result.add(start);

		Deque<State> deque = new ArrayDeque<>();
		State state = new State(start, accessibleNeighbours(start, result));
		deque.add(state);
		while (!state.current.equals(end)) {
			if (state.it.hasNext()) {
				CircleCoordinate to = state.it.next();
				result.add(to);
				state = new State(to, accessibleNeighbours(to, result));
				deque.addLast(state);
			} else {
				result.removeLast();
				deque.removeLast();
				state = deque.getLast();
			}
		}

		return result;
	}

	public Set<List<CircleCoordinate>> maxPaths() {
		CircleCoordinate deadEnd = findDeadEnd();
		Set<List<CircleCoordinate>> paths = maxPathsForStart(deadEnd);
		
		int maxDist = -1;
		Set<List<CircleCoordinate>> result = new HashSet<>();
		for (List<CircleCoordinate> path : paths) {
			Set<List<CircleCoordinate>> maxPaths = maxPathsForStart(path.get(path.size() - 1));

			Iterator<List<CircleCoordinate>> it = maxPaths.iterator();

			List<CircleCoordinate> candidate = it.next();
			int dist = candidate.size();
			if (dist >= maxDist) {
				if (dist > maxDist) {
					maxDist = dist;
					result.clear();
				}

				result.add(new ArrayList<>(candidate));
			}
		}

		return result;
	}

	public Set<List<CircleCoordinate>> maxPathsForStart(CircleCoordinate start) {
		Set<List<CircleCoordinate>> result = new HashSet<>();

		LinkedList<CircleCoordinate> path = new LinkedList<>();

		path.add(start);

		int maxDist = -1;

		Deque<State> deque = new ArrayDeque<>();
		State state = new State(start, accessibleNeighbours(start, path));
		deque.add(state);
		while (!deque.isEmpty()) {
			if (state.it.hasNext()) {
				CircleCoordinate to = state.it.next();
				path.add(to);
				state = new State(to, accessibleNeighbours(to, path));
				deque.addLast(state);
			} else {
				int dist = path.size();

				if (dist >= maxDist) {
					if (dist > maxDist) {
						maxDist = dist;
						result.clear();
					}

					result.add(new ArrayList<>(path));
				}

				path.removeLast();
				deque.removeLast();

				state = deque.peekLast();
			}
		}

		return result;
	}

	private CircleCoordinate findDeadEnd() {
		CircleCoordinate current = CircleCoordinate.ORIGIN;

		LinkedList<CircleCoordinate> visited = new LinkedList<>();
		visited.add(current);

		Iterator<CircleCoordinate> it = accessibleNeighbours(current, visited);
		while (it.hasNext()) {
			current = it.next();
			visited.add(current);
			it = accessibleNeighbours(current, visited);
		}

		return current;
	}

	Iterator<CircleCoordinate> accessibleNeighbours(CircleCoordinate current, List<CircleCoordinate> visited) {
		return Step.allFor(current).stream()
				.filter(step -> !visited.contains(step.getTo()) &&
						((step.getType() == StepType.LINE) && (maze.isArcOpen(step.getBoundaryToCross())) ||
								(step.getType() == StepType.ARC) && (maze.isLineOpen(step.getBoundaryToCross()))))
				.map(s -> s.getTo())
				.iterator();
	}

	private static final class State {
		final CircleCoordinate current;
		final Iterator<CircleCoordinate> it;

		State(CircleCoordinate current, Iterator<CircleCoordinate> it) {
			this.current = current;
			this.it = it;
		}
	}
}
