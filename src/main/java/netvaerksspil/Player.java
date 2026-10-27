package netvaerksspil;

public class Player {
	private final String name;
//	private final PlayerColor color;
	private Tile location;
	private Direction direction;
	private int point;

	public Player(String name, Tile location, Direction direction) {
		this.name = name;
        this.location = location;
		this.direction = direction;
		this.point = 0;
	};

//	public PlayerColor getColor() {
//		return color;
//	}

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
