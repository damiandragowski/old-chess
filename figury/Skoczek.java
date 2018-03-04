package figury;
import java.awt.*;
import javax.swing.*;
import program.*;

public class Skoczek extends Figura {
    
    /** Creates a new instance of Skoczek */
    private static ImageIcon f_b = new javax.swing.ImageIcon(ClassLoader.getSystemResource("figury/gif/b_skoczek.gif")); 
    private static ImageIcon f_c = new javax.swing.ImageIcon(ClassLoader.getSystemResource("figury/gif/c_skoczek.gif")); 
    
    /** Creates a new instance of Skoczek */
    public Skoczek(boolean biale) {
        if (f_b.getIconHeight()==-1||f_c.getIconHeight()==-1)
            JOptionPane.showMessageDialog(null, "Nie udało mi się wczytajć obrazka figury !\nNajprawdopodobniej gra będzie niemożliwa");
        this.biale = biale;
        id='S';
        if (biale) obrazek = f_b;
        else obrazek = f_c;
    }
    
    public int zbadajRuch(Point p1, Point p2, Figura[][] f, Szachownica sz) {
        int wynik = zbadajRuchBezKrola(p1, p2, f);
        if (wynik != Szachownica.RUCH_DOZWOLONY) return wynik;
               

        if (testujSzachowanie(p1, p2, f, sz)) return Szachownica.RUCH_SZACHOWANY;
        return Szachownica.RUCH_DOZWOLONY;
    }
    
    public int zbadajRuchBezKrola(Point p1, Point p2, Figura[][] f) {
        if ( (Math.abs(p1.x-p2.x)==2 && Math.abs(p1.y-p2.y)==1)     //czy jest to ruch konika
           ||(Math.abs(p1.x-p2.x)==1 && Math.abs(p1.y-p2.y)==2)) {  //
               
               if (f[p2.x][p2.y]!=null)
                   if (f[p2.x][p2.y].isBiala() == biale) return Szachownica.RUCH_ZABLOKOWANY;
               
               return Szachownica.RUCH_DOZWOLONY;
        }
        return Szachownica.RUCH_NIEDOZWOLONY;
    }
    public static ImageIcon getObrazek(boolean biale) {
        if (biale) return f_b;
        return f_c;
    }
    
}