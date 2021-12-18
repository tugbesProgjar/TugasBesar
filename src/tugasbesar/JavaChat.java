/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasbesar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import static tugasbesar.JavaChat.HOST_MODE;

/**
 *
 * @author Benny Irianto
 */
public class JavaChat extends javax.swing.JFrame {
    
    public static final int HOST_MODE = 0;
    public static final int CLIENT_MODE = 1;
    int mode;
    String Name;
    String roomname;
    InetAddress hostip;
    JavaChat chat;
    DatagramSocket socket;
    ArrayList<client> ClientList;
    byte[] b;
    /**
     * Creates new form JavaChat
     */
    public JavaChat() {
       initComponents();
    }
    
    public JavaChat(String myname, int mod, String ip, String room) {
        
        try {
            initComponents();
            Name = myname;
            mode = mod;
            hostip = InetAddress.getByName(ip);
            roomname = room;
            ClientList = new ArrayList<>();
            txtChatBox.setEditable(false);
            btnSend.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String s = txtChat.getText();
                    if (s.equals("") == false) {
                        if (mode == HOST_MODE) {
                            broadcast(Name + ": " + s);
                        } else {
                            sendToHost(Name + ": " + s);
                        }
                        txtChat.setText("");
                    }
                }
            });

            if (mode == HOST_MODE) {
                socket = new DatagramSocket(37988);
                lbl_ip.setText("My Room IP : " + InetAddress.getLocalHost().getHostAddress());
                lblNama.setText("Nama : "+Name);
            } else {
                socket = new DatagramSocket();
                String reqresp = "!!^^" + Name + "^^!!";
                DatagramPacket pk = new DatagramPacket(reqresp.getBytes(), reqresp.length(), hostip, 37988);
                socket.send(pk);
                b = new byte[300];
                pk = new DatagramPacket(b, 300);
                socket.setSoTimeout(6000);
                socket.receive(pk);
                reqresp = new String(pk.getData());
                if (reqresp.contains("!!^^")) {
                    roomname = reqresp.substring(4, reqresp.indexOf("^^!!"));
                    lbl_ip.setText("Chat Room : " + roomname);
                    lblNama.setText("Nama : "+Name);
                    btnSend.setEnabled(true);
                    txtChat.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(chat, "Tidak ada respon");
                    System.exit(0);
                }
            }
            Messenger.start();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void broadcast(String str) {
        try {
            DatagramPacket pack = new DatagramPacket(str.getBytes(), str.length());
            for (int i = 0; i < ClientList.size(); i++) {
                pack.setAddress(InetAddress.getByName(ClientList.get(i).ip));
                pack.setPort(ClientList.get(i).port);
                socket.send(pack);
            }
            txtChatBox.setText(txtChatBox.getText() + "\n" + str);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(chat, ex);
        }
    }

    public void sendToHost(String str) {
        DatagramPacket pack = new DatagramPacket(str.getBytes(), str.length(), hostip, 37988);
        try {
            socket.send(pack);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(chat, "Kirim pesan gagal");
        }
    }

    Thread Messenger = new Thread() {
        public void run() {
            try {
                while (true) {
                    b = new byte[300];
                    DatagramPacket pkt = new DatagramPacket(b, 300);
                    socket.setSoTimeout(0);
                    socket.receive(pkt);
                    String s = new String(pkt.getData());
                    if (mode == HOST_MODE) {
                        if (s.contains("!!^^")) {
                            client temp = new client();
                            temp.ip = pkt.getAddress().getHostAddress();
                            temp.port = pkt.getPort();
                            broadcast(s.substring(4, s.indexOf("^^!!")) + " Bergabung.");
                            ClientList.add(temp);
                            s = "!!^^" + roomname + "^^!!";
                            pkt = new DatagramPacket(s.getBytes(), s.length(), InetAddress.getByName(temp.ip), temp.port);
                            socket.send(pkt);
                            btnSend.setEnabled(true);
                            txtChat.setEnabled(true);
                        } else {
                            broadcast(s);
                        }
                    } else {
                        txtChatBox.setText(txtChatBox.getText() + "\n" + s);
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(chat, ex);
                System.exit(0);
            }
        }
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtChat = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtChatBox = new javax.swing.JTextArea();
        lbl_ip = new javax.swing.JLabel();
        lblNama = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChatActionPerformed(evt);
            }
        });

        btnSend.setText("Send");

        txtChatBox.setColumns(20);
        txtChatBox.setRows(5);
        jScrollPane1.setViewportView(txtChatBox);

        jScrollPane2.setViewportView(jScrollPane1);

        lbl_ip.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbl_ip.setText("IP Address : ");

        lblNama.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNama.setText("Nama : ");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Kritik dan Saran Polinema");

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(153, 204, 255), null, null));
        jPanel1.setForeground(new java.awt.Color(102, 204, 255));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Berlin Sans FB", 1, 11)); // NOI18N
        jButton1.setForeground(new java.awt.Color(102, 204, 255));
        jButton1.setText("Dashboard");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Berlin Sans FB", 1, 11)); // NOI18N
        jButton2.setForeground(new java.awt.Color(102, 204, 255));
        jButton2.setText("Chat");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Berlin Sans FB", 1, 11)); // NOI18N
        jButton3.setForeground(new java.awt.Color(102, 204, 255));
        jButton3.setText("Disconnect");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addComponent(jButton1)
                .addGap(27, 27, 27)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNama)
                            .addComponent(lbl_ip)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtChat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(193, 193, 193))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(19, 19, 19)
                .addComponent(lbl_ip)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNama)
                .addGap(25, 25, 25)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtChat, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(76, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChatActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        JavaChat a = new JavaChat();
        a. setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        loginMahasiswa a = new loginMahasiswa();
        a. setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dashboard a = new dashboard();
        a. setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JavaChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JavaChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JavaChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JavaChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JavaChat().setVisible(true);
            }
        });
        
        try {
            String host = "", room = "";
            String name = JOptionPane.showInputDialog("Masukkan namamu");
            if (name == null || name.equals("")) {
                JOptionPane.showMessageDialog(null, "Name tidak boleh kosong");
                return;
            }
            int mode = JOptionPane.showConfirmDialog(null, "Buat Room Chat Sendiri? atau Join Room Chat?\nYa - Buat Room\nTidak - Join Room Chat", "Buat atau Join?", JOptionPane.YES_NO_OPTION);
            if (mode == 1) {
                host = JOptionPane.showInputDialog("Masukan alamat IP Address Room");
                if (host == null || host.equals("")) {
                    JOptionPane.showMessageDialog(null, "IP room tidak ada");
                    return;
                }
            } else {
                room = JOptionPane.showInputDialog("Nama Roommu");
            }
            JavaChat obj = new JavaChat(name, mode, host, room);
            obj.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lbl_ip;
    private javax.swing.JTextField txtChat;
    private javax.swing.JTextArea txtChatBox;
    // End of variables declaration//GEN-END:variables
}
