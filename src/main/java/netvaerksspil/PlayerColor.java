package netvaerksspil;

public enum PlayerColor {
    RED(0xf54254),
    LIGHTBLUE(0x11c0f5),
    GREEN(0x92f511),
    YELLOW(0xf5b411),
    MAGENTA(0xf105f5);

    private final int color;
    PlayerColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}