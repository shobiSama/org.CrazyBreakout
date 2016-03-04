package itcr.crazybreakout.project;

import java.awt.Color;
import java.awt.Graphics2D;

public class Barra {
	    public static final int Y_POS = Juego.HEIGHT - 30;
	    public static final int ANCHO_BARRA = 80;
	    public static final int ALTO_BARRA = 10;
	    public static final Color COLOR_BARRA = Color.black;
	    private int xPos;
	    public static final int DELTA_X = 5;
	    private int score;
	    private int lives;
	    public Barra(int xPos){
	        this.xPos = xPos;
	        score = 0;
	        lives = 5;
	    }
	     
	    public void setX(int xPos){ 
	        this.xPos = xPos;
	        if(xPos < 0) this.xPos = 0;
	        if(xPos > (Juego.WIDTH - ANCHO_BARRA)) this.xPos = (Juego.WIDTH - ANCHO_BARRA);
	    }
	     
	    public int getX(){ return xPos; }
	    public int getScore(){ return score; }
	    public void setScore(int score){ this.score = score; }
	    public int getLives(){ return lives; }
	    public void setLives(int lives){ this.lives = lives; }
	     
	/**
	 * Determina si una pelota ha golpeado a la barra
	 * Comprueba si el balón está en el rango Xde la barra
	 * omprobaciones siguientes si es en realidad que golpea la pala . 
	 * si pelota.y + el diámetro está cerca de la posición de la barra y el método devuelve true
	 * @param b
	 * @return
	 */
	    public boolean hitPaddle(Bola b){
	        if(b.getX() <= xPos + (ANCHO_BARRA + 15)){
	            if(b.getX() >= xPos - 10){
	                if((b.getY() + (Bola.DIAMETER - 1)) >= (Y_POS)){
	                    if((b.getY() + (Bola.DIAMETER - 1)) <= (Y_POS + (ALTO_BARRA - 5))){
	                        return true;
	                    }
	                }
	            }
	        }
	        return false;
	    }
	    /***
	     * Dibuja la Barra
	     * @param g
	     */
	     
	    public void drawPaddle(Graphics2D g){
	        g.setColor(COLOR_BARRA);
	        g.fillRect(xPos, Y_POS, ANCHO_BARRA, ALTO_BARRA);
	        g.setColor(Color.WHITE);
	        g.drawRect(xPos, Y_POS, ANCHO_BARRA, ALTO_BARRA);
	    }
	     
	    public static void main(String[] args){
	        Bola b = new Bola(110, (Y_POS - (Bola.DIAMETER - 5)), 5, 5);
	        Barra p = new Barra(110);
	        for(int i = 1; i <= ANCHO_BARRA; ++i){
	            b.setX(b.getX() + 1);
	            System.out.println(p.hitPaddle(b));
	        }
	        System.out.println(p.hitPaddle(b));
	    }
	}


