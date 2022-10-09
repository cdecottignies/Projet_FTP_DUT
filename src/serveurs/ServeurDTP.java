package serveurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurDTP {
	private ServerSocket serveurSocket;
	Socket unClient;

	public ServeurDTP(int port) {
		try {
			// Cr�ation de la Socket Serveur qui permettra d'attendre les connexions
			serveurSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public ServeurDTP() {
		try {
			// Cr�ation de la Socket Serveur qui permettra d'attendre les connexions
			serveurSocket = new ServerSocket(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public String getadress() {
		return " " + serveurSocket.getLocalSocketAddress().toString();
	}

	public boolean isBound() {
		return serveurSocket.isBound();
	}

	public void list() {
		try {
			PrintWriter envoyer = new PrintWriter(unClient.getOutputStream(), true);
			Process p = Runtime.getRuntime().exec("ls -l");
        	BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        	String line;
			envoyer.println("la list fonctionne !");

			while ((line = br.readLine()) != null) {
				envoyer.println(line);
			}
    

			envoyer.close();
			unClient.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public void PWD() {
		try {
			PrintWriter envoyer = new PrintWriter(unClient.getOutputStream(), true);
			Process p = Runtime.getRuntime().exec("pwd");
        	BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        	String line;
			envoyer.println("PWD fonctionne !");
			while ((line = br.readLine()) != null) {
				envoyer.println(line);
			}

			envoyer.close();
			unClient.close();
		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public void CWD() {
		try {
			PrintWriter envoyer = new PrintWriter(unClient.getOutputStream(), true);
			Process p = Runtime.getRuntime().exec("ls -l");
        	BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        	String line;
			envoyer.println("CWD fonctionne !");

			while ((line = br.readLine()) != null) {
				envoyer.println(line);
			}

			envoyer.close();
			unClient.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public void connection() {
		unClient = null;
		try {
			serveurSocket.setSoTimeout(20000);
			unClient = serveurSocket.accept();
		} catch (IOException e) {
			System.out.println("Socket Connexion Timed Out");
		}
	}
}