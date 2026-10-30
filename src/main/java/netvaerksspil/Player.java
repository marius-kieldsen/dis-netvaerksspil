package netvaerksspil;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class Player {
	private final String name;
	private final PlayerColor color;
	private Tile location;
	private HashSet<Tile> claimedTiles;
	private Direction direction;
	private int point;

	public Player(String name, PlayerColor color, int points, Tile location, Direction direction) {
		this.name = name;
        this.color = color;
        this.location = location;
		this.direction = direction;
		this.point = points;
		claimedTiles = new HashSet<>();
	}

	public void claimTile(Tile tile) {
		claimedTiles.add(tile);
		tile.setOwner(this);
	}

	public void removeTile(Tile tile) {
		claimedTiles.remove(tile);
		tile.setOwner(null);
	}

	public HashSet<Tile> getClaimedTiles() {
		return claimedTiles;
	}

	public void setClaimedTiles(HashSet<Tile> claimedTiles) {
		this.claimedTiles = claimedTiles;
	}


	public PlayerColor getColor() {
		return color;
	}

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
