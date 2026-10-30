package netvaerksspil;

public class Player {
	private final String name;
//	private final PlayerColor color;
	private Tile location;
	private Direction direction;
	private int point;

	public Player(String name, int points, Tile location, Direction direction) {
		this.name = name;
        this.location = location;
		this.direction = direction;
		this.point = points;
	}

//	public PlayerColor getColor() {
//		return color;
//	}

	public String getName() { return name; }

	public int getPoint() { return point; }

	public Tile getLocation() {
		return this.location;
	}

	public void setLocation(Tile tile) {
		this.location = tile;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void addPoints(int p) {
		point+=p;
	}

	public String toString() {
		return name+":   "+point;
	}


}
