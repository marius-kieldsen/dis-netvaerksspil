package netvaerksspil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
public class ServerThread extends Thread{
	Socket connSocket;
	GameLogic game;
	
	public ServerThread(Socket connSocket) {
		this.connSocket = connSocket;

	}
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());
			String playername = inFromClient.readLine();
			Player me = GameLogic.makePlayers(playername);
			outToClient.writeBytes(DataFormatter.packData(game.players));

			System.out.println("Player connected: " + playername);

			String input = "";
			while (true) {
				if (!input.isEmpty()) {

					// Tøm input buffer (For at sørge for at server kun reagerer på 1 opdatering pr ms)
					while (inFromClient.ready()) {
						inFromClient.readLine();
					}
					// Hvis spilleren forlader
					if (input.equals("ESC")) {
						GameLogic.players.remove(me);
						outToClient.writeBytes("ESC" + "\n");
						connSocket.close();
					} else {
						String[] newPlayerPos = input.split("#");
						GameLogic.updatePlayer(me, Direction.valueOf(newPlayerPos[0]));
					}
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
        }
    }
}
