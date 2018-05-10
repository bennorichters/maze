package org.bnor.maze.visual;

import java.util.List;

import org.bnor.maze.CircleCoordinate;
import org.bnor.maze.Maze;

public interface Drawer {
	void drawMaze(Maze maze);
	void drawPath(List<CircleCoordinate> path);
	void drawDotOnPath(CircleCoordinate coordinate);
}
