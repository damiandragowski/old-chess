package program;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import klientserwer.*;
import serwer.*;
import program.*;

public class Gra extends Thread {
    private OknoProgramu op;
    private Szachownica sz;
    private boolean biale;
    private Czytacz czytacz;
    private Pisarz pisarz;
    private boolean  isMojRuch;
    private ZegarSekundowy zs;
    private int czasGry; 
    private boolean graSkonczona; 
    
    public Gra(OknoProgramu op, Szachownica sz, boolean biale, Socket gniazdo, Czytacz czytacz, Pisarz pisarz, int czasGry) {
        this.op = op;
        this.sz = sz;
        this.biale = biale;
        this.czytacz = czytacz;
        this.pisarz = pisarz;
        this.czasGry = czasGry;
        graSkonczona = false;
        
        isMojRuch = biale;
               
        op.zeruj();
        sz.ustawFigury();
        sz.wybierzKolor(biale);
        sz.setGra(this);
        sz.setEnabled(true); 
        zs = new ZegarSekundowy(this);
        if (isMojRuch) {
            sz.zablokujPlansze(true);
            zs.wlacz(true);
        }
        else sz.zablokujPlansze(false);
        
        start();
    }
    
    private void blad() {
        Serwer s = op.getSerwer();
        if (s != null) s.usun();
        op.menuPoddajeSie(false);
        op.gra = null;
        sz.setGra(null);
        sz.setEnabled(false);
        zs.zakoncz();
        if (!graSkonczona) JOptionPane.showMessageDialog(op, "Blad placzenia");
    }
    
    private void koniecGry() { 
        graSkonczona = true;
        Serwer s = op.getSerwer();
        if (s != null) s.usun();
        op.menuPoddajeSie(false);
        op.gra = null;
        sz.setGra(null);
        sz.setEnabled(false);
        zs.zakoncz();
    }
    
    public void zmienFigure(Point p, int naCo) {
        sz.zmienFigure(p, naCo, biale);
        
        try {
            pisarz.wyslijPaczke(Jezyk.AWANS);
            pisarz.wyslijPaczke("" + p.x);
            pisarz.wyslijPaczke("" + p.y);
            pisarz.wyslijPaczke("" + naCo);
        } catch (IOException e) {
            blad();
            return;
        }
    }
    

    public void zmienRuch(Point p1, Point p2) {
        sz.przesunFigure(p1, p2);
        
        if (isMojRuch) 
            try {
                pisarz.wyslijPaczke(Jezyk.RUCH);
                pisarz.wyslijPaczke("" + p1.x);
                pisarz.wyslijPaczke("" + p1.y);
                pisarz.wyslijPaczke("" + p2.x);
                pisarz.wyslijPaczke("" + p2.y);
            } catch (IOException e) {
                blad();
                return;
            }
        else {
            if (sz.szachMat(biale)) {
                JOptionPane.showMessageDialog(op, "Szachmat !!!");
                try {
                    pisarz.wyslijPaczke(Jezyk.PRZEGRALEM);
                } catch (IOException e) {
                }
                koniecGry();
                return;
            }
            
        }
        
        isMojRuch = !isMojRuch;
        zs.wlacz(isMojRuch);
        op.zaznaczZmianeGracza(!(isMojRuch^biale));
        
        if (isMojRuch) sz.zablokujPlansze(true);
        else sz.zablokujPlansze(false);
    }
    
    public void zmienCzas(long sek) {
        String czas = sformatujCzas(sek);
        op.zmienCzas(czas, biale);
        try {
            pisarz.wyslijPaczke(Jezyk.CZAS);
            pisarz.wyslijPaczke(czas);
        } catch (IOException e) {
            blad();
            return;        }
        
        if (sek >= czasGry*60*1000) {
            try {
                pisarz.wyslijPaczke(Jezyk.TIMEOUT);
            } catch (IOException e) {
                blad();
                return;
            }
            JOptionPane.showMessageDialog(op, "Czas sie skonczyl przegrales");
            koniecGry();
            return;
        }
    }
    
    private void zmienJegoCzas(String lan) {
        op.zmienCzas(lan, !biale);
    }
    
    private String sformatujCzas(long sek) {

        int godz;
        GregorianCalendar kal = new GregorianCalendar();
        kal.setTimeInMillis(0);
        if (kal.get(kal.HOUR)==1) godz = 1; 
        else godz = 0;
                

        DecimalFormat df = new DecimalFormat();
        df.applyPattern("00");
        
        kal.setTimeInMillis(sek);
        return df.format(kal.get(kal.HOUR)-godz) + ":" + df.format(kal.get(kal.MINUTE)) + ":" + df.format(kal.get(kal.SECOND));
    }
    
    public boolean ping() {
        return true;
    }
    
    public void chceSiePoddac() {
        try {
            pisarz.wyslijPaczke(Jezyk.BYE);
        } catch (IOException e) {
            blad();
            return;
        }
        koniecGry();
    }
    
    public void run() {
        try {
            while (!graSkonczona) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){
                }

                String buf = czytacz.czytajPaczke();
                
                if (buf.equals(Jezyk.BYE)) {
                    JOptionPane.showMessageDialog(op, "przeciwnik oposcil plansze");
                    new Wygrales(op).show();
                    break;
                }
                
                else
                if (buf.equals(Jezyk.PRZEGRALEM)) {
                    new Wygrales(op).show();
                    break;
                }
                                                
                else
                if (buf.equals(Jezyk.TIMEOUT)) {
                    JOptionPane.showMessageDialog(op, "przeciwnik zakonczyl swoj czas");
                    new Wygrales(op).show();
                    break;
                }
                
                else
                if (buf.equals(Jezyk.AWANS)) {
                    Point p = new Point(0, 0); 
                    int i = 0;                 
                                        
                    try {
                        buf = czytacz.czytajPaczke();
                        p.x = Integer.parseInt(buf);
                        buf = czytacz.czytajPaczke();
                        p.y = Integer.parseInt(buf);
                        buf = czytacz.czytajPaczke();
                        i = Integer.parseInt(buf);                                                
                    } catch (NumberFormatException e) {
                        throw new IOException("zle liczby");
                    }
                    sz.zmienFigure(p, i, !biale);
                }
                
                else
                if (buf.equals(Jezyk.RUCH)) {
                    Point p1 = new Point(0, 0);
                    Point p2 = new Point(0, 0);
                    
                    try {
                        buf = czytacz.czytajPaczke();
                        p1.x = Integer.parseInt(buf);
                        buf = czytacz.czytajPaczke();
                        p1.y = Integer.parseInt(buf);
                        buf = czytacz.czytajPaczke();
                        p2.x = Integer.parseInt(buf);
                        buf = czytacz.czytajPaczke();
                        p2.y = Integer.parseInt(buf);                        
                    } catch (NumberFormatException e) {
                        throw new IOException("zle liczby");
                    }
                    
                    zmienRuch(p1, p2);
                }
                
                else
                if (buf.equals(Jezyk.CZAS)) {
                    buf = czytacz.czytajPaczke();
                    zmienJegoCzas(buf);
                }
                
            }
        } catch (Exception e) {
            blad();
            return;
        }
        
        koniecGry();
    }
    
}