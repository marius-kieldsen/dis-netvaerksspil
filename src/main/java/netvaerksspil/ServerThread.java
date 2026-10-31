package netvaerksspil;

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
			GameLogic.registerObserver(this);
			connectedPlayer = GameLogic.makePlayers(playername);
			System.out.println("Player connected: " + playername);

			String input = "";
			while (!connSocket.isClosed()) {
				if (!input.isEmpty()) {
					GameLogic.updatePlayer(connectedPlayer, Direction.valueOf(input));
				}

				if (inFromClient.ready()) {
					input = inFromClient.readLine();
				} else {
					input = "";
				}
				Thread.sleep(10);
			}
		} catch (IOException e) {} catch (InterruptedException e) {} finally {
			// Hvis spilleren forlader
            try {
				GameLogic.players.remove(connectedPlayer);
				GameLogic.freeColors.push(connectedPlayer.getColor());
				System.out.println("Player left: " + playername);
				GameLogic.removeObserver(this);
				connSocket.close();
            } catch (IOException e) {}
		}
    }

	public void send() {
		try {
			outToClient.writeBytes(DataFormatter.packData(GameLogic.players));
			outToClient.flush();
		} catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
