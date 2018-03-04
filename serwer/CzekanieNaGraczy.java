package serwer;
import java.util.*;
import java.text.*;
import javax.swing.*;

public class CzekanieNaGraczy extends javax.swing.JDialog implements Runnable {

    private long poczLicz;
    private Thread uaktualniacz;
    

    public CzekanieNaGraczy(JFrame r) {
        initComponents();
        setSize(200,100);
        setLocationRelativeTo(r);
        uaktualniacz = new Thread(this);
        uaktualniacz.start();
    }
    
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Czekanie...");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jLabel1, gridBagConstraints);

        jButton1.setMnemonic('U');
        jButton1.setText("ukryj zegar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        getContentPane().add(jButton1, gridBagConstraints);

    }//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        hide();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    
    public void run() {
        GregorianCalendar kal = new GregorianCalendar();
        poczLicz = new Date().getTime();
        long now;
        
        int godz;
        kal.setTimeInMillis(0);
        if (kal.get(kal.HOUR)==1) godz = 1;
        else godz = 0;
        
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("00");
        ZmieniaczEtykiety ze = new ZmieniaczEtykiety();
        
        while (true) {
            now = new Date().getTime();
            kal.setTimeInMillis(now - poczLicz);
            ze.str = df.format(kal.get(kal.HOUR)-godz) + ":" + df.format(kal.get(kal.MINUTE)) + ":" + df.format(kal.get(kal.SECOND));
            SwingUtilities.invokeLater(ze);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) { 
                return;
            }
        }
    }
           
    class ZmieniaczEtykiety implements Runnable{
            public String str;

            public void run() {
                jLabel1.setText(str);
            }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
    
}