/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansyncserver;

import java.io.IOException;

/**
 *
 * @author Kazi
 */
public class LANSyncServer {

    /**
     * @param args the command line arguments
     */
    public final static int SOCKET_PORT = 13267;  // you may change this
    public static String FILE_DEFAULT_PATH = "C:\\LANSync\\server\\";
    public static Server smsserver;

    public static void main(String[] args) throws IOException {
        smsserver = new Server(5060);
    }
}
