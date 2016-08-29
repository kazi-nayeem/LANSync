/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansyncclient;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kazi
 */
public class SyncRequest implements Runnable {

    ChatBox box;
    Thread th;
    volatile boolean flag;
    public SyncRequest(ChatBox box) {
        this.box = box;
        this.th = new Thread(this);
        flag = false;
        th.start();
        
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        while (true) {
            if(flag) break;
            try {
                box.sendMessage(HandleProcessClient.REFRESH_REQUEST_MESSAGE);
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SyncRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
