package netvaerksspil;

public class GameMap {
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

	public static final int MAP_WIDTH = board[0].length();
	public static final int MAP_HEIGHT = board.length;
	public static final Tile[][] tileMap = new Tile[MAP_WIDTH][MAP_HEIGHT];

	static {
		buildMap();
	}

	public static void buildMap() {
		for (int row = 0; row < MAP_HEIGHT; row++) {
			for (int column = 0; column < MAP_WIDTH; column++) {
				char c = board[row].charAt(column);
				TileType type = switch (c) {
					case 'w' -> TileType.WALL;
					case ' ' -> TileType.FLOOR;
					default -> throw new IllegalStateException("Ukendt tegn '" + c + "' på " + column + "," + row);
				};
				tileMap[column][row] = new Tile(column, row, type, null);
			}
		}
	}
}
