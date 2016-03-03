package com.crazybreakout.shobipc.crazybreakout;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by ShobiPc on 28/02/2016.
 */
public class Bloque extends MostrarObjetos {
    private boolean visible;
    private int color1,color2,color3,color4;

    public Bloque(){
        visible = true;
    }

// se crean get y set para saber si fue destruido o  es visible aun
    public void setDestruido(){
        visible=false;
    }
    public  boolean getVisibilidad(){
        return  visible;
    }
    // se dibujan los bloque es pantalla
    public void draw(Canvas canvas,Paint paint){
        Random color =new Random();
        color1 = color.nextInt(255);
        color2= color.nextInt(255);
        color3= color.nextInt(255);
        color4= color.nextInt(255);
        paint.setColor(Color.argb(color1, color2, color3, color4));
        if (this.visible){
            super.draw(canvas,paint);
        }
    }
    //metodo para resetear los bloques
    @Override
    public void resetear(){
        visible=true;}
}
