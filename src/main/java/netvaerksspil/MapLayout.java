package netvaerksspil;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class MapLayout {
	public static String[] board = {    // 20x20
			"wwwwwwwwwwwwwwwwwwww",
			"w        ww        w",
			"w w  w  www w  w  ww",
			"w w  w   ww w  w  ww",
			"w  w               w",
			"w w w w w w w  w  ww",
			"w w     www w  w  ww",
			"w w     w w w  w  ww",
			"w   w w  w  w  w   w",
			"w     w  w  w  w   w",
			"w ww ww        w  ww",
			"w  w w    w    w  ww",
			"w        ww w  w  ww",
			"w         w w  w  ww",
			"w        w     w  ww",
			"w  w              ww",
			"w  w www  w w  ww ww",
			"w w      ww w     ww",
			"w   w   ww  w      w",
			"wwwwwwwwwwwwwwwwwwww"
	};

	public static final int mapWidth = board[0].length();
	public static final int mapHeight = board.length;
	public static final Tile[][] tileMap = new Tile[mapWidth][mapHeight];

	static {
		buildMap();
	}

	public static void buildMap() {
		for (int row = 0; row < mapHeight; row++) {
			for (int column = 0; column < mapWidth; column++) {
				switch (board[row].charAt(column)) {
					case 'w':
						tileMap[column][row] = new Tile(column, row, TileType.WALL, null);
						break;
					case ' ':
						tileMap[column][row] = new Tile(column, row, TileType.FLOOR, null);
						break;
				}
			}
		}
	}
}
