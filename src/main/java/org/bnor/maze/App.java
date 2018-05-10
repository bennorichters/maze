package org.bnor.maze;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.bnor.maze.visual.Drawer;
import org.bnor.maze.visual.DrawerOnCanvas;
import org.bnor.maze.visual.GraphicsCanvas;

public final class App {

	public static void main(String[] args) {
		go();
	}

	private static void go() {
		int circles = 40;
		int distance = 12;
		int size = 2 * circles * distance + 10;

		Maze maze = new MazeFactory(circles).createClosedOuter();
		System.out.println("Maze created");
		
		// System.out.println(MazeJson.serialize(maze));

		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics = image.createGraphics();

		graphics.setPaint(Color.WHITE);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

		graphics.setPaint(Color.BLACK);

		Drawer drawer = new DrawerOnCanvas(distance, new GraphicsCanvas(size / 2, graphics));
		drawer.drawMaze(maze);
		System.out.println("Draw maze finished");


		Set<List<CircleCoordinate>> paths = new MazeSolver(maze).maxPaths();
		System.out.println("Max paths calculated.");
		
		drawAllPaths(maze, graphics, drawer, paths.iterator(), distance);
		drawEndPoints(graphics, paths.iterator().next(), drawer);
		
		System.out.println("Draw paths finished");

		try {
			ImageIO.write(image, "PNG", new File("d:\\temp\\maze.png"));
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	private static void drawEndPoints(Graphics2D graphics, List<CircleCoordinate> path, Drawer drawer) {
		graphics.setColor(Color.BLUE);
		
		CircleCoordinate start = path.get(0);
		CircleCoordinate end = path.get(path.size() - 1);
		
		drawer.drawDotOnPath(start);
		drawer.drawDotOnPath(end);
	}

	private static void drawAllPaths(Maze maze, Graphics2D graphics, Drawer drawer, Iterator<List<CircleCoordinate>> paths, int distance) {
		graphics.setStroke(new BasicStroke(distance / 2));
		Iterator<Color> colors = Set.of(Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK, Color.MAGENTA).iterator();

		while (paths.hasNext() && colors.hasNext()) {
			List<CircleCoordinate> path = paths.next();
			Color color = colors.next();

			graphics.setPaint(color);
			CircleCoordinate start = path.get(0);
			CircleCoordinate end = path.get(path.size() - 1);

			drawer.drawPath(new MazeSolver(maze).solve(start, end));
		}
	}
}
