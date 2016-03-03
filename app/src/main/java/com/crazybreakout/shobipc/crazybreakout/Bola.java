package com.crazybreakout.shobipc.crazybreakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by ShobiPc on 26/02/2016.
 */
public class Bola extends MostrarObjetos {
    private float velocidad;
    private float velocidadInicial;
    private float angulo;
    private int screenX;
    private int screenY;
    private long TiempoTrascuridoDesdeEmpezo;
    private final long intervaloColision;
    private long tiempoColision;

    Bola() {
        // se declara el tamaño de la bola y su velocidad inicial
        //  y se crea un random del angulo en
        // en que va a salir por primera vez
        setSize(10, 10);
        velocidadInicial = 300;
        velocidad = velocidadInicial;

        Random generador = new Random();
        angulo = generador.nextInt(360);

        if (angulo >= 70.0f && angulo <= 110.0f) {
            angulo += 40;
        }
        if (angulo >= 250.0f && angulo <= 290.0f) {
            angulo -= 40;
        }
        screenX = CrazyBreakoutGame.CrazyBreakoutView.screenX;
        screenY = CrazyBreakoutGame.CrazyBreakoutView.screenY;
        TiempoTrascuridoDesdeEmpezo = 0;
        intervaloColision = 200;
        tiempoColision = 0;
    }
    /// actualizacion  de la bola  en pantalla y a la hora de colisionar
        @Override
        public void actualizar (long fps, long tiempoTrascurrido){
            TiempoTrascuridoDesdeEmpezo+= tiempoTrascurrido;
            if(TiempoTrascuridoDesdeEmpezo < 3000)
                return;
            float velocidadX = velocidad * velocidadLinealX (angulo);
            float velocidadY = velocidad * velocidadLinealY(angulo);
           // se delimita el rectangulo de la bola
            RectF rect  = getBoundingRect();

            float izq =rect.left +(velocidadX /fps);
            float pSup =rect.top+(velocidadY / fps);
            float der = rect.right+(velocidadX/fps);
            float pInf = rect.bottom+(velocidadY/fps);

            // colision con la pared izquierda y la pared derecha

            if (izq <= 0|| der >= screenX ){
                angulo = 360.0f - angulo;
                velocidadX =-velocidadX;

                if (angulo >= 70.0f && angulo <= 110.0f){
                    angulo+=40;
                }
                if (angulo >= 250.0f && angulo <= 290.0f){
                    angulo-=40;
                }
            }
            //colision con la parte superior
            if (pSup<=0){
                velocidadY=-velocidadY;
                angulo=180.0f -angulo;

                if (angulo < 0.0f){
                    angulo+=360.0f;
                }
                if(angulo> 360.0f){
                    angulo =angulo-360.0f;
                }
            }
            //colision con la parte inferior
            if(pInf >= screenY){
                CrazyBreakoutGame.CrazyBreakoutView.getpuntuacion().pierdeVidas();
                boolean resultado =CrazyBreakoutGame.CrazyBreakoutView.validarVictoria();

                if(resultado){
                    return;
                }
                resetear();
            }
            //colisiones con los bloques
            int numBloques =CrazyBreakoutGame.CrazyBreakoutView.getNumBloques();
            for(int i=0; i<numBloques; i++){
                String nombre = "Bloque"+ Integer.toString(i);
                Bloque bloque = (Bloque) CrazyBreakoutGame.CrazyBreakoutView.getObject().get(nombre);
                if(bloque.getVisibilidad()){
                    if(RectF.intersects(bloque.getBoundingRect(), getBoundingRect())){
                        bloque.setDestruido();
                        velocidadY = -velocidadY;

                        angulo = 180.0f - angulo;

                        if(angulo < 0.0f){
                            angulo += 360.0f;
                        }
                        if(angulo > 360.0f){
                            angulo = angulo - 360.0f;
                        }

                        CrazyBreakoutGame.CrazyBreakoutView.getpuntuacion().incrementarPuntuacion();
                        boolean resultado = CrazyBreakoutGame.CrazyBreakoutView.validarVictoria();
                        if(resultado){
                            return;
                        }

                    }
                }
            }
            //colision con la raqueta
            Raqueta raqueta =(Raqueta)CrazyBreakoutGame.CrazyBreakoutView.getObject().get("Raqueta");
            tiempoColision+=tiempoTrascurrido;
            if (raqueta != null){
                RectF rectRaqueta = raqueta.getBoundingRect();
                if(RectF.intersects(rectRaqueta,getBoundingRect())&&tiempoColision > intervaloColision){
                    velocidadY = -velocidadY;
                    //colicion en la orilla
                    if(rectRaqueta.top+raqueta.getLargo()/2<getBoundingRect().bottom){
                       angulo= 360.0f - angulo;
                    }else{//colision con la pSup
                        angulo=180.0f-angulo;
                    }
                    if(angulo < 0.0f){
                        angulo += 360.0f;
                    }
                    if(angulo >= 360.0f){
                        angulo = angulo - 360.0f;
                    }
                    limpiarEnY(raqueta.getBoundingRect().top-2);
                    if(raqueta.getEstadoDMovimiento()==Raqueta.EstadoDeMovimiento.izquierda){
                        angulo-=30.0f;
                        if(angulo<0.0f){
                            angulo+=360.0f;
                        }

                    }
                    else if(raqueta.getEstadoDMovimiento()==Raqueta.EstadoDeMovimiento.derecha){
                        angulo+=30.0f;
                        if(angulo>360.0f){
                            angulo=angulo-360.0f;
                        }
                    }
                    velocidad+=20.0f;
                    tiempoColision=0;

                }
            }
            // se define tanto la parte iquierda derecha y superior e inferior de la bola
            rect.left = rect.left + (velocidadX / fps);
            rect.top = rect.top + (velocidadY / fps);
            rect.right = rect.left + getAncho();
            rect.bottom = rect.top + getLargo();
           // se setea su posicion en pantalla
            setPosition(rect.left, rect.top);


        }
    // se crean estos metodos para calcular la velocidad "para saber el tiempo de colision" y
    // el angulo que va a tomar la bola
    private float velocidadLinealX(float angulo2){
        angulo2 -=90;
        if(angulo2<0){
            angulo2+=360;
        }
        return (float)Math.cos(angulo2*3.142/180.0);
    }
    private float velocidadLinealY(float angulo2){
        angulo2 -= 90;
        if(angulo2 < 0){
            angulo2 += 360;
        }
        return (float)Math.sin(angulo2 * 3.14159 / 180.0);
    }
    // metodo que resetea todos los valores a la hora de perder una vida
    public void resetear(){
        super.resetear();
        velocidad = velocidadInicial;
        TiempoTrascuridoDesdeEmpezo = 0;

        Random generator = new Random();
        angulo = generator.nextInt(360);
        if(angulo >= 70.0f && angulo <= 110.0f){
            angulo += 40;
        }
        if(angulo >= 250.0f && angulo <= 290.0f){
            angulo -= 40;
        }
    }
    //metodo que dibuja la bola en el canvas principal
    public void draw(Canvas canvas, Paint paint){
        paint.setColor(Color.argb(255, 255, 255, 255));
        super.draw(canvas, paint);
    }

    public void limpiarEnY(float y){
        RectF rect = getBoundingRect();
        rect.bottom = y;
        rect.top = y - getLargo();
        setBoundingRect(rect);
    }

}

