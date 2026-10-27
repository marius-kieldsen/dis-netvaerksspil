package netvaerksspil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputThread extends Thread {
    private BufferedReader inFromServer = null;
    List<Player> oldPlayers = new ArrayList<>();

    public InputThread(BufferedReader inFromServer) {
        this.inFromServer = inFromServer;
    }

    public void run() {
        while (true) {
            try {
                String input = inFromServer.readLine();
                if (input != null) {

                    if (input.equals("ESC")) {
                        return;
                    }

                    List<Player> newPlayers = DataFormatter.parseData(input);

                    for (Player old : oldPlayers) {
                        Gui.removePlayerOnScreen(old.getLocation());
                    }

                    for (Player p : newPlayers) {
                        Gui.placePlayerOnScreen(p.getLocation(), p.getDirection());
                    };

                    Gui.updateScoreTable(newPlayers);

                    oldPlayers = newPlayers;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
