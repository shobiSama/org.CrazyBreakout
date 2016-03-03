package com.crazybreakout.shobipc.crazybreakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by ShobiPc on 28/02/2016.
 */
public class Raqueta extends MostrarObjetos {
    private float velocidad;
    private float velocidadInicial;
    private int screenX;
    private int screenY;
    public enum EstadoDeMovimiento{izquierda,derecha,detenida}
    private EstadoDeMovimiento estadoDeMovimiento;

    Raqueta(){
        velocidadInicial=380.0f;
        velocidad=velocidadInicial;
        setSize(130,20);
        estadoDeMovimiento=EstadoDeMovimiento.detenida;
        screenX=CrazyBreakoutGame.CrazyBreakoutView.screenX;
        screenY=CrazyBreakoutGame.CrazyBreakoutView.screenY;
    }
    @Override
    public  void actualizar (long fps,long tiempoTrascurrido) {
        PointF locali = getPosition();
        //colision con los limites de la pantalla
        if (locali.x < 0.0f) {
            if (estadoDeMovimiento == EstadoDeMovimiento.izquierda) {
                velocidad = 0;
            } else {
                velocidad = velocidadInicial;
            }
        }
            if (locali.x + getAncho() > screenX) {
                if (estadoDeMovimiento == EstadoDeMovimiento.derecha) {
                    velocidad = 0;
                } else {
                    velocidad = velocidadInicial;
                }

            }
            if (estadoDeMovimiento == EstadoDeMovimiento.derecha) {
                locali.x = locali.x + velocidad / fps;
            }
            if (estadoDeMovimiento == EstadoDeMovimiento.izquierda) {
                locali.x = locali.x - velocidad / fps;
            }
            setPosition(locali.x, locali.y);

        }

    public void setEstadoDMovimiento(EstadoDeMovimiento estado){
        estadoDeMovimiento=estado;}

    public EstadoDeMovimiento getEstadoDMovimiento(){return
            estadoDeMovimiento;}
    //se dibuja la raqueta en canvas
    public void draw (Canvas canvas,Paint paint){
        paint.setColor(Color.argb(255,255,255,255));
        super.draw(canvas,paint);
    }
    // al ganar o perder se reinicia la raqueta
    public void resetear(){
        super.resetear();
        velocidad=velocidadInicial;
        estadoDeMovimiento=EstadoDeMovimiento.detenida;
    }

}
