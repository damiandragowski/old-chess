
package klientserwer;
import java.io.*;

public class Pisarz {
    BufferedWriter bw;
    
    public Pisarz(OutputStream out) {
        OutputStreamWriter osw = new OutputStreamWriter(out);
        bw = new BufferedWriter(osw);
    }
    
    public void wyslijPaczke(String s) throws IOException{
        s.replace('\n', ' ');
        s.replace('\r', ' ');
        bw.write(s);
        bw.newLine();
        bw.flush();
    }
    
    public void wylacz() {
        try {
            bw.close();
        } catch (IOException e) {
            System.out.println("Error: Nie udało się zamknąć strumienia pisarza");
        }
    }
    
}
