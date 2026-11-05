package netvaerksspil.klient;

import netvaerksspil.DataFormatter;
import netvaerksspil.Player;

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
        boolean getInput = true;
        while (getInput) {
            try {
                String input = inFromServer.readLine();
                if (input != null) {
                    DataFormatter dataFromServer = DataFormatter.parseData(input);
                    playersFromServer = dataFromServer.getPlayers();
                    int timeLeft = dataFromServer.getTimer();
                    Gui.renderNew(playersFromServer, timeLeft);
                    if (timeLeft <= 0) {
                        getInput = false;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
