package netvaerksspil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Gui extends Application {

	public static final int TILE_SIZE = 30;
	public static final int SCENE_HEIGHT = TILE_SIZE * GameMap.MAP_HEIGHT + 50;
	public static final int SCENE_WIDTH = TILE_SIZE * GameMap.MAP_WIDTH + 200;

	public static Image imageFloor;
	public static Image imageWall;

	private static final Map<Direction, Image> heroImages = new EnumMap<>(Direction.class);

	private static Label[][] tiles;
	private static TextArea scoreList;
	


	
	// -------------------------------------------
	// | Maze: (0,0)              | Score: (1,0) |
	// |-----------------------------------------|
	// | boardGrid (0,1)          | scorelist    |
	// |                          | (1,1)        |
	// -------------------------------------------

	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(0, 10, 0, 10));

			Text mazeLabel = new Text("Maze:");
			mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	
			Text scoreLabel = new Text("Score:");
			scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			scoreList = new TextArea();
			
			GridPane boardGrid = new GridPane();

			imageWall  = loadImage("/images/wall4.png");
			imageFloor = loadImage("/images/floor1.png");

			heroImages.put(Direction.RIGHT, loadImage("/images/heroRight.png"));
			heroImages.put(Direction.LEFT, loadImage("/images/heroLeft.png"));
			heroImages.put(Direction.UP, loadImage("/images/heroUp.png"));
			heroImages.put(Direction.DOWN, loadImage("/images/heroDown.png"));


			// Bygger map
			tiles = new Label[GameMap.MAP_WIDTH][GameMap.MAP_HEIGHT];
			for (int row = 0; row < GameMap.MAP_HEIGHT; row++) {
				for (int column = 0; column < GameMap.MAP_WIDTH; column++) {
					Image img = switch (GameMap.tileMap[column][row].getType()) {
						case WALL -> imageWall;
						case FLOOR -> imageFloor;
					};
					tiles[column][row] = new Label("", new ImageView(img));
					boardGrid.add(tiles[column][row], column, row);
				}
			}
			scoreList.setEditable(false);
			
			
			grid.add(mazeLabel,  0, 0); 
			grid.add(scoreLabel, 1, 0); 
			grid.add(boardGrid,  0, 1);
			grid.add(scoreList,  1, 1);
						
			Scene scene = new Scene(grid, SCENE_WIDTH, SCENE_HEIGHT);
			primaryStage.setScene(scene);
			primaryStage.show();

			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				switch (event.getCode()) {
				case UP     -> playerMoved(Direction.UP);
				case DOWN   -> playerMoved(Direction.DOWN);
				case LEFT   -> playerMoved(Direction.LEFT);
				case RIGHT  -> playerMoved(Direction.RIGHT);
				case ESCAPE -> System.exit(0);
				default  	-> { }
				}
			});

			// Startes først når mappet er loadet
			App.inputThread.start();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateTileColor(int column, int row, int tilecolor) {
		ImageView view = (ImageView) tiles[column][row].getGraphic();
//		Player owner = GameMap.tileMap[column][row].getOwner();
//
//		if (owner == null) {
//			view.setEffect(null);
//			return;
//		}

//		int rgb = owner.getColor().getColor();
		Image img = view.getImage();

		ColorInput tint = new ColorInput(
				0, 0, img.getWidth(), img.getHeight(),
				Color.rgb((tilecolor >> 16) & 0xff, (tilecolor >> 8) & 0xff, tilecolor & 0xff) // ikke spørg mig hvordan det virker..
		);
		view.setEffect(new Blend(BlendMode.MULTIPLY, null, tint));
	}

	public void playerMoved(Direction direction) {
		try {
			App.outToServer.writeBytes(direction.toString() + "\n");
			App.outToServer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public static void removePlayerOnScreen(Tile tile) {
		Platform.runLater(() -> {
			tiles[tile.getX()][tile.getY()].setGraphic(new ImageView(imageFloor));
		});
	}

	public static void placePlayerOnScreen(Tile tile,Direction direction) {
		Platform.runLater(() -> {
			tiles[tile.getX()][tile.getY()].setGraphic(new ImageView(heroImages.get(direction)));
		});
	}

	public void playerLeft() {
		try {
			App.outToServer.writeBytes("ESC" + "\n");
			App.outToServer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void updateScoreTable(List<Player> playerList)
	{
		Platform.runLater(() -> {
			scoreList.setText(getScoreList(playerList));
		});
	}

	public static String getScoreList(List<Player> playerList) {
		StringBuffer b = new StringBuffer(100);
		for (Player p : playerList) {
			b.append(p+"\r\n");
		}
		return b.toString();
	}

	private Image loadImage(String path) {
		return new Image(getClass().getResourceAsStream(path), TILE_SIZE, TILE_SIZE, false, false);
	}
}

