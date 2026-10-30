package netvaerksspil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputThread extends Thread {
    private BufferedReader inFromServer = null;
    List<Player> playersFromServer = new ArrayList<>();

    public InputThread(BufferedReader inFromServer) {
        this.inFromServer = inFromServer;
    }

    public void run() {
        while (true) {
            try {
                String input = inFromServer.readLine();
                if (input != null) {
                    playersFromServer = DataFormatter.parseData(input);
                    Gui.renderNew(playersFromServer);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
