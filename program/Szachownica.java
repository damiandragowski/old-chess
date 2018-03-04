package program;
import java.awt.*;
import javax.swing.*;
import figury.*;

public class Szachownica extends javax.swing.JPanel {
    public static final int RUCH_DOZWOLONY = 0;
    public static final int RUCH_NIEDOZWOLONY = 1; 
    public static final int RUCH_ZABLOKOWANY = 2;
    public static final int RUCH_SZACHOWANY = 3;

    
    private ImageIcon imSzach;
    private Figura[][] fig;
    private Point k_b, k_c;
    private boolean wyswietlac;
                               
    private Point nacisnieta; 
    private final Color kolorKratkiZaznaczonej = Color.yellow;
    private boolean isBiale; 
    private boolean mojRuch; 
    private Gra gra; 
    
    /** Creates new form Szachownica */
    public Szachownica() {
        wyswietlac = isEnabled();
        imSzach = new javax.swing.ImageIcon(getClass().getResource("/figury/gif/szachownica_gotowa.gif"));
        initComponents();
        Dimension wym = new Dimension(imSzach.getIconWidth(), imSzach.getIconHeight());
        setPreferredSize(wym);
        fig = new Figura[8][];
        for (int a=0; a<8; a++)
            fig[a] = new Figura[8];
    }
    
    public void setEnabled(boolean b) {
        wyswietlac = b;
        paintImmediately(0, 0, getWidth(), getHeight());
        super.setEnabled(b);
    }
    
    public synchronized void zablokujPlansze(boolean b) {
        mojRuch = b;
    }
    
    public boolean getEnabled() {
        return wyswietlac;
    }
    
    public void wybierzKolor(boolean biale) {
        isBiale = biale;
    }
    
    public void setGra(Gra gra) {
        this.gra = gra;
    }
        
    public void ustawFigury() {
        nacisnieta = null;
        
        int x,y;
        
        for (y=0; y<8; y++)
            for (x=0; x<8; x++)
                fig[x][y]=null;
        
        for (x=0; x<8; x++) {
            fig[x][1] = new Pionek(true);
            fig[x][6] = new Pionek(false);
        }
        
        fig[0][0] = new Wieza(true);
        fig[7][0] = new Wieza(true);
        fig[0][7] = new Wieza(false);
        fig[7][7] = new Wieza(false);
       
        fig[1][0] = new Skoczek(true);
        fig[6][0] = new Skoczek(true);
        fig[1][7] = new Skoczek(false);
        fig[6][7] = new Skoczek(false);
       
        fig[2][0] = new Goniec(true);
        fig[5][0] = new Goniec(true);
        fig[2][7] = new Goniec(false);
        fig[5][7] = new Goniec(false);
       
        fig[3][0] = new Hetman(true); 
        fig[4][0] = new Krol(true);
        fig[4][7] = new Hetman(false);
        fig[3][7] = new Krol(false);
        
        k_b = new Point(4,0);
        k_c = new Point(3,7);
        
    }
           
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        setLayout(new java.awt.BorderLayout());

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

    }//GEN-END:initComponents
    private Point naKratke(Point p) {
        int x = ((int)p.getX()-12)/30;
        int y = 7 - ((int)p.getY()-12)/30;
        if (x<0 || x>7 || y<0 || y>7) return null; 
        return new Point(x,y);
    }
    
    private int awansPionka() {
        int i;
        PionekDochodzi dp = new PionekDochodzi(this, true);
        i = dp.getWybrana();
        dp.dispose();
        return i;
    }
        
    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        synchronized (this) { 
            if (!mojRuch) return;
        }
        
        if (nacisnieta==null) return; 
        
        Point p = naKratke(evt.getPoint());
        if (p!=null) {
            if (!p.equals(nacisnieta) && (fig[nacisnieta.x][nacisnieta.y].isBiala() == isBiale)) { 
                int spr;
                spr = fig[nacisnieta.x][nacisnieta.y].zbadajRuch(nacisnieta, p, fig, this); 
                if (spr == RUCH_DOZWOLONY) {
                    gra.zmienRuch(nacisnieta, p);  
                    
                    if (fig[p.x][p.y] instanceof Krol)
                        if (fig[p.x][p.y].isBiala()) k_b = p;
                        else k_c = p;
                                                           
                    if (fig[p.x][p.y] instanceof Pionek && (p.y == 0 || p.y == 7)) {
                        PionekDochodzi pd;
                        if (fig[p.x][p.y].isBiala()) pd = new PionekDochodzi(this, true);
                        else pd = new PionekDochodzi(this, false);
                        pd.show();
                                                
                        gra.zmienFigure(p, pd.getWybrana());
                        pd.dispose();
                    }
                }
                else if(spr == RUCH_SZACHOWANY) JOptionPane.showMessageDialog(this, "Szach");
            }
        }
        nacisnieta = null;
        repaint();
    }//GEN-LAST:event_formMouseReleased

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        synchronized (this) {
            if (!mojRuch) return;
        }
        
        Point p = naKratke(evt.getPoint());
        if (p!=null) {
            if (fig[p.x][p.y]==null) return; 
            nacisnieta = new Point(p.x, p.y);
            repaint();
        }
    }//GEN-LAST:event_formMousePressed
           
    public boolean szachowanieKrola(boolean bialyKrol) {
        Point krol = getKrol(bialyKrol);
        
        for (int a = 0; a < 8; a++)
            for (int b = 0; b < 8; b++)   
                if (fig[a][b]!=null)  
                    if (fig[a][b].zbadajRuchBezKrola(new Point(a,b), krol, fig) == RUCH_DOZWOLONY) 
                        return true;
        return false;
    }
    
    public synchronized boolean szachMat(boolean bialyKrol) {
        if (!szachowanieKrola(bialyKrol)) return false;
        
        for (int a = 0; a <8; a++)
            for (int b = 0; b < 8; b++)
                if (fig[a][b] != null)
                    if (fig[a][b].isBiala() == bialyKrol)
                        for (int c = 0; c < 8; c++)
                            for (int d = 0; d < 8; d++)
                                if (fig[a][b].zbadajRuch(new Point(a,b), new Point(c,d), fig, this) == RUCH_DOZWOLONY)
                                    return false;
        return true;
    }
    
    private void zaznaczKratke(Point p, Graphics g) {
        Color staryKolor = g.getColor();
        g.setColor(kolorKratkiZaznaczonej);
        g.fillRect(10+2+ p.x*30, 10+2+ (7-p.y)*30, 30, 30);
        g.setColor(staryKolor);
    }
    
    public void paint(Graphics g) {
        if (wyswietlac) {
            imSzach.paintIcon(this, g, 0, 0);
            if (nacisnieta!=null) zaznaczKratke(nacisnieta, g);
            
            for (int y=0; y<8; y++)
                for (int x=0; x<8; x++)
                    if (fig[x][y]!=null)
                        fig[x][y].paintxy(x*30+10+2, 264-((y+1)*30+10+2), g); 
        } else {
            Color staryKolor = g.getColor();
            g.setColor(getBackground());
            Rectangle rec = g.getClipRect();
            g.fillRect(0, 0, rec.width, rec.height);
            g.setColor(staryKolor);
        }
    } 
        
    private Point getKrol(boolean biale) {
        if (biale) return k_b;
        else return k_c;
    }
    
    public void setKrol(boolean biale, Point p) {
        if (biale) k_b = p;
        else k_c = p;
    }
    
    public void przesunFigure(Point p1, Point p2) {
        fig[p2.x][p2.y] = fig[p1.x][p1.y];
        fig[p1.x][p1.y] = null;
        repaint();
    }
    
    public void zmienFigure(Point p, int naCo, boolean b) {
        Figura nowa;
                
        switch(naCo) {
            case 1: nowa = new Wieza(b); break;
            case 2: nowa = new Skoczek(b); break;
            case 3: nowa = new Goniec(b); break;
            case 4: nowa = new Hetman(b); break;
            default: nowa = new Pionek(b); break;
        }
        
        fig[p.x][p.y] = nowa;
        repaint();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}