package org.bnor.maze.visual;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.function.Consumer;

import org.bnor.euler.Fraction;
import org.bnor.maze.CircleCoordinate;
import org.bnor.maze.Maze;
import org.bnor.maze.MazeJson;
import org.bnor.maze.MazeSolver;
import org.junit.Test;

@SuppressWarnings("static-method")
public class DrawerOnCanvasTest {

	@Test
	public void drawMazes() {
		drawMazeForResource("maze_3_1.json");
		drawMazeForResource("maze_40_1.json");
	}

	private void drawMazeForResource(String resource) {
		Maze maze = MazeJson.deserialize(MazeJsonReader.read(resource));

		referenceEqualsActual(s -> s.drawMaze(maze));
	}

	@Test
	public void drawPaths() {
		drawPathForResource("maze_3_1.json");
		drawPathForResource("maze_40_1.json");
	}

	private void drawPathForResource(String resource) {
		Maze maze = MazeJson.deserialize(MazeJsonReader.read(resource));
		List<CircleCoordinate> path = new MazeSolver(maze).maxPaths().iterator().next();
		
		referenceEqualsActual(s -> s.drawPath(path));
	}

	@Test
	public void drawCircles() {
		CircleCoordinate center = CircleCoordinate.create(4, 2);
		
		referenceEqualsActual(s -> s.drawDotOnPath(center));
	}
	
	private void referenceEqualsActual(Consumer<Drawer> consumer) {
		Drawing drawingReference = new Drawing();
		Drawer  drawerReference = new OldAndUglyButWorkingReferenceDrawer(25, new DrawingMaker(drawingReference));
		consumer.accept(drawerReference);
		
		Drawing drawingActual = new Drawing();
		Drawer  drawerActual = new DrawerOnCanvas(25, new DrawingMaker(drawingActual));
		consumer.accept(drawerActual);
		
		assertEquals(drawingReference, drawingActual);
	}
	
	private static final class DrawingMaker implements Canvas {

		final Drawing drawing;

		DrawingMaker(Drawing drawing) {
			this.drawing = drawing;
		}

		@Override
		public void line(PolarCoordinate from, int length) {
			drawing.addLine(Line.create(from, from.getRadius() + length));
		}

		@Override
		public void circle(PolarCoordinate center, int diameter) {
			drawing.addCircle(Circle.create(center, diameter));
		}

		@Override
		public void arc(PolarCoordinate from, Fraction span) {
			drawing.addArc(Arc.create(from, span));
		}
	}
}
