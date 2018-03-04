package serwer;
import javax.swing.*;
import java.net.*;
import java.io.*;
import klientserwer.*;
import klient.*;
import program.*;

public class Serwer extends Thread {
    private ServerSocket sersoc;
    private Socket soc = null;
    private OknoProgramu rodzic;
    private CzekanieNaGraczy cng;
    private int czasGry; 
    private boolean zajety = false;
    private String opis;
    
    public Serwer(OknoProgramu rodzic, DanePolaczenia dp) throws Rezygnacja, BladDanych, IOException{
        this.rodzic = rodzic;
        
        
        czasGry = dp.getCzasGry();
        if (czasGry<0) throw new BladDanych("Podano niepoprawnie czas gry");
        
        int port = dp.getPort();
        if (port<1 || port >65535)
            throw new BladDanych("Podano numer portu który nie jest liczbą całkowitą z zakresu 0..65535");
        
        opis = dp.getOpis();
        
        sersoc = new ServerSocket(port);        
        start();
    }
    
    public void run() {
        cng = new CzekanieNaGraczy(rodzic); 
        cng.show();
        rodzic.menuZegar(true);
        

        while (true) {
            InputStream in = null;
            OutputStream out = null;
            Pisarz pisarz;
            Czytacz czytacz;
                        
            try {
                soc = sersoc.accept();                
            } catch (IOException e1) {
                zamknijGniazdo(soc);
                break;
            }
            
            try {
                in = soc.getInputStream();
                out = soc.getOutputStream();
            } catch (IOException e) {
                bladZamknijStrumienie(soc, in, out);
                break; 
            } 
            
            czytacz = new Czytacz(in);
            pisarz = new Pisarz(out);


            synchronized (this) {
                if (isZajety()) {
                    try {
                        pisarz.wyslijPaczke(Jezyk.BUSY);
                    } catch(IOException e) {
                    }
                    zamknij(soc, czytacz, pisarz);
                    break;
                }
                setZajety(true);
            }

            nawiazanieGry(soc, czytacz, pisarz);
        }
    }
    
    private void zamknijGniazdo(Socket soc) {
        try {
            soc.close();
        } catch (IOException e) {
        }
    }
    
    private void bladZamknijStrumienie(Socket soc, InputStream in, OutputStream out) {
        try {
            in.close();
        } catch (IOException e) {
        }
        try {
            out.close();
        } catch (IOException e) {
        }
        zamknijGniazdo(soc);
    }
    
    private void zamknij(Socket soc, Czytacz cz, Pisarz pi) {
        cz.wylacz();
        pi.wylacz();
        zamknijGniazdo(soc);
    }
    
    public void pokazZegar() {
        if (cng!=null) cng.show();
    }
    
    public void schowajZegar() {
        if (cng!=null) cng.hide();
    }
    
    public synchronized boolean setZajety(boolean busy) {
        if (zajety && busy) return false; 
        zajety = busy;
        return true;
    }
    
    private synchronized boolean isZajety() {
        return zajety;
    }
    
    private void nawiazanieGry(Socket gniazdo, Czytacz czytacz, Pisarz pisarz) {
        String buf;
        
        try {
            pisarz.wyslijPaczke(Jezyk.INFO);
            pisarz.wyslijPaczke(String.valueOf(czasGry));
            pisarz.wyslijPaczke(opis);
            
            buf = czytacz.czytajPaczke();
            if (!buf.equals(Jezyk.INFO)) throw new IOException("Nie chce z nami grać");
            buf = czytacz.czytajPaczke();
        } catch (IOException e) {
            setZajety(false);
            zamknij(gniazdo, czytacz, pisarz);
            return; 
        }
        
        try {
            DanePrzeciwnika dp = new DanePrzeciwnika(rodzic, gniazdo.getInetAddress(), czasGry, buf);
            dp.show();
            if (dp.isAnulowano()) { 
                setZajety(false);
                pisarz.wyslijPaczke(Jezyk.BYE);
                dp.dispose();
                zamknij(gniazdo, czytacz, pisarz);
                return;
            }
            dp.dispose();
            pisarz.wyslijPaczke(Jezyk.OK);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rodzic, "Niestety nastąpiło nieoczekiwane zerwanie połączenia");
            setZajety(false);
            zamknij(gniazdo, czytacz, pisarz);
        }
                
        rodzic.menuZegar(false);
        schowajZegar();
        rodzic.gra = new Gra(rodzic, rodzic.getSzachownica(), true, gniazdo, czytacz, pisarz, czasGry);
    }
    
    public void usun() {
        try {
            if (sersoc != null) sersoc.close();  
            if (soc != null) soc.close();        
        } catch (Exception e) {
        }
    }
}