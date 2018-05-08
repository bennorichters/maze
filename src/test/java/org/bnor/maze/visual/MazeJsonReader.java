package org.bnor.maze.visual;

final class MazeJsonReader {

	static String read(String resource) {
		try {
			java.net.URL url = MazeJsonReader.class.getResource(resource);
			java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());

			return new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
		} catch (Exception e) {
			throw new RuntimeException("Error reading resource: " + resource, e);
		}
	}
}
