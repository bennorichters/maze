package org.bnor.maze.visual;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

final class Drawing {

	private final Set<DrawElement<Line>> lines = new HashSet<>();
	private final Set<DrawElement<Arc>> arcs = new HashSet<>();
	
	void addLine(Line toAdd) {
		merge(lines, toAdd);
	}

	void addArc(Arc toAdd) {
		merge(arcs, toAdd);
	}

	private static <T> void merge(Set<DrawElement<T>> elements, DrawElement<T> toAdd) {
		DrawElement<T> candidate = toAdd;
		DrawElement<T> toMerge = findElementToMerge(elements, candidate);
		while (toMerge != null) {
			elements.remove(toMerge);
			candidate = candidate.merge(toMerge) ;
			toMerge = findElementToMerge(elements, candidate);
		}
		elements.add(candidate);
	}

	private static <T> DrawElement<T> findElementToMerge(Set<DrawElement<T>> elements, DrawElement<T> toAdd) {
		for (DrawElement<T> element : elements) {
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

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		
		result = prime * result + getLines().hashCode();
		result = prime * result + getArcs().hashCode();
		
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
		return getLines().equals(other.getLines()) && getArcs().equals(other.getArcs());
	}

	@Override
	public String toString() {
		return "Drawing [lines=" + getLines() + ", arcs=" + getArcs() + "]";
	}
}
