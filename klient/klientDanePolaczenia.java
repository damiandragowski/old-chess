
package klient;
import javax.swing.*;
import java.net.*;

public class klientDanePolaczenia extends javax.swing.JDialog {
    private boolean anulowal=false; 
    

    public klientDanePolaczenia(java.awt.Window r) {
        initComponents();
        setSize(370,230);
        setLocationRelativeTo(r);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPort = new javax.swing.JTextField();
        jOpis = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jIP = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        setTitle("Informacje o serwerze");
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jLabel1.setText("Numer portu:");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 20, 100, 16);

        jLabel2.setText("Twoj Opis:");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 90, 160, 16);

        jPort.setColumns(6);
        jPort.setText("6667");
        getContentPane().add(jPort);
        jPort.setBounds(120, 20, 120, 20);

        jOpis.setColumns(30);
        jOpis.setRows(3);
        getContentPane().add(jOpis);
        jOpis.setBounds(20, 110, 330, 48);

        jButton1.setMnemonic('O');
        jButton1.setText("Ok");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton1);
        jButton1.setBounds(260, 20, 90, 26);

        jButton2.setMnemonic('A');
        jButton2.setText("Anuluj");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton2);
        jButton2.setBounds(260, 60, 90, 26);

        jLabel4.setText("Adress IP:");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(10, 50, 100, 16);

        jIP.setText("127.0.0.1");
        getContentPane().add(jIP);
        jIP.setBounds(120, 50, 120, 20);

        pack();
    }//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        anulowal = true;
        setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed
    
    public boolean isAnulowano() {
        return anulowal;
    }
    
    public int getPort() {
        try {
            return Integer.parseInt(jPort.getText());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public InetAddress getIP() {
        try {
            return InetAddress.getByName(jIP.getText());
        } catch (UnknownHostException e) {
            return null;
        }
    }
    
    public String getOpis() {
        return jOpis.getText();
    }
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTextField jIP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextArea jOpis;
    private javax.swing.JTextField jPort;
    // End of variables declaration//GEN-END:variables
    
}