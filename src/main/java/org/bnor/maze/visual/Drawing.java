package org.bnor.maze.visual;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

final class Drawing {

	private final Set<Mergeable<Line>> lines = new HashSet<>();
	private final Set<Mergeable<Arc>> arcs = new HashSet<>();
	private final Set<Circle> circles = new HashSet<>();
	
	void addLine(Line toAdd) {
		merge(lines, toAdd);
	}

	void addArc(Arc toAdd) {
		merge(arcs, toAdd);
	}

	void addCircle(Circle toAdd) {
		circles.add(toAdd);
	}
	
	private static <T> void merge(Set<Mergeable<T>> elements, Mergeable<T> toAdd) {
		Mergeable<T> candidate = toAdd;
		Mergeable<T> toMerge = findElementToMerge(elements, candidate);
		while (toMerge != null) {
			elements.remove(toMerge);
			candidate = candidate.merge(toMerge) ;
			toMerge = findElementToMerge(elements, candidate);
		}
		elements.add(candidate);
	}

	private static <T> Mergeable<T> findElementToMerge(Set<Mergeable<T>> elements, Mergeable<T> toAdd) {
		for (Mergeable<T> element : elements) {
			if (element.canMerge(toAdd)) {
				return element;
			}
		}
		
		return null;
	}

	public Set<Line> getLines() {
		return lines.stream()
				.map(s -> (Line) s)
				.collect(Collectors.toSet());
	}

	public Set<Arc> getArcs() {
		return arcs.stream()
				.map(s -> (Arc) s)
				.collect(Collectors.toSet());
	}

	public Set<Circle> getCircles() {
		return new HashSet<>(circles);
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		
		result = prime * result + lines.hashCode();
		result = prime * result + arcs.hashCode();
		result = prime * result + circles.hashCode();
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Drawing)) {
			return false;
		}
		
		Drawing other = (Drawing) obj;
		return lines.equals(other.lines) && arcs.equals(other.arcs) && circles.equals(other.circles);
	}

	@Override
	public String toString() {
		return "Drawing [lines=" + lines + ", arcs=" + arcs + ", circles=" + circles + "]";
	}
}
