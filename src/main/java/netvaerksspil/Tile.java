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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TileType getType() {
        return type;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        if (this.owner != owner) {
            Player oldOwner = this.owner;
            if (oldOwner != null) {
                oldOwner.removeTile(this);
            }
            this.owner = owner;
            if (owner != null) {
                owner.claimTile(this);
            }
        }
    }

}
