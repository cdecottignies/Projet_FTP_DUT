package serveurs;

import java.net.*;
import java.util.ArrayList;
public class Serveur {
	public static String host;
	private static ServerSocket serverSocket;
	private static ArrayList<ServeurPI> serveursPI;
	
	public static void main(String[] args) throws Exception {
		serveursPI = new ArrayList<ServeurPI>();
		serverSocket = new ServerSocket(2121);
		System.out.println(">>> starting FTP server on 0.0.0.0:" + serverSocket.getLocalPort() + " <<<");
		System.out.println("passive ports: no restriction");
		while (true) {
			Socket socket = serverSocket.accept();
			ServeurPI servPI = new ServeurPI(socket);
			serveursPI.add(servPI);
			servPI.start();
		}
	}
}