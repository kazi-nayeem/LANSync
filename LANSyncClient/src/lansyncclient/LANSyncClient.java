/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansyncclient;

import java.io.IOException;

/**
 *
 * @author Kazi
 */
public class LANSyncClient {

    public final static int SOCKET_PORT = 13267;      // you may change this
    public static String SERVER;  // localhost
    // you may change this, I give a
    // different name because i don't want to
    // overwrite the one used by server...
    public static String FILE_DEFAULT_PATH = "C:\\LANSync\\client\\";

    public static void main(String[] args) throws IOException {
        LoginFrame.main(args);
    }
}
