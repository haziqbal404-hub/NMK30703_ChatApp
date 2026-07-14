package com.whatsapp.client;

import com.whatsapp.utils.CryptoUtil;
import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * @author ihazi
 */
public class ChatClient extends javax.swing.JFrame {
    private PrintWriter out;
    private BufferedReader in;
    private String userName;

    public ChatClient() {
    initComponents();
    
    // Simple Authentication Mechanism
    String pass = JOptionPane.showInputDialog(this, "Enter Server Password:");
    
    if (pass != null && pass.equals("1234")) { // "1234" is the 'Verified' password
        userName = JOptionPane.showInputDialog(this, "Enter Name:");
        if (userName == null || userName.isEmpty()) userName = "User";
        this.setTitle("WhatsApp - " + userName);
        connectToServer();
    } else {
        JOptionPane.showMessageDialog(this, "Authentication Failed! Closing App.");
        System.exit(0); // Closes the app if the password is wrong
    }
}

    private void connectToServer() {
        try {
            // Changed port to 6000 to avoid "Address already in use"
            Socket socket = new Socket("localhost", 7777); 
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        // Decrypt and show message
                        String decrypted = CryptoUtil.decrypt(line);
                        chatArea.append(decrypted + "\n");
                    }
                } catch (Exception e) { 
                    chatArea.append("Disconnected from server.\n");
                }
            }).start();
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(this, "Server not found! Make sure ChatServer is running on port 6000.");
        }
    }
    private void sendMessage() {
        try {
            String msg = messageField.getText().trim();
            if(!msg.isEmpty()){
                // Encrypt before sending
                String encrypted = CryptoUtil.encrypt(userName + ": " + msg);
                out.println(encrypted);
                messageField.setText("");
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        messageField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        chatArea.setColumns(20);
        chatArea.setRows(5);
        jScrollPane1.setViewportView(chatArea);

        messageField.addActionListener(this::messageFieldActionPerformed);

        sendButton.setText("send");
        sendButton.addActionListener(this::sendButtonActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(messageField, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(messageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
    sendMessage();       // TODO add your handling code here:
    }//GEN-LAST:event_sendButtonActionPerformed

    private void messageFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messageFieldActionPerformed
      sendMessage();   // TODO add your handling code here:
    }//GEN-LAST:event_messageFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new ChatClient().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField messageField;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
}
