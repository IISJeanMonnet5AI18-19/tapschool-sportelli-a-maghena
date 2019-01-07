/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author maggioni_matteo00
 */
public class TCPServer {
    byte[] bufferIN;
    InetAddress IPClient;
    int portClient;

    String fragment, message;
    int n;
    ServerSocket serverSocket;
    Socket socket;
    OutputStreamWriter out;
    InputStream in;

    public void GestioneSocketServer() throws SocketException, IOException {
        serverSocket = new ServerSocket(6789);
        bufferIN = new byte[1024];

        socket = serverSocket.accept();

        out = new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1");
        in = socket.getInputStream();

    }

    public String riceviServer() throws IOException {

        message = "";
        boolean fine = false;
        while (!fine && ((n = in.read(bufferIN)) != -1)) {
            fragment = new String(bufferIN, 0, n, "ISO-8859-1");
            if (fragment.endsWith("\n")) {
                fine = true;
            }
            message = message + fragment.substring(0, fragment.length() - 1);
        }
        System.out.println("RICEVUTO " + message);
        return message;
    }

    public void inviaServer(String daInviare) throws IOException {
        System.out.println("INVIATO " + daInviare);
        out.write(daInviare + "\n");
        out.flush();
    }

    public void chiudiServer() throws IOException {
        out.close();
        in.close();
        socket.close();
        serverSocket.close();
    }
}
