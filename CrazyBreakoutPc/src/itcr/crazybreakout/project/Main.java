package itcr.crazybreakout.project;

import javax.swing.JFrame;
/**
 * Se instancias las demas clases para ser ejecutadas
 * @author JoseRivera7
 *
 */
public class Main {
	public static void main(String[] args){
        JFrame frame = new JFrame();
        Juego c = new Juego();
        frame.add(c);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }

}
