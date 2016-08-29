/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansyncclient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import lansyncserver.FileInfo;

/**
 *
 * @author Kazi
 */
public class ReceiveFile {

    final int SOCKET_PORT;      // you may change this
    final String SERVER;  // localhost
    final String FILE_TO_RECEIVED;  // you may change this, I give a
    // different name because i don't want to
    // overwrite the one used by server...

    final long FILE_SIZE, LAST_MODIFIED_DATE; // file size temporary hard coded
    // should bigger than the file to be downloaded

    public ReceiveFile(int Server_Socket_Port, String Server_IP, FileInfo file) {
        SOCKET_PORT = Server_Socket_Port;
        SERVER = Server_IP;
        FILE_TO_RECEIVED = file.getFileAbsulotePath();
        FILE_SIZE = file.getFileSize() + 1000;
        LAST_MODIFIED_DATE = file.getLastModifiedDate();
    }

    public void recieveFile() throws IOException {
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(SERVER, SOCKET_PORT);
            System.out.println("Connecting...");

            // receive file
            byte[] mybytearray = new byte[(int) FILE_SIZE];
            System.out.println("waiting socket input");
            InputStream is = sock.getInputStream();
            System.out.println("complete socket input");
            fos = new FileOutputStream(FILE_TO_RECEIVED);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray, 0, mybytearray.length);
            current = bytesRead;

            do {
                bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
                if (bytesRead >= 0) {
                    current += bytesRead;
                }
            } while (bytesRead > -1);

            bos.write(mybytearray, 0, current);
            bos.flush();
            System.out.println("File " + FILE_TO_RECEIVED
                    + " downloaded (" + current + " bytes read)");
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (bos != null) {
                bos.close();
            }
            if (sock != null) {
                sock.close();
            }
        }
        File file = new File(FILE_TO_RECEIVED);
        if (file.isFile()) {
            file.setLastModified(LAST_MODIFIED_DATE);
        }
    }
}
