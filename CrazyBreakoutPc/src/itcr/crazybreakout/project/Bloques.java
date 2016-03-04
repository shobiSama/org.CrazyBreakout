package itcr.crazybreakout.project;

import java.awt.Color;
import java.awt.Graphics2D;
 /**
  * 
  * @author JoseRivera7
  *
  */
public class Bloques {
    public static final int ALTURA_BLOQUES = 30;// Altura de los bloques
    public static final int ANCHO_BLOQUES = 10;//Ancho de los bloques
    private int     xPos, yPos; //Posicion en x y para los bloques
    private Type    TipoBloque;// tipo de bloque ULTRA,HIGHT,MEDIUM,LOW,DEAD
     
    enum Type{ //caracteristicas de cada bloque dependiendo a su instaciancion (life,points,Color)
        TRES   (6, 12, Color.BLACK),
        DOS    (3, 15, Color.WHITE), 
        UNO (2, 15, Color.DARK_GRAY),
        LOW     (1, 10, Color.WHITE),
        DEAD    (0, 0, Color.WHITE);
        private int vidas;
        private Color color;
        private int puntos;
         
        Type(int life, int points, Color color){
            this.vidas = life;
            this.puntos = points;
            this.color = color;
        }
        public int getPoints(){ return puntos;  }
        public Color getColor(){    return color;   }
        public int getLife(){   return vidas;    }
    }
     
    public Bloques(int xPos, int yPos, Type brickType){
        this.xPos = xPos;
        this.yPos = yPos;
        this.TipoBloque = brickType;
    }
     
    public int getX(){  return xPos;    }
    public int getY(){  return yPos;    }
    public Type getBrickType(){ return TipoBloque;   }
     /*
      * 
      */
    public boolean hitBy(Bola b){
        //chequea si la bola colisiono con un borde abajo o arriba
        if(b.getX() <= (xPos + ALTURA_BLOQUES) && b.getX() >= xPos){
            //colision abajo
            if(b.getY() <= (yPos + ALTURA_BLOQUES) && b.getY() >= (yPos + (ALTURA_BLOQUES / 2))){
                b.setDY(b.getDY() * -1);
                return true;
            }
            //colision arriba
            else if(b.getY() >= (yPos - Bola.DIAMETER) && b.getY() < (yPos + (Bola.DIAMETER / 3))){
                b.setDY(b.getDY() * -1);
                return true;
            }
        }
        //con los bordes
        else if(b.getY() <= (yPos + ALTURA_BLOQUES) && b.getY() >= yPos){
            //colision derecha
            if(b.getX() <= (xPos + ALTURA_BLOQUES) && b.getX() > (xPos + (ALTURA_BLOQUES - (Bola.DIAMETER / 2)))){
                b.setDX(b.getDX() * -1);
                return true;
            }
            //colision izquierda
            else if(b.getX() >= (xPos - Bola.DIAMETER) && b.getX() < (xPos + (Bola.DIAMETER / 2))){
                b.setDX(b.getDX() * -1);
                return true;
            }
        }
        return false;
    }
     /**
      * Decrementa la vida de los bloques al ser colisionados por la bola
      *
      */
    public void decrementType(){
        switch(TipoBloque.vidas){
            case 6:
            case 5:
            case 4:
                --TipoBloque.vidas;
                break;
            case 3:
                TipoBloque = Type.UNO;
                break;
            case 2: 
                TipoBloque = Type.LOW;
                break;
            case 1:
            default:
                TipoBloque = Type.DEAD;
                break;
        }
    }
     
    public void drawBrick(Graphics2D g){
        g.setColor(Color.white);
        g.fillRect(xPos, yPos, ALTURA_BLOQUES, ALTURA_BLOQUES);
        g.setColor(TipoBloque.color);
        g.fillRect((xPos+2), (yPos+2), ALTURA_BLOQUES-4, ALTURA_BLOQUES-4);
        g.setColor(Color.black);
        g.drawRect((xPos+2), (yPos+2), ALTURA_BLOQUES-4, ALTURA_BLOQUES-4);
    }
 
    public boolean dead() {
        if(TipoBloque.vidas == 0)
            return true;
        return false;
    }
}
