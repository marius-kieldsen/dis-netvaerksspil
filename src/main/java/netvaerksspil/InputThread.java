package netvaerksspil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputThread extends Thread {
    private BufferedReader inFromServer = null;
    List<Player> newPlayers = new ArrayList<>();

    public InputThread(BufferedReader inFromServer) {
        this.inFromServer = inFromServer;
    }

    public void run() {
        while (true) {
            try {
                String input = inFromServer.readLine();
                if (input != null) {

                    for (Player old : newPlayers) {
                        Gui.removePlayerOnScreen(old.getLocation());
                        Gui.resetTiles();
                    }
                    newPlayers = DataFormatter.parseData(input);

                    for (Player p : newPlayers) {
                        Gui.placePlayerOnScreen(p.getLocation(), p.getDirection());
                        for (Tile tile : p.getClaimedTiles()) {
                            Gui.updateTileColor(tile.getX(), tile.getY(), p.getColor().getColor());
                        }
                    };

                    Gui.updateScoreTable(newPlayers);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
