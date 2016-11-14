/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansyncserver;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kazi
 */
public class SyncRequestServer implements Runnable {

    Thread th;
    public SyncRequestServer() {
        this.th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        while (LANSyncServer.IS_CLIENT_ALIVE) {
            try {
                LANSyncServer.smsserver.sendMessage(HandleProcess.REFRESH_REQUEST_MESSAGE);
                Thread.sleep(6000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SyncRequestServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
