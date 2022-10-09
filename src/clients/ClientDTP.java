package clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientDTP {
	private Socket mySocket;
	private PrintWriter out;
	private BufferedReader in;
	private String ip;
	private InetAddress ipIA;
	private int port;
	
	public ClientDTP(String ip, int port) throws IOException {
		mySocket = new Socket(ip, port);
		this.ip = ip;
		this.port = port;
		initialiser();
	}
	
	public ClientDTP(InetAddress ipIA, int port) throws IOException {
		mySocket = new Socket(ip, port);
		this.ipIA = ipIA;
		this.port = port;
		initialiser();
	}

	private void initialiser() throws IOException {
		out = new PrintWriter(mySocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
	}

	private void send() throws IOException {
		//à remplir
		close();
	}

	private void receive() throws IOException {
		//à remplir
		close();
	}

	private void close() throws IOException {
		out.close();
		mySocket.close();
	}
}