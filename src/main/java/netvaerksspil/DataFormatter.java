package netvaerksspil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DataFormatter {

    // Serveren pakker spillets data til en streng (Som bruges til at sende til klienten)
    public static String packData(List<Player> players) {
        synchronized (GameLogic.class) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Player player : players) {
                stringBuilder
                        .append(player.getName()).append("#")
                        .append(player.getColor().name()).append("#")
                        .append(player.getLocation().getX()).append("#")
                        .append(player.getLocation().getY()).append("#")
                        .append(player.getDirection()).append("#")
                        .append(claimedTilesString(player.getClaimedTiles())).append(".");
            }
            stringBuilder.append("\n");
            return stringBuilder.toString();
        }
    }

    // Bruges til at pakke data ud som klienten modtager fra serveren.
    public static List<Player> parseData(String input) {
        List<Player> newPlayers = new ArrayList<>();
        for (String playerData : input.split("\\.")) {
            if (!playerData.isBlank()) {
                String[] fields = playerData.split("#");
                String name = fields[0];
                PlayerColor color = PlayerColor.valueOf(fields[1]);
                int x = Integer.parseInt(fields[2]);
                int y = Integer.parseInt(fields[3]);
                String direction = fields[4];
                String tilesString = fields[5];


                Player player = new Player(name, color, GameMap.tileMap[x][y], Direction.valueOf(direction));
                player.setClaimedTiles(claimedTilesToSet(tilesString, player));
                newPlayers.add(player);
            };
        }
        return newPlayers;
    }

    public static String claimedTilesString(HashSet<Tile> set) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(":");
        for (Tile t : set) {
            stringBuilder.append(t.getX()).append("-").append(t.getY()).append("$");
        }
        stringBuilder.append(":");
        return stringBuilder.toString();

    }

    public static HashSet<Tile> claimedTilesToSet(String claimedTilesString, Player player) {
        HashSet<Tile> claimedTilesSet = new HashSet<>();
        int length = claimedTilesString.length();
        if (length > 2) {
            claimedTilesString = claimedTilesString.substring(1, length -1);
            String[] split1 = claimedTilesString.split("\\$");
            for(String s : split1) {
                String[] split2 = s.split("-");
                int x = Integer.parseInt(split2[0]);
                int y = Integer.parseInt(split2[1]);
                claimedTilesSet.add(new Tile(x, y, TileType.FLOOR, player));
            }
        }
        return claimedTilesSet;
    }
}
