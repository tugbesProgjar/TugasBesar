/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasbesar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import static java.util.Collections.list;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import static tugasbesar.JavaChat.createFile;
import static tugasbesar.JavaChat.HOST_MODE;

/**
 *
 * @author Benny Irianto
 */
public class JavaChat extends javax.swing.JFrame {
//    private Socket socket;
    private ObjectOutputStream out;
    public static final int HOST_MODE = 0;
    public static final int CLIENT_MODE = 1;
    int mode;
    String Name;
    String roomname;
    String ipAddress;
    InetAddress hostip;
    JavaChat chat;
    DatagramSocket socket;
    ArrayList<client> ClientList;
    byte[] b;
    private DefaultListModel mod = new DefaultListModel();
    /**
     * Creates new form JavaChat
     */
    public JavaChat() {
       initComponents();
    }
    
    public JavaChat(String myname, int mod, String ip, String room) {
        
        try {
            ipAddress = ip;
            initComponents();
            Name = myname;
            mode = mod;
            hostip = InetAddress.getByName(ip);
            roomname = room;
            ClientList = new ArrayList<>();
            txtChatBox.setEditable(false);
            btnSend.addActionListener(new ActionListener() {
                //koneksi agar dapat terhubung chat
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
                // koneksi pada sisi admin
                socket = new DatagramSocket(37988);
                lbl_ip.setText("My Room IP : " + InetAddress.getLocalHost().getHostAddress());
                lblNama.setText("Nama : "+Name);
                send.setText("Receive");
            } else {
                //koneksi pada sisi mahasiswa
                socket = new DatagramSocket();
                send.setText("Choose");
                String reqresp = "!!^^" + Name + "^^!!";
                DatagramPacket pk = new DatagramPacket(reqresp.getBytes(), reqresp.length(), hostip, 37988);
                socket.send(pk);
                b = new byte[300];
                pk = new DatagramPacket(b, 300);
                socket.setSoTimeout(6000);
                socket.receive(pk);
                reqresp = new String(pk.getData());
                //menampilkan nama room dan username
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
            //memanggil function messanger
            Messenger.start();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void broadcast(String str) {
        //menampilkan pesan pada chatbox sesuai login
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

    //thread digunakan untuk menampilkan chat yang dikirim
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtChatBox = new javax.swing.JTextArea();
        lbl_ip = new javax.swing.JLabel();
        lblNama = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        btnSend = new javax.swing.JButton();
        txtName = new javax.swing.JTextField();
        send = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChatActionPerformed(evt);
            }
        });

        txtChatBox.setColumns(20);
        txtChatBox.setRows(5);
        jScrollPane1.setViewportView(txtChatBox);

        jScrollPane2.setViewportView(jScrollPane1);

        lbl_ip.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbl_ip.setText("My Room IP :");

        lblNama.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNama.setText("Nama : ");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Kritik dan Saran Polinema");

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(153, 204, 255), null, null));
        jPanel1.setForeground(new java.awt.Color(102, 204, 255));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Berlin Sans FB", 1, 11)); // NOI18N
        jButton3.setForeground(new java.awt.Color(102, 204, 255));
        jButton3.setText("Logout");
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
                .addGap(37, 37, 37)
                .addComponent(jButton3)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(200, 200, 200)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "File", "Image" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        btnSend.setText("Send Chat");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendActionPerformed(evt);
            }
        });

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
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtName))
                                    .addComponent(txtChat, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(send)
                                    .addComponent(btnSend))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(177, 177, 177))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSend, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtChat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(send))
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    void ready(int port, String host) {

        System.out.println("Memilih file untuk dikirim");
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(host);
            String fileName;

            JFileChooser jfc = new JFileChooser(); // Memilih file yang dikirim
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY); // Hanya files yang bisa dipilih (bukan direktori)
            if (jfc.isMultiSelectionEnabled()) { // Hanya satu file dalam sekali waktu (tidak ada beberapa pilihan)
                jfc.setMultiSelectionEnabled(false);
            }

            int r = jfc.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) { // jika sebuah file dipilih
                File f = jfc.getSelectedFile();
                fileName = f.getName();
                byte[] fileNameBytes = fileName.getBytes(); // nama file sebagai bytes untuk mengirimkan
                DatagramPacket fileStatPacket = new DatagramPacket(fileNameBytes, fileNameBytes.length, address, port); 
                socket.send(fileStatPacket); 

                byte[] fileByteArray = readFileToByteArray(f); // Array dari bytes files yang dibuat
                sendFile(socket, fileByteArray, address, port); // Memasukkan method untuk mengirim file sebenarnya
            }
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void sendFile(DatagramSocket socket, byte[] fileByteArray, InetAddress address, int port) throws IOException {
        System.out.println("Mengirim file");
        int sequenceNumber = 0; // untuk urutan
        boolean flag; // untuk melihat jika kita mendapatkan akhir dari file
        int ackSequence = 0; // untuk melihat jika datagram telah diterima dengan benar

        for (int i = 0; i < fileByteArray.length; i = i + 1021) {
            sequenceNumber += 1;

            byte[] message = new byte[1024]; // dua bytes pertama data untuk mengontrol (integritas datagram dn urutan)
            message[0] = (byte) (sequenceNumber >> 8);
            message[1] = (byte) (sequenceNumber);

            if ((i + 1021) >= fileByteArray.length) {
                flag = true;
                message[2] = (byte) (1); // Mencapai akhir dari file (datagram terakhir untuk dikirimkan) 
            } else {
                flag = false;
                message[2] = (byte) (0); // Belum mencapai akhir dari file, mssih mengirim datagrams
            }

            if (!flag) {
                System.arraycopy(fileByteArray, i, message, 3, 1021);
            } else {
                System.arraycopy(fileByteArray, i, message, 3, fileByteArray.length - i);
            }

            DatagramPacket sendPacket = new DatagramPacket(message, message.length, address, port); // data yang dikirimkan
            socket.send(sendPacket); // mengirimkan data

            boolean ackRec; // apakah datagram diterima ?

            while (true) {
                byte[] ack = new byte[2]; // membuat paket datagram lainnya
                DatagramPacket ackpack = new DatagramPacket(ack, ack.length);

                try {
                    socket.setSoTimeout(50); // Menunggu server untuk mengirim ack
                    socket.receive(ackpack);
                    ackSequence = ((ack[0] & 0xff) << 8) + (ack[1] & 0xff); 
                    ackRec = true;
                } catch (SocketTimeoutException e) {
                    ackRec = false; // Kita tidak menerima sebah ack
                }

                // Jika paket telah diterima dengan benar, paket selanjutnya bisa dikirimkan 
                if ((ackSequence == sequenceNumber) && (ackRec)) {
                    break;
                } // Paket belum diterima, jadi kita mengirim ulang
                else {
                    socket.send(sendPacket);
                }
            }
        }
    }
  
    private static byte[] readFileToByteArray(File file) {
        FileInputStream fis = null;
        // Membuat sebuah byte array menggunakan panjang dari file
        // file.length mengembalikan panjang dimana cast ke int
        byte[] bArray = new byte[(int) file.length()];
        try {
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();

        } catch (IOException ioExp) {
            ioExp.printStackTrace();
        }
        return bArray;
    }
    
    public static void createFile (int port, String serverRoute){
        try{
            DatagramSocket socket = new DatagramSocket(port);
            byte[] receiveFileName = new byte[1024]; //Menyimpan data dari nama datagram
            DatagramPacket receiveFileNamePacket = new DatagramPacket(receiveFileName, receiveFileName.length);
            socket.receive(receiveFileNamePacket); // Menerima datagram dengan nama file
            System.out.println("Menerima nama file");
            byte [] data = receiveFileNamePacket.getData(); // Membaca nama dalam bytes
            String fileName = new String(data, 0, receiveFileNamePacket.getLength()); //Mengkonversi nama ke dalam string
            
            System.out.println("Membuat file");
            File f = new File (serverRoute + "\\" + fileName); // Membuat file
            FileOutputStream outToFile = new FileOutputStream(f); // Membuat stream melalui konten file yang telah ditulis
            
            receiveFile(outToFile, socket); // Menerima file
            socket.close();
        }catch(Exception ex){
            ex.printStackTrace();
            System.exit(1);
        }   
    }
    
    private static void receiveFile(FileOutputStream outToFile, DatagramSocket socket) throws IOException {
        System.out.println("Menerima file");
        boolean flag;
        int sequenceNumber = 0; // Urutan sequences
        int foundLast = 0; // sequence terakhir yang ditemukan
        
        while (true) {
            byte[] message = new byte[1024]; // Dimana data dari datagram yang diterima disimpan
            byte[] fileByteArray = new byte[1021]; // Dimana kita menyimpan data untuk ditulis pada file

            DatagramPacket receivedPacket = new DatagramPacket(message, message.length);
            socket.receive(receivedPacket);
            message = receivedPacket.getData(); // Data yang ditulis pada file

            // Mendapatkan port dan address 
            InetAddress address = receivedPacket.getAddress();
            int port = receivedPacket.getPort();

            //memperoleh sequence number
            sequenceNumber = ((message[0] & 0xff) << 8) + (message[1] & 0xff);
            // Mengecek jika kita telah mencapai datagram terakhir
            flag = (message[2] & 0xff) == 1;
            
            // Jika urutan nomor yang terakhir dilihat +1, maka bernilai benar
            // Kami mendapatkan data dari pesan dan menuliskan konfirmasi bahwa sudah diterima dengan benar
            if (sequenceNumber == (foundLast + 1)) {
                // mengatur urutan nomor terakhir menjadi satu-satunya yang baru saja diterima
                foundLast = sequenceNumber;
                // Menerima data dari pesan
                System.arraycopy(message, 3, fileByteArray, 0, 1021);
                // Menulis data yang telah diperoleh pada file dan mencetak urutan nomor data
                outToFile.write(fileByteArray);
                sendAck(foundLast, socket, address, port);
            } else {
                sendAck(foundLast, socket, address, port);
            }
            // Mengecek untuk datagram terakhir
            if (flag) {
                outToFile.close();
                break;
            }
        }
    }    
    
    private static void sendAck(int foundLast, DatagramSocket socket, InetAddress address, int port) throws IOException {
        // kirim ack
        byte[] ackPacket = new byte[2];
        ackPacket[0] = (byte) (foundLast >> 8);
        ackPacket[1] = (byte) (foundLast);

        // datagram paket dikirimkan
        DatagramPacket acknowledgement = new DatagramPacket(ackPacket, ackPacket.length, address, port);
        socket.send(acknowledgement);
//        System.out.println("Ack: Nomor Sequence = " + foundLast);
    }
    
    private void txtChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChatActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        home a = new home();
        a. setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnSendActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendActionPerformed
        if(mode == HOST_MODE){
            System.out.println("Ready to receive!");
            int port = 1234; // portnya
            String serverRoute = "D:\\KritikSaran\\"; // destinasi file
            createFile(port, serverRoute);
        } else{
            int port = 1234;
            String host = ipAddress; // local host bisa diubah
            JavaChat chat = new JavaChat();
            chat.ready(port, host);  
        }

    }//GEN-LAST:event_sendActionPerformed

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
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lbl_ip;
    private javax.swing.JButton send;
    private javax.swing.JTextField txtChat;
    private javax.swing.JTextArea txtChatBox;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
