package org.bnor.maze.visual;

public interface DrawElement<T> {
	
	boolean isOn(PolarCoordinate point);
	boolean canMerge(DrawElement<T> other);
	DrawElement<T> merge(DrawElement<T> other);
}
