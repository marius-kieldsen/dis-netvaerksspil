package netvaerksspil;

import java.io.*;
import java.net.Socket;

import javafx.application.Application;

public class App {
	public static DataOutputStream outToServer = null;
	public static InputThread inputThread;

	public static void main(String[] args) throws Exception{
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Indtast spillernavn");
		String navn = inFromUser.readLine();
		Socket clientSocket= new Socket("localhost",6789);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		outToServer.writeBytes(navn + "\n");
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		inputThread = new InputThread(inFromServer);
		inputThread.setDaemon(true);

		Application.launch(Gui.class);
	}
}