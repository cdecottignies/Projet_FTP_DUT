package clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientPI {
	//constantes
	final static int PORT = 2121;
	final static String IP = "localhost";
	//PI
	private Socket mySocket;
	private PrintWriter out;
	private BufferedReader in;
	private BufferedReader local;
	private String ip;
	private InetAddress ipIA;
	private int port;
	//DTP
	private ClientDTP cltDTP;

	public ClientPI(String ip, int port) throws IOException {
		this.ip = ip;
		this.port = port;
		mySocket = new Socket(this.ip, this.port);
		initialiser();
	}
	
	public ClientPI(InetAddress ip, int port) throws IOException {
		this.ipIA = ip;
		this.port = port;
		mySocket = new Socket(this.ipIA, this.port);
		initialiser();
	}

	public ClientPI() throws IOException {
		this(IP, PORT);
	}

	private void initialiser() throws IOException {
        in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
		out = new PrintWriter(mySocket.getOutputStream(), true);
		local = new BufferedReader(new InputStreamReader(System.in));
	}

	public void go() throws IOException {
		String input;
		do {
			input = local.readLine();
			send(input);
			printResponse();
		} while(!input.toUpperCase().equals("QUIT"));
		send("quit");
		close();
	}

	private void send(String command) throws IOException { out.println(command); }

	private void printResponse() throws IOException {
		do{
			System.out.println(in.readLine());
		}while(in.ready());
		}
	

	private String receive() throws IOException {
		return in.readLine();
	}
	

	private void close() throws IOException {
		out.close();
		mySocket.close();
	}
}