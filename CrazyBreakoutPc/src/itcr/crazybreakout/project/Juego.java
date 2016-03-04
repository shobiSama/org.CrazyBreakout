package itcr.crazybreakout.project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
 
import javax.swing.JPanel;
import javax.swing.Timer;
 
import itcr.crazybreakout.project.Bloques.Type;
 /**
  * 
  * @author JoseRivera7
  *
  */
public class Juego extends JPanel implements ActionListener, MouseMotionListener, MouseListener, KeyListener {
    
    private static final long serialVersionUID = -5699255769305413877L;//no se que es pero  lo ocupo porque el canvas me da error.
    public static final int HEIGHT = 920;
    public static final int WIDTH = 1080;
     
    private int horizontalCount;
    private BufferedImage image;
    private Graphics2D bufferedGraphics;
    private Timer time;
    private static final Font endFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    private static final Font scoreFont = new Font(Font.SANS_SERIF, Font.BOLD, 15);
     
    private Barra player;
    private Bola bola;
    ArrayList<ArrayList<Bloques> > bloques;
    /**
     * Prepara la pantalla , se centra la barra y la pelota.
     * El balón se encuentra en el centro de la barra , y la paleta se encuentra en el centro de la pantalla
     */
     
    public Juego(){
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        bufferedGraphics = image.createGraphics();
        time = new Timer(15, this);
        player = new Barra((WIDTH/2)-(Barra.ANCHO_BARRA/2));
        bola = new Bola(((player.getX() + (Barra.ANCHO_BARRA / 2)) - (Bola.DIAMETER / 2)), 
                (Barra.Y_POS - (Bola.DIAMETER + 10)), -5, -5);
         
        bloques = new ArrayList<ArrayList<Bloques> >();
        horizontalCount = WIDTH / Bloques.ALTURA_BLOQUES;
        for(int i = 0; i < 8; ++i){ //se agregan bloques a la fila
            ArrayList<Bloques> temp = new ArrayList<Bloques>(); //array de los bloques para crearlos en loop
            Type rowColor = null;
            switch(i){             //Son 10bloques la hubicacion del tipo de bloque en el canvas esta definida aqui
                case 0:				//depende del case en que este i ahi dibuja el tipo de bloque
                case 2:
                    rowColor = Type.LOW;
                    break;
                case 1:
                case 3:
                case 5:
                    rowColor = Type.UNO;
                    break;
                case 4:
                case 6:
                    rowColor = Type.DOS;
                    break;
                case 7:
                default:
                    rowColor = Type.TRES;
                    break;
            }
            for(int j = 0; j < horizontalCount; ++j){ //Se agregan bloques en columna
                Bloques tempBrick = new Bloques((j * Bloques.ALTURA_BLOQUES), ((i+2) * Bloques.ALTURA_BLOQUES), rowColor);
                temp.add(tempBrick);
            }
            bloques.add(temp);
            addMouseMotionListener(this);
            addMouseListener(this);
            addKeyListener(this);
            requestFocus();
        }
    }
     /**
      * Repinta el canvas primero generando un chequeo si algun bloque es del tipo dead. lo elimina y vuelve a pintar
      */
    @Override public void actionPerformed(ActionEvent e){
        checkCollisions();
        bola.move();
        for(int i = 0; i < bloques.size(); ++i){
            ArrayList<Bloques> al = bloques.get(i);
            for(int j = 0; j < al.size(); ++j){
                Bloques b = al.get(j);
                if(b.dead()){         //dead funcion que devuelve true si las vidas del bloque son 0
                    al.remove(b);	//remueve ese bloque de el arreglo
                }
            }
        }
        repaint();				//actualiza el canvas
    }
 
    /**    
     *Comprueba la existencia de cualquier colisión,
     *si la pelota golpea la pared superior, 
     *o las paredes laterales que cambia de dirección. Si la pelota pasa por debajo de la paleta, 
     *la posición de la pelota se restablece y el jugador pierde una vida
     */
    private void checkCollisions() {
        if(player.hitPaddle(bola)){
            bola.setDY(bola.getDY() * -1);
            return;
        }
        //Primero ve si la bola ha pegado en alguna de las paredes
        if(bola.getX() >= (WIDTH - Bola.DIAMETER) || bola.getX() <= 0){
            bola.setDX(bola.getDX() * -1);
        }
        if(bola.getY() > (Barra.Y_POS + Barra.ALTO_BARRA + 10)){
            resetBall();
        }
        if(bola.getY() <= 0){
            bola.setDY(bola.getDY() * -1);
        }
         
        //Luego la colision con los bloques
        int brickRowsActive = 0;
        for(ArrayList<Bloques> alb : bloques){
            if(alb.size() == horizontalCount){
                ++brickRowsActive;
            }
        }
         
        for(int i = (brickRowsActive==0) ? 0 : (brickRowsActive - 1); i < bloques.size(); ++i){
            for(Bloques b : bloques.get(i)){
                if(b.hitBy(bola)){
                    player.setScore(player.getScore() + b.getBrickType().getPoints());
                    b.decrementType();
                }
            }
        }
    }
     
    /**
     * Coloca la bola aproximadamente en el centro de la pantalla
     * 
     */
    private void resetBall() {
        if(gameOver()){
            time.stop();
            return;
        }
        bola.setX(WIDTH/2);
        bola.setY((HEIGHT/2) + 80);
        player.setLives(player.getLives() - 1);
        player.setScore(player.getScore() - 10);
    }
     
    private boolean gameOver() {
        if(player.getLives() <= 1)
            return true;
        return false;
    }
 
    /**
     *  Dibuja la pantalla y todos los componentes para el juego,
     *  primero coloca la pantalla en blanco(o el colr qe se defina) 
     * Finalmente dibuja la barra la bola y los bloques sobre el lienzo.
     */
    @Override public void paintComponent(Graphics g){
        super.paintComponent(g);
        bufferedGraphics.clearRect(0, 0, WIDTH, HEIGHT);
        bufferedGraphics.setColor(Color.LIGHT_GRAY);
        bufferedGraphics.fillRect(0, 0, WIDTH, HEIGHT);
        player.drawPaddle(bufferedGraphics);
        bola.drawBall(bufferedGraphics);
        for(ArrayList<Bloques> row : bloques){
            for(Bloques b : row){
                b.drawBrick(bufferedGraphics);
            }
        }
        bufferedGraphics.setFont(scoreFont);
        bufferedGraphics.drawString("Score: " + player.getScore(), 10, 25);//se pinta el score
        bufferedGraphics.drawString("Live: " + player.getLives(), 500, 25);//se pintan las vidas
        //en caso de perder
        if(gameOver() &&
                bola.getY() >= HEIGHT){
            bufferedGraphics.setColor(Color.RED);
            bufferedGraphics.setFont(endFont);
            bufferedGraphics.drawString("PERDISTE!  Score: " + player.getScore(), (WIDTH/2) - 85, (HEIGHT/2));
        }
        //si el arreglo de los bloques queda en 0 quiere decir que todos fueron eliminados
        //se ejecuta la funcion empty que devueve un booleano si este es True quiere decir
        //que no hay bloques por lo tanto el jugador gano la partida
        if(empty()){
            bufferedGraphics.setColor(Color.RED);
            bufferedGraphics.setFont(endFont);
            bufferedGraphics.drawString("GANASTE!  Score: " + player.getScore(), (WIDTH/2) - 85, (HEIGHT/2));
            time.stop();
        }
        g.drawImage(image, 0, 0, this);
        Toolkit.getDefaultToolkit().sync();
    }
     
     
 
    private boolean empty() {
        for(ArrayList<Bloques> al : bloques){
            if(al.size() != 0){
                return false;
            }
        }
        return true;
    }
 
    @Override public void mouseMoved(MouseEvent e){
        player.setX(e.getX() - (Barra.ANCHO_BARRA / 2));
    }
     
    @Override public void mouseDragged(MouseEvent e){}
     
    @Override public void mouseClicked(MouseEvent e){
        if(time.isRunning()){
            return;
        }
        time.start();
    }
     
    
 
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
         
    }

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
