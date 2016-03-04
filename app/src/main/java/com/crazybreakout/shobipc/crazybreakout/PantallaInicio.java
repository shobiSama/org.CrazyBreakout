package com.crazybreakout.shobipc.crazybreakout;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import java.util.Random;
import java.util.Vector;

/**
 * Created by ShobiPc on 22/02/2016.
 */
public class PantallaInicio {
    private int screenX, screenY;
    private int color1,color2,color3,color4;
    public enum Estado { Play }
    MenuItem playButton;

    public class MenuItem{
        public Rect rect;
        public Estado action;
    }
    private Vector<MenuItem> botones;

    PantallaInicio(){
        screenX = CrazyBreakoutGame.CrazyBreakoutView.screenX;
        screenY = CrazyBreakoutGame.CrazyBreakoutView.screenY;

        botones = new Vector<MenuItem>();
        Paint paint;
        paint = new Paint();
        paint.setTextSize(230);
// se crea el boton  llamado play y se le da una funcion y se delimita para que  a la hora de la colision con el evento touchs lo reconosca
        String text = "!!!PLAY¡¡¡";
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int height = (int) (paint.descent() - paint.ascent());
        //int dist = screenY/10;
        playButton = new MenuItem();
        playButton.rect = new Rect();
        playButton.rect.left = (screenX / 2) - (bounds.width() / 2);
        playButton.rect.top = (screenY / 2)-150;
        playButton.rect.bottom = playButton.rect.top + height;
        playButton.rect.right = playButton.rect.left + bounds.width();
        playButton.action = Estado.Play;

       /* text = "Exit"
        paint.getTextBounds(text, 0, text.length(), bounds);
        exitButton = new MenuItem();
        exitButton.rect = new Rect();
        exitButton.rect.left = (screenX / 2) - (bounds.width() / 2);
        exitButton.rect.top = (screenY / 2) + dist/2;
        exitButton.rect.bottom = exitButton.rect.top + height;
        exitButton.rect.right = exitButton.rect.left + bounds.width();
        exitButton.action = MenuResult.Exit;
        */
        botones.addElement(playButton);
       // _menuItems.addElement(exitButton);
    }

    public Vector<MenuItem> getMenuItems(){
        return botones;
    }

    public void show(SurfaceHolder holder, Canvas canvas, Paint paint){
        if(holder.getSurface().isValid()){
            canvas = holder.lockCanvas();
            // se crea el random de colores para que la pantalla tenga un efecto de parpadeo y cambio su color al mismo tiempo
            Random color =new Random();
            color1 = color.nextInt(255);
            color2= color.nextInt(255);
            color3= color.nextInt(255);
            color4= color.nextInt(255);
            canvas.drawColor(Color.argb(color1, color2, color3, color4));
            paint.setColor(Color.argb(color2, color4, color3, color1));
            paint.setTextSize(230);

            float height = paint.descent() - paint.ascent();
            float offset = (height / 2) - paint.descent();

            // se dibujo el texto encima del boton de  play
            canvas.drawText("!!!PLAY¡¡¡", playButton.rect.left, playButton.rect.bottom - offset, paint);
            //canvas.drawText("Exit", exitButton.rect.left, exitButton.rect.bottom - offset, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }
}
