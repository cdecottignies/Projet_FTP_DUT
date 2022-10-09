package serveurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServeurPI extends Thread{
    static private List<String> withParam = Arrays.asList("USER", "PASS", "PORT", "RETR", "STOR");
    static private List<String> withoutParam = Arrays.asList("QUIT", "PASV", "LIST", "HELP");
    static public Map<String,String> users;

    private Socket socket;
    private String user, input, output, pending;
    private boolean authentified;
    private BufferedReader in;
    private PrintWriter out;
    private ServeurDTP serveurDTP;

    public ServeurPI(Socket socket) {
        this.socket = socket;
        this.serveurDTP = null;
        this.user = null;
        this.pending = null;
        this.authentified = false;
        if(users==null) initialize();;
    }

    static void initialize() {
        users = new HashMap<>();
        users.put("anonymous", "toto");
        users.put("adam", "shanghai");
        users.put("clement", "brawlhalla");
        users.put("carle", "reseau42");
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            input = null;
            while (true) {
                if(input == null || (!input.toUpperCase().equals("PASV") && !input.toUpperCase().startsWith("PORT")) || pending==null) input = in.readLine();
                else {
                    input = pending;
                    pending = null;
                }
                if(input.length()<3 || (!(withParam.contains(input.substring(0,3).toUpperCase()) || withParam.contains(input.substring(0,4).toUpperCase())) && !withoutParam.contains(input.toUpperCase()))) {
                    out.println("500 Command \"" + input + "\" not understood.");
                } else if(input.toUpperCase().equals("QUIT")) {
                    output = "221 Goodbye.";
                    out.println(output);
                    localPrint("FTP session closed (disconnect).");
                    break;
                } else if(input.toUpperCase().equals("HELP")) {
                    output = "214-The following commands are recognized:\n";
                    for (String command : withoutParam) output += command + "\t";
                    output += "\n";
                    for (String command : withParam) output += command + "\t";
                    output += "\n214 Help command successful.";
                    out.println(output);
                } else if(input.length()<=5 && withParam.contains(input.toUpperCase())) {
                    output = "501 Syntax error: command needs an argument.";
                    out.println(output);
                    localPrint(output);
                } else if(input.toUpperCase().startsWith("USER")) {
                    user = input.substring(5);
                    output = "331 Username ok, send password.";
                    out.println(output);
                } else if(input.toUpperCase().startsWith("PASS")) {
                    if(user==null) {
                        output = "503 Login with USER first.";
                    } else {
                        String pswd = users.get(user);
                        authentified = false;
                        if(!user.toUpperCase().equals("ANONYMOUS") && (pswd==null || !input.substring(5).equals(pswd))) {
                            localPrint("USER '" + user + "' failed login.");
                            output = "530 Authentication failed.";
                            user = null;
                        } else {
                            authentified = true;
                            output = "230 Login successful.";
                            localPrint("USER '" + user + "' logged in.");
                        }
                    }
                    out.println(output);
                } else if(!authentified) {
                    output = "530 Log in with USER and PASS first.";
                    out.println(output);
                } else if(input.toUpperCase().startsWith("PORT")) {
                    output = "tien ton port \n";
                    out.println(output);
                    localPrint(output);
                } else if(input.toUpperCase().equals("PASV")) {
                    serveurDTP = new ServeurDTP();
                    //lancer le 2eme serveur
                    output = "227 Entering passive mode "+ serveurDTP.getadress();
                    //227 Entering passive mode (127,0,0,1,244,170).
                    localPrint("ServeurDTP: "+serveurDTP.getadress());
                    out.println(output);
                    serveurDTP.connection();
                } else if(input.toUpperCase().equals("LIST")) {
                    if(serveurDTP!=null) {
                        output = "125 Data connection already open. Transfer starting.";
                        serveurDTP.list();
                        //serveurDTP.close();
                        serveurDTP = null;
                        out.println(output);
                        output = "226 Transfer complete.";
                        pending = null;
                    } else {
                        output = "150 File status okay. About to open data connection.";
                        pending = input;
                    }
                    out.println(output);
                } else if(input.toUpperCase().equals("PWD")) {
                    if(serveurDTP!=null) {
                        output = "125 Data connection already open. Transfer starting.";
                        serveurDTP.PWD();
                        //serveurDTP.close();
                        serveurDTP = null;
                        out.println(output);
                        output = "226 Transfer complete.";
                        pending = null;
                    } else {
                        output = "150 File status okay. About to open data connection.";
                        pending = input;
                    }
                    out.println(output);
                } else if(input.toUpperCase().equals("CWD")) {
                    if(serveurDTP!=null) {
                        output = "125 Data connection already open. Transfer starting.";
                        serveurDTP.CWD();
                        //serveurDTP.close();
                        serveurDTP = null;
                        out.println(output);
                        output = "226 Transfer complete.";
                        pending = null;
                    } else {
                        output = "150 File status okay. About to open data connection.";
                        pending = input;
                    }
                    out.println(output);
                } else {
                    localPrint("Erreur de commande");
                    out.println("500 Command \"" + input + "\" not understood.");
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void localPrint(String message) {
        System.out.println(socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "-[" + (authentified?user:"") + "] " + message);
    }
}