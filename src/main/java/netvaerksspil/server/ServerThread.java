package netvaerksspil.server;

import netvaerksspil.DataFormatter;
import netvaerksspil.Direction;
import netvaerksspil.Player;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread{
	Socket connSocket;
	DataOutputStream outToClient;
	Player connectedPlayer;
	String playername;
	
	public ServerThread(Socket connSocket) throws IOException {
		this.connSocket = connSocket;
		outToClient = new DataOutputStream(connSocket.getOutputStream());
	}
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			playername = inFromClient.readLine();
			// En ny spiller tilføjes og spillet og timeren genstartes
			connectedPlayer = GameLogic.makePlayers(playername);
			System.out.println("Player connected: " + playername);

			String input = "";
			while (input != null) {
				if (!input.isEmpty()) {
					GameLogic.updatePlayer(connectedPlayer, Direction.valueOf(input));
				}

				// Udregn hvor meget tid der er tilbage i spillet
				int secondsPassed = (int) ((System.nanoTime() - GameLogic.timer) / 1_000_000_000);
				int timeLeft = 90 - secondsPassed;

				outToClient.writeBytes(DataFormatter.packData(GameLogic.players, timeLeft));
				outToClient.flush();

				if (inFromClient.ready()) {
					input = inFromClient.readLine();
				} else {
					input = "";
				}

				// Smid spiller af hvis tiden er gået
				if (timeLeft <= 0) {
					input = null;
					connSocket.close();
				}

				Thread.sleep(4);
			}
		} catch (IOException | InterruptedException | IllegalArgumentException ignored) {}
		finally {
			// Hvis spilleren forlader
			if (connectedPlayer != null) {
				GameLogic.players.remove(connectedPlayer);
				GameLogic.freeColors.push(connectedPlayer.getColor());
				System.out.println("Player left: " + playername);
			}
            try {
				connSocket.close();
            } catch (IOException ignored) {}
		}
    }
}
