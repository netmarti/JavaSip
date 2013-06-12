/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.LinkedList;
import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import p2p.model.MessageProcessor;
import p2p.model.SipLayer;

/**
 *
 * @author jefferson
 */
public class Frame extends javax.swing.JFrame implements MessageProcessor {

    private SipLayer sipLayer;
    private String serverNotificer;
    private DefaultListModel modelo;
    private LinkedList<String> clients;

    /**
     * Creates new form Frame
     */
    public Frame(SipLayer sip, String ipServer, int portServer) {
        initComponents();
        onclose();
        sipLayer = sip;
        String from = "sip:" + sip.getUsername() + "@" + sip.getHost() + ":" + sip.getPort();
        this.setTitle(from);
        this.serverNotificer = "sip:server@" + ipServer + ":" + portServer;
        modelo = new DefaultListModel();
        this.list.setModel(modelo);
        this.list.addListSelectionListener(new HandlerList());
        this.sendBtn.addActionListener(new HandlerSend());
        try {
            this.sipLayer.sendMessage(this.serverNotificer, "connected");
        } catch (Throwable e) {
            e.printStackTrace();
            this.receivedMessages.append("Error notificando al tracker: " + e.getMessage() + "\n");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        receivedMessages = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        toAddress = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        sendBtn = new javax.swing.JButton();
        sendMessages = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        receivedMessages.setColumns(20);
        receivedMessages.setRows(5);
        jScrollPane1.setViewportView(receivedMessages);

        jScrollPane2.setViewportView(list);

        toAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toAddressActionPerformed(evt);
            }
        });

        jLabel2.setText("Destinatario");

        sendBtn.setText("Enviar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sendMessages)
                            .addComponent(toAddress)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(sendBtn))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sendMessages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(sendBtn)
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void toAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toAddressActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList list;
    private javax.swing.JTextArea receivedMessages;
    private javax.swing.JButton sendBtn;
    private javax.swing.JTextField sendMessages;
    private javax.swing.JTextField toAddress;
    // End of variables declaration//GEN-END:variables

    private void sendBtnActionPerformed(ActionEvent evt) {
    }

    private void onclose() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                try {
                    sipLayer.sendMessage(serverNotificer, "disconnected");
                } catch (Throwable e) {
                    e.printStackTrace();
                    receivedMessages.append("Error notificando al tracker: " + e.getMessage() + "\n");
                }
                System.exit(0);
            }
        });
    }

    @Override
    public void processMessage(String sender, String message) {
        if (message.charAt(0) == '*') {
            clients = new LinkedList<>();
            mostrarUsuario(message);
        } else {
            sender = sender.substring(sender.indexOf(":") + 1, sender.indexOf("@"));
            this.receivedMessages.append("De " + sender + ": " + message + "\n");
        }
    }

    @Override
    public void processError(String errorMessage) {
        this.receivedMessages.append("ERROR: " + errorMessage + "\n");
    }

    @Override
    public void processInfo(String infoMessage) {
//        this.receivedMessages.append(infoMessage + "\n");
    }

    private void mostrarUsuario(String message) {
        message = message.replace("*", "");
        String[] str = message.split("\\ ");
        String s;
        String suser;
        modelo.removeAllElements();
        for (int i = 0; i < str.length; i++) {
            s = str[i];
            suser = s.substring(s.indexOf(":") + 1, s.indexOf("@"));
            if (!sipLayer.getUsername().equals(suser)) {
                this.clients.add(s);
                modelo.addElement(suser);
            }
        }
    }

    class HandlerList implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            toAddress.setText(clients.get(list.getSelectedIndex()));
        }
    }

    class HandlerSend implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String to = toAddress.getText();
                String message = sendMessages.getText();
                sendMessages.setText("");
                sipLayer.sendMessage(to, message);
            } catch (ParseException | InvalidArgumentException | SipException ex) {
                ex.printStackTrace();
                receivedMessages.append("ERROR sending message: " + ex.getMessage() + "\n");
            }
        }
    }
}
