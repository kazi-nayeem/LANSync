package lansyncclient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lansyncserver.FileInfo;
import lansyncserver.ReceiveFile;
import lansyncserver.SendFile;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kazi
 */
public class HandleProcessClient implements Runnable {

    public static final String UPDATE_MESSAGE = "update info:";
    public static final String SENDING_MESSAGE = "sending:";
    public static final String SEND_REQUEST_MESSAGE = "send request:";
    public static final String REFRESH_REQUEST_MESSAGE = "refresh request:";
    private static ChatBox ClientBox;
    private static Thread t;

    public HandleProcessClient() {
        t = new Thread(this);
        t.start();
    }

    public static void handleProcess(String msg, ChatBox clientBox) throws IOException {
        ClientBox = clientBox;
        if (msg.startsWith(UPDATE_MESSAGE)) {
            updateHandle(msg.substring(UPDATE_MESSAGE.length()), clientBox);
        } else if (msg.startsWith(SENDING_MESSAGE)) {
            readyToRecieve(msg.substring(SENDING_MESSAGE.length()), clientBox);
        } else if (msg.startsWith(SEND_REQUEST_MESSAGE)) {
            sendFile(msg.substring(SEND_REQUEST_MESSAGE.length()), clientBox);
        } else if (msg.startsWith(REFRESH_REQUEST_MESSAGE)) {
            if (t != null && t.isAlive()) {
                return;
            }
            new HandleProcessClient();
            System.out.println("lansyncclient.HandleProcessclient.handleProcessclient()");
        }
    }

    private static void updateHandle(String substring, ChatBox clientBox) {
        try {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            JSONObject obj = new JSONObject(substring);
            FileInfo file = new FileInfo(obj, LANSyncClient.FILE_DEFAULT_PATH);
            File f = new File(file.getFileAbsulotePath());
            if (f.isFile()) {
                if (f.lastModified() < file.getLastModifiedDate()) {
                    requetToSend(file, clientBox);
                }
            } else {
                String p = f.getParent();
                new File(p).mkdirs();
                requetToSend(file, clientBox);
            }
        } catch (JSONException ex) {
            Logger.getLogger(HandleProcessClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void requetToSend(FileInfo file, ChatBox clientBox) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        clientBox.sendMessage(SEND_REQUEST_MESSAGE + file.getFileInfoMessage());
    }

    private static void readyToRecieve(String substring, ChatBox clientBox) throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try {
            JSONObject obj = new JSONObject(substring);
            FileInfo file = new FileInfo(obj, LANSyncClient.FILE_DEFAULT_PATH);
            System.err.println(file.getFileInfoMessage());
            ReceiveFile fileToReceive = new ReceiveFile(LANSyncClient.SOCKET_PORT, LANSyncClient.SERVER, file);
            fileToReceive.recieveFile();
        } catch (JSONException ex) {
            Logger.getLogger(ChatBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    synchronized public static void sendFile(String subString, ChatBox clientbox) {
        try {
            FileInfo file = new FileInfo(new JSONObject(subString), LANSyncClient.FILE_DEFAULT_PATH);
            file = new FileInfo(file.getFileAbsulotePath(), LANSyncClient.FILE_DEFAULT_PATH);
            clientbox.sendMessage(SENDING_MESSAGE + file.getFileInfoMessage());
            SendFile fileServer = new SendFile(LANSyncClient.SOCKET_PORT, file);

            try {
                fileServer.sendFile();
            } catch (IOException ex) {
                Logger.getLogger(HandleProcessClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (JSONException ex) {
            Logger.getLogger(HandleProcessClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        browseFile(new File(LANSyncClient.FILE_DEFAULT_PATH));
    }

    void browseFile(File file) {
        System.err.println("->>>>" + file.getAbsolutePath());
        if (file.isFile()) {
            FileInfo fi = new FileInfo(file.getAbsolutePath(), LANSyncClient.FILE_DEFAULT_PATH);
            ClientBox.sendMessage(UPDATE_MESSAGE + fi.getFileInfoMessage());
        }
        if (file.isDirectory()) {
            File[] files = new File(file.getAbsolutePath()).listFiles();
            for (File f : files) {
                browseFile(f);
            }
        }
    }

}
