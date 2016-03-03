package com.crazybreakout.shobipc.crazybreakout;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by ShobiPc on 19/02/2016.
 */
public class MostrarObjetos {
    private RectF rect;
    private float ancho;
    private float largo;
    private float x;
    private float y;

    // posiciones  iniciales de los objetos

    private float inicialX;
    private float inicialY;
    //se fija la posicion del rectangulo y se delimita
    public void setPosition(float x,float y ){
        this.x=x;
        this.y=y;
        RectF temp = new RectF(x,y,x+ancho,y+largo);
        setBoundingRect(temp);
    } //se fija la posicion inicial del rectangulo y se delimita
    public void setPosicionIni(float x, float y){
        this.x = x; inicialX = x;
        this.y = y; inicialY = y;
        RectF temp = new RectF(x, y, x+ancho, y+largo);
        setBoundingRect(temp);
    }

    //se fija las dimenciones del rectangulo
    public void setSize(float ancho, float largo){
        this.ancho=ancho;
        this.largo=largo;
        rect= new RectF(x,y,x+ancho,y+largo);
    }
    public void setBoundingRect(float actualizarX){
        rect.left=actualizarX;
        rect.right=actualizarX+ancho;

    }
    //se crean las delimitaciones del rectangulo
    public void setBoundingRect(RectF r){
        rect.right=r.right;
        rect.left=r.left;
        rect.top=r.top;
        rect.bottom=r.bottom;

    }
    public void resetear(){
        setPosition(inicialX,inicialY);
        setSize(ancho,largo);
    }
    // get de las posiciones x y y
    public PointF getPosition(){
        PointF  pos =new PointF();
        pos.x=x;
        pos.y=y;
        return pos;
    }
    //se crar el canvas donde se van a dibujar los objetos
    public void draw(Canvas canvas,Paint paint){
        canvas.drawRect(rect,paint);

    }
    public float getAncho(){return ancho;}
    public float getLargo(){return largo;}
    public RectF getBoundingRect(){return rect;}
    public void actualizar (long fps, long elapsedTime){}

}

