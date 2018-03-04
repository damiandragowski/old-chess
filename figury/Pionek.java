package figury;
import java.awt.*;
import javax.swing.*;
import program.*;

public class Pionek extends Figura {
    private static ImageIcon f_b = new javax.swing.ImageIcon(ClassLoader.getSystemResource("figury/gif/b_pionek.gif")); 
    private static ImageIcon f_c = new javax.swing.ImageIcon(ClassLoader.getSystemResource("figury/gif/c_pionek.gif")); 
        
    /** Creates a new instance of Pionek */
    public Pionek(boolean biale) {
        if (f_b.getIconHeight()==-1||f_c.getIconHeight()==-1)
            JOptionPane.showMessageDialog(null, "Nie udało mi się wczytajć obrazka figury !\nNajprawdopodobniej gra będzie niemożliwa");
        this.biale = biale;
        id = 'P';
        if (biale) obrazek = f_b;
        else obrazek = f_c;
    }
    
    public int zbadajRuch(Point p1, Point p2, Figura[][] f, Szachownica sz) {
        int wstepne = zbadajRuchBezKrola(p1, p2, f); 
        if (wstepne != Szachownica.RUCH_DOZWOLONY) return wstepne;
        

        if (testujSzachowanie(p1, p2, f, sz)) return Szachownica.RUCH_SZACHOWANY;
        return Szachownica.RUCH_DOZWOLONY;
    }
    
    public int zbadajRuchBezKrola(Point p1, Point p2, Figura[][] f) {

        if (p1.x == p2.x && f[p2.x][p2.y]==null) {
            
            if ((p2.y-p1.y == 1 && biale) || (p2.y-p1.y == -1 && !biale)) return Szachownica.RUCH_DOZWOLONY; //pojedynczy ruch 
            if ((p1.y == 1 && p2.y-p1.y == 2 && biale) || (p1.y == 6 && p2.y-p1.y == -2 && !biale))
                if (f[p1.x][Math.abs(p1.y+p2.y) / 2] == null)
                    return Szachownica.RUCH_DOZWOLONY; 
                else return Szachownica.RUCH_ZABLOKOWANY;
            
            return Szachownica.RUCH_NIEDOZWOLONY;
        }
        

        if ( ((f[p2.x][p2.y]!=null) && (Math.abs(p1.x-p2.x) == 1))
            && ((biale && p2.y-p1.y == 1) || (!biale && p2.y-p1.y == -1)) )
            if (f[p2.x][p2.y].isBiala() != biale) return Szachownica.RUCH_DOZWOLONY;
        
        return Szachownica.RUCH_NIEDOZWOLONY;
    }
    
    public static ImageIcon getObrazek(boolean biale) {
        if (biale) return f_b;
        return f_c;
    }
}