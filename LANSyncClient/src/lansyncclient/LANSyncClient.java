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
    public final static String SERVER = "192.168.0.150";  // localhost
    public final static String FILE_TO_RECEIVED = "D:\\LANSync\\client\\source-downloaded.zip";  // you may change this, I give a
    // different name because i don't want to
    // overwrite the one used by server...
    public final static int FILE_SIZE = 7641498;
    public static String FILE_DEFAULT_PATH = "D:\\LANSync\\client\\";

    public static void main(String[] args) throws IOException {
        LoginFrame.main(args);
    }
}
