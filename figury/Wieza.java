package figury;
import java.awt.*;
import javax.swing.*;
import program.*;

public class Wieza extends Figura {
    private static ImageIcon f_b = new javax.swing.ImageIcon(ClassLoader.getSystemResource("figury/gif/b_wieza.gif")); 
    private static ImageIcon f_c = new javax.swing.ImageIcon(ClassLoader.getSystemResource("figury/gif/c_wieza.gif")); 
    
    /** Creates a new instance of Wieza */
    public Wieza(boolean biale) {
        if (f_b.getIconHeight()==-1||f_c.getIconHeight()==-1)
            JOptionPane.showMessageDialog(null, "Nie udało mi się wczytajć obrazka figury !\nNajprawdopodobniej gra będzie niemożliwa");
        this.biale = biale;
        id = 'W';
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
        int dx, dy; 
        
        if (p1.x==p2.x) dx = 0;
        else if (p1.x<p2.x) dx = 1;
        else dx = -1;
        
        if (p1.y==p2.y) dy = 0;
        else if (p1.y<p2.y) dy = 1;
        else dy = -1;
        
        
        if (dx!=0 && dy!=0) return Szachownica.RUCH_NIEDOZWOLONY; 
        

        for (int a = p1.x + dx, b = p1.y + dy; a != p2.x || b != p2.y; a += dx, b += dy)
            if (f[a][b]!=null) return Szachownica.RUCH_ZABLOKOWANY;
        

        if (f[p2.x][p2.y]!=null)
            if (f[p2.x][p2.y].isBiala() == biale) return Szachownica.RUCH_ZABLOKOWANY;
        
        return Szachownica.RUCH_DOZWOLONY;
    }
    
    public static ImageIcon getObrazek(boolean biale) {
        if (biale) return f_b;
        return f_c;
    }
}