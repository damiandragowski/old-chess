package program;
import java.util.*;

public class ZegarSekundowy extends Thread {
    private Gra gra;
    private boolean koniec = false;
    private boolean mojaKolej;
    private long sek; 
    
    public ZegarSekundowy(Gra gra) {
        this.gra = gra;
        mojaKolej = false;
        sek = 0;
        start();
    }
    
    public void run() {
        long poczLicz;
        long now;
        String lanCzasu;
                               
        while (!koniec) { 
            spij(0);
            

            poczLicz = new Date().getTime();
            while (mojaKolej & !koniec) {
                spij(500); 
                now = new Date().getTime();
                sek += now - poczLicz;
                gra.zmienCzas(sek);
                poczLicz = new Date().getTime();
            }
        }            
    }
    
    private void spij(int czas) {
        try {
            Thread.sleep(czas);
        } catch (InterruptedException e) {
        }
    }
    
    public void wlacz(boolean stan) {
        mojaKolej = stan;
        interrupt();
    }
    
    public void zakoncz() {
        koniec = true;
        interrupt();
    }
    
}