/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansyncserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author masum
 */
public class Server {

    private ServerSocket serverSocket = null;
    private Socket connection = null;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;

    public Server(int port) {
        Thread t = null;
        t = new Thread(new Runnable() {

            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                listen(port);
            }
        });
        t.start();
    }

    private void listen(int port) {
        if (readyServerSocket(port) == false) {
            return;
        }
        while (true) {
            try {
                displayMessage("\nWaiting for new client \n ip -> " + getIPAddress() + "\n port -> " + port);

                connection = serverSocket.accept();

                System.out.println("connected" + connection);

                input = new ObjectInputStream(connection.getInputStream());
                output = new ObjectOutputStream(connection.getOutputStream());
                output.flush();
                System.out.println("accoted");

                recieveMessage();
                
            } catch (IOException ex) {
                //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeConnection();
            }

        }
    }

    private boolean readyServerSocket(int port) {
        try {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

            serverSocket = new ServerSocket(port, 100);
            return true;
        } catch (IOException ex) {
            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    private String getIPAddress() {
        try {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException ex) {
            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return "127.0.0.1";
        }
    }

    private void displayMessage(String string) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                System.err.println(string);
            }

        });
    }

    private void closeConnection() {
        try {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            if (connection != null) {
                connection.close();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        } catch (IOException ex) {
            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void recieveMessage() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String msg = null;
        do {
            try {
                msg = (String) input.readObject();
                displayMessage("\nCLIENT >> " + msg);
                HandleProcess.handleProcess(msg);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        } while (!msg.equals("EXIT"));
    }

    void sendMessage(String msg) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        try {
            displayMessage("\nSERVER >> " + msg);
            output.writeObject(msg);
            output.flush();

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
