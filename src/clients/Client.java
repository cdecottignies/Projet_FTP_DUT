package clients;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
	//PI
    private static ClientPI cltPI;

	public static void main(String[] args) throws IOException {
        try {
            if (args.length >= 2 && isNumeric(args[1])) {
                cltPI = new ClientPI(args[0], Integer.parseInt(args[1]));
            } else {
                cltPI = new ClientPI();
            }
            cltPI.go();
        } catch (Exception e) {
			System.out.println(e);
		}
	}

	private static boolean isNumeric(String value) { 
		try {  
			Integer.parseInt(value);  
			return true;
		} catch(NumberFormatException e){  
			return false;  
		}  
	}
}