package org.bnor.maze.visual;

public interface Mergeable<T> {
	
	boolean isOn(PolarCoordinate point);
	boolean canMerge(Mergeable<T> other);
	Mergeable<T> merge(Mergeable<T> other);
}
