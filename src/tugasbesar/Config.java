/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasbesar;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
    private int port;
    private String ip;

    public Config() {
        try {
            port = 55286;
            ip = InetAddress.getLocalHost().getHostAddress().trim();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }
    
    
}
