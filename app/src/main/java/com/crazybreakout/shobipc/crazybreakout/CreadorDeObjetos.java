package com.crazybreakout.shobipc.crazybreakout;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ShobiPc on 28/02/2016.
 */
public class CreadorDeObjetos {

    // creamos el hashMap para trabajar de mejor manera al actualizar los hilos

    private ConcurrentHashMap<String, MostrarObjetos> objetos = new ConcurrentHashMap
            <String, MostrarObjetos>();
// creamos este metodo para añadir los objetos al canvas
    public void añadir (String nombre, MostrarObjetos objeto){
        objetos.put(nombre,objeto);
    }
    //metodo para hacer un get del nombre del objeto
    public MostrarObjetos get(String nombre){return objetos.get(nombre);}


    //este metodo se encarga de  ir dibujando los objetos en el canvas

    public void dibujarTodo (SurfaceHolder ourHolder, Canvas canvas,Paint paint){
        if(ourHolder.getSurface().isValid()){
            canvas=ourHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
        }
        //se van dibujando los  objetos
        for (ConcurrentHashMap.Entry<String,MostrarObjetos> entry: objetos.entrySet()){
            MostrarObjetos valor = entry.getValue();
            valor.draw(canvas,paint);
        }
        CrazyBreakoutGame.CrazyBreakoutView.dibujarPuntuacion(canvas,paint);
        ourHolder.unlockCanvasAndPost(canvas);
    }
    //este metodo actualiza a todos los objetos
    public void actualizaTodo(long fps,long elapsedTime){
        for (ConcurrentHashMap.Entry<String,MostrarObjetos> entry : objetos.entrySet()){
            MostrarObjetos valor = entry.getValue();
            valor.actualizar(fps,elapsedTime);
        }
    }
    //este metodo resetea todos los objetos
    public void resetearTodo (){
        for (ConcurrentHashMap.Entry<String,MostrarObjetos> entry: objetos.entrySet()){
            MostrarObjetos valor = entry.getValue();
            valor.resetear();
        }
    }



}


