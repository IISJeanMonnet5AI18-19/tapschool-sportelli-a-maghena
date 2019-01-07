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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author maggioni_matteo00
 */
public class TCPClient {
    byte[] bufferIN;

    String fragment, message;
    int n;
    int portaServer;
    InetAddress IPServer;
    Socket socket;
    OutputStreamWriter out;
    InputStream in;

    public void GestioneSocketClient() throws SocketException, UnknownHostException, IOException {
        portaServer = 6789;
        IPServer = InetAddress.getByName("localhost");
        bufferIN = new byte[1024];

        socket = new Socket();
        InetSocketAddress serverAddress = new InetSocketAddress(IPServer, portaServer);
        socket.connect(serverAddress);

        out = new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1");
        in = socket.getInputStream();

    }

    public void inviaClient(String daInviare) throws IOException {
        System.out.println("INVIATO "+daInviare);
        out.write(daInviare+"\n");
        out.flush();

    }

    public String riceviClient() throws IOException {

        message = "";
        boolean fine = false;
        while (!fine && ((n = in.read(bufferIN)) != -1)) {
            fragment = new String(bufferIN, 0, n, "ISO-8859-1");
            if (fragment.endsWith("\n")) {
                fine = true;
            }
            message = message + fragment.substring(0, fragment.length() - 1);
        }
        System.out.println("RICEVUTO "+message);
        return message;
    }

    public void chiudiClient() throws IOException {
        out.close();
        in.close();
        socket.close();
    }

    void inviaClient(byte[] bufferOUT) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
