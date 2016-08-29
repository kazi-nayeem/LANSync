/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansyncserver;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kazi
 */
public class HandleProcess implements Runnable{
    
    public static final String UPDATE_MESSAGE = "update info:";
    public static final String SENDING_MESSAGE = "sending:";
    public static final String SEND_REQUEST_MESSAGE = "send request:";
    public static final String REFRESH_REQUEST_MESSAGE = "refresh request:";
    private static Thread t;
    
    public HandleProcess() {
        t = new Thread(this);
        t.start();
    }
    
    public static void handleProcess(String msg) {
        if (msg.startsWith(SEND_REQUEST_MESSAGE)) {
            sendFile(msg.substring(SEND_REQUEST_MESSAGE.length()));
        } else if (msg.startsWith(REFRESH_REQUEST_MESSAGE)) {
            if(t != null && t.isAlive()) return;
            new HandleProcess();
            System.out.println("lansyncserver.HandleProcess.handleProcess()");
        }
    }
    
    synchronized public static void sendFile(String subString) {
        try {
            FileInfo file = new FileInfo(new JSONObject(subString), LANSyncServer.FILE_DEFAULT_PATH);
            file = new FileInfo(file.getFileAbsulotePath(), LANSyncServer.FILE_DEFAULT_PATH);
            LANSyncServer.smsserver.sendMessage(SENDING_MESSAGE + file.getFileInfoMessage());
            SendFile fileServer = new SendFile(LANSyncServer.SOCKET_PORT, file);
            try {
                fileServer.sendFile();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (JSONException ex) {
            Logger.getLogger(HandleProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        browseFile(new File(LANSyncServer.FILE_DEFAULT_PATH));
    }
    
    void browseFile(File file) {
        System.err.println("->>>>"+file.getAbsolutePath());
        if (file.isFile()) {
            FileInfo fi = new FileInfo(file.getAbsolutePath(), LANSyncServer.FILE_DEFAULT_PATH);
            LANSyncServer.smsserver.sendMessage(UPDATE_MESSAGE + fi.getFileInfoMessage());
        }
        if (file.isDirectory()) {
            File[] files = new File(file.getAbsolutePath()).listFiles();
            for (File f : files) {
                browseFile(f);
            }
        }
    }
}
