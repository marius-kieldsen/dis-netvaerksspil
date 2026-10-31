package netvaerksspil;

import java.util.*;

public class GameLogic {
	public static List<Player> players = new ArrayList<>();
	private static final Random RANDOM = new Random();
	public static final Deque<PlayerColor> freeColors = new ArrayDeque<>();
	private static List<ServerThread> observers = new ArrayList<>();

	static {
		PlayerColor[] allColors = PlayerColor.values();
		for (int i = allColors.length - 1; i >= 0 ; i--) {
			freeColors.push(allColors[i]);
		}
	}

	public static void registerObserver(ServerThread observer) {
		observers.add(observer);
	}

	public static void removeObserver(ServerThread observer) {
		observers.remove(observer);
	}

	public static void notifyObservers() {
		for (ServerThread observer : observers) {
			observer.send();
		}
	}

	public synchronized static Player makePlayers(String name) {
		if(freeColors.isEmpty()) throw new IllegalStateException("Spillet er fuldt!!!");

		PlayerColor color = freeColors.pop();
		Tile startPosition = getRandomFreeTile();
		Player player = new Player(name, color, startPosition, Direction.UP);
		players.add(player);
		player.claimTile(startPosition);

		notifyObservers();

		return player;
	}

	// finds a random new position which is not wall
	// and not occupied by other players
	public synchronized static Tile getRandomFreeTile() {
		while (true) {
			int x = 1 + RANDOM.nextInt(GameMap.MAP_WIDTH - 2);
			int y = 1 + RANDOM.nextInt(GameMap.MAP_HEIGHT - 2);
			Tile t = GameMap.tileMap[x][y];
			if (t.getType() == TileType.FLOOR && getPlayerAt(t) == null) {
				return t;
			}
		}
	}
	
	public synchronized static void updatePlayer(Player player, Direction direction) {
		player.setDirection(direction);

		Tile oldLocation = player.getLocation();
		Tile newLocation = GameMap.tileMap[oldLocation.getX() + direction.getDx()]
										  [oldLocation.getY() + direction.getDy()];

		if (newLocation.getType() == TileType.WALL) {
			return;
		} else {
			player.claimTile(newLocation);
		}

		Player otherPlayer = getPlayerAt(newLocation);
		if (otherPlayer != null) {
			Tile respawn = getRandomFreeTile();
			otherPlayer.setLocation(respawn);
			otherPlayer.resetClaimedTiles();
		}

		player.setLocation(newLocation);
		notifyObservers();
	}
	
	public synchronized static Player getPlayerAt(Tile tile) {
		for (Player p : players) {
			if (p.getLocation() == tile) {
				return p;
			}
		}
		return null;
	}
}
