package com.crazybreakout.shobipc.crazybreakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by ShobiPc on 28/02/2016.
 */
public class Puntuacion {
    private int puntacion;
    private int vidas;
    private int puntacioMax;
    private  int screenX;
    private int screenY;
    enum ResultadoJuego{gano,perdio,jugando}
    private ResultadoJuego resultado;

    Puntuacion(int numBloques){
        puntacion = 0;
        vidas = 3;
        puntacioMax = numBloques*10;

        screenX=CrazyBreakoutGame.CrazyBreakoutView.screenX;
        screenY =  CrazyBreakoutGame.CrazyBreakoutView.screenY;
        resultado =ResultadoJuego.jugando;
    }
    // se dibuja el resultado en la pantalla principal
    public  void draw (Canvas canvas, Paint paint) {
        paint.setColor(Color.argb(255, 255, 255, 255));
        paint.setTextSize(25);
        String Puntuacion = "Score" + puntacion;
        Rect limit = new Rect();
        paint.getTextBounds(Puntuacion, 0, Puntuacion.length(), limit);
        canvas.drawText("Score:" + puntacion, 10, screenY/2, paint);

        String vid = "vidas:" + vidas;
        paint.getTextBounds(vid, 0, vid.length(), limit);
        canvas.drawText(vid, screenX - limit.width() - 15, screenY/2, paint);
        // si el jugador gana
        if (resultado == ResultadoJuego.gano) {
            paint.setTextSize(screenY / 6);
            String ganar = "HAS GANADO";
            paint.getTextBounds(ganar, 0, ganar.length(), limit);
            canvas.drawText(ganar,screenX/2-500, screenY/2, paint);
        }
        //si el jugador pierde
        if (resultado == ResultadoJuego.perdio) {
            paint.setTextSize(screenY / 6);
            String perder = "HAS PERDIDO";
            paint.getTextBounds(perder, 0, perder.length(), limit);
            canvas.drawText(perder, screenX/2-500, screenY/2, paint);
        }
    }

    public ResultadoJuego getResultadoJuego(){return resultado;}
    // se crea el incremento de puntuacion y si tiene la puntuacion maxima su estado cambia a "gano"
    public void incrementarPuntuacion(){
        puntacion+= 10;
        if (puntacion==puntacioMax){
            resultado = ResultadoJuego.gano;
        }
    }
    //se crea el metodo perder vidas y su estado cambia a "perdio"
    public void pierdeVidas(){
        vidas-=1;
        if (vidas <=0){
            resultado=ResultadoJuego.perdio;
        }
    }
    //se crea el metodo  para resetear los puntos y las vidas y su estado cambia a "jugando"
    public void ResetPun(){
        puntacion=0;
        vidas=3;
        resultado=ResultadoJuego.jugando;
    }

}

