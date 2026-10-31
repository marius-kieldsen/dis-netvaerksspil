package netvaerksspil.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket spiller = new ServerSocket(6789);
		while (true) {
			// Lad nye spillere forbinde
			// (Spiltilstand skal sendes med så det kan opdateres fra spillertråd)
			Socket connectionSocket = spiller.accept();
			(new ServerThread(connectionSocket)).start();
		}
	}
}
