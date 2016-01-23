/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.diskstation.ammon.w724va_fix_wa;

import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import me.diskstation.ammon.w724va_fix_wa.actions.Routing;
import me.diskstation.ammon.w724va_fix_wa.actions.Login;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author Marco Ammon <ammon.marco@t-online.de>
 */
public class FixWA {

    private CloseableHttpClient client;

    public FixWA() {
        String host = JOptionPane.showInputDialog(null, 
                "Please enter the hostname or IP of your Speedport W724V Typ A (usually \"speedport.ip\")",
                "Enter IP-Address",
                JOptionPane.DEFAULT_OPTION);
        JPasswordField pf = new JPasswordField();
        JOptionPane.showConfirmDialog(null, pf,
                "Enter password",
                JOptionPane.OK_OPTION, 
                JOptionPane.PLAIN_MESSAGE);
        String password = new String(pf.getPassword());
        
        client = HttpClients.createDefault();
        Login login = new Login(client);
        boolean loggedIn = login.login(host, password);
        if (loggedIn){
            Routing lr = new Routing(client, "192.168.2.1");
            int index = lr.getIndex();
            if (index != -1){
                boolean successful = lr.deleteIndex(index);
                if (successful){
                    JOptionPane.showMessageDialog(null,
                            "The wrong route has been deleted successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "The wrong route could not been deleted!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
               JOptionPane.showMessageDialog(null,
                            "The wrong route was not found!",
                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                            "Login was not successful! Please check your password.",
                            "Error", JOptionPane.ERROR_MESSAGE);
        };

        try {
            client.close();
        } catch (IOException io) {
            System.out.print(io);
        }

    }

    public static void main(String[] args) {
        FixWA fwa = new FixWA();
    }
}
