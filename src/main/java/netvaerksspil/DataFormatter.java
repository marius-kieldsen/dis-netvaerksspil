package netvaerksspil;

import java.util.ArrayList;
import java.util.List;

public class DataFormatter {
    public static String packData(List<Player> players) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Player player : players) {
            stringBuilder.append(player.getName()).append("#").append(player.getColor().name()).append("#").append(player.getPoint()).append("#").append(player.getLocation().getX()).append("#").append(player.getLocation().getY()).append("#").append(player.getDirection()).append(".");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public static List<Player> parseData(String input) {
        List<Player> newPlayers = new ArrayList<>();
        for (String playerData : input.split("\\.")) {
            if (!playerData.isBlank()) {
                String[] fields = playerData.split("#");
                String name = fields[0];
                PlayerColor color = PlayerColor.valueOf(fields[1]);
                int points = Integer.parseInt(fields[2]);
                int x = Integer.parseInt(fields[3]);
                int y = Integer.parseInt(fields[4]);
                String direction = fields[5];

                Player player = new Player(name, color, points, GameMap.tileMap[x][y], Direction.valueOf(direction));
                newPlayers.add(player);
            };
        }
        return newPlayers;
    }
}
