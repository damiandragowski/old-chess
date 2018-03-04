
package klientserwer;
import java.io.*;

public class Czytacz {
    BufferedReader br;
    
    /** Creates a new instance of Czytacz */
    public Czytacz(InputStream in) {
        InputStreamReader isr = new InputStreamReader(in);
        br = new BufferedReader(isr);
    }
    
    public String czytajPaczke() throws IOException{
        return br.readLine();
    }
    
    public void wylacz() {
        try {
            br.close();
        } catch (IOException e) {
            System.out.println("Bl;ad struminia");
        }
    }
    
}
