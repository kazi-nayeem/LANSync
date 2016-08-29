/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansyncserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Kazi
 */
public class SendFile {

    private final int SOCKET_PORT;  // you may change this
    private final String FILE_TO_SEND;

    public SendFile(int SERVER_SOCKET_PORT, FileInfo file) {
        SOCKET_PORT = SERVER_SOCKET_PORT;
        FILE_TO_SEND = file.getFileAbsulotePath();
    }

    public void sendFile() throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;
        try {
            servsock = new ServerSocket(SOCKET_PORT);

            System.out.println("Waiting...");
            try {
                sock = servsock.accept();
                System.out.println("Accepted connection : " + sock);
                // send file
                File myFile = new File(FILE_TO_SEND);
                byte[] mybytearray = new byte[(int) myFile.length()];
                fis = new FileInputStream(myFile);
                bis = new BufferedInputStream(fis);
                bis.read(mybytearray, 0, mybytearray.length);
                os = sock.getOutputStream();
                System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
                os.write(mybytearray, 0, mybytearray.length);
                os.flush();
                System.out.println("Done.");
            } finally {
                if (bis != null) {
                    bis.close();
                }
                if (os != null) {
                    os.close();
                }
                if (sock != null) {
                    sock.close();
                }
            }

        } finally {
            if (servsock != null) {
                servsock.close();
            }
        }
    }

}
