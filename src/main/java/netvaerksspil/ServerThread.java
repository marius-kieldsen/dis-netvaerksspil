package netvaerksspil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
public class ServerThread extends Thread{
	Socket connSocket;
	GameLogic game;
	DataOutputStream outToClient;
	Player connectedPlayer;
	
	public ServerThread(Socket connSocket) {
		this.connSocket = connSocket;
	}
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			outToClient = new DataOutputStream(connSocket.getOutputStream());
			String playername = inFromClient.readLine();
			connectedPlayer = GameLogic.makePlayers(playername);
			outToClient.writeBytes(DataFormatter.packData(game.players));

			System.out.println("Player connected: " + playername);

			String input = "";
			while (!connSocket.isClosed()) {
				if (!input.isEmpty()) {

					String[] newPlayerPos = input.split("#");
					GameLogic.updatePlayer(connectedPlayer, Direction.valueOf(newPlayerPos[0]));
				}
				outToClient.writeBytes(DataFormatter.packData(GameLogic.players));
				outToClient.flush();

				if (inFromClient.ready()) {
					input = inFromClient.readLine();
				} else {
					input = "";
				}
				Thread.sleep(10);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
			// Hvis spilleren forlader
            try {
				GameLogic.players.remove(connectedPlayer);
                outToClient.writeBytes("ESC" + "\n");
				GameLogic.freeColors.push(connectedPlayer.getColor());
				connSocket.close();
            } catch (IOException e) {}
		}
    }
}
