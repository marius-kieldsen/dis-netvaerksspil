package netvaerksspil;

public class Tile {
    private final int x, y;     // placering
    private TileType type;      // WALL, FLOOR
    private Player owner;       // null = ingen spiller har farvet den endnu

    public Tile(int x, int y, TileType type, Player owner) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.owner = null;
    }

    public void claim(Player p) {
        owner = p;
    }

    public boolean isClaimed() {
        return owner != null;
    }

    public Player getOwner() {
        return owner;
    }


}
