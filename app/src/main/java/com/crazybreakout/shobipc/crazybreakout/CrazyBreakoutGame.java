package com.crazybreakout.shobipc.crazybreakout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.Iterator;
/**
 * Created by ShobiPc on 17/02/2016.
 */

public class CrazyBreakoutGame extends Activity {
    //vista del juego
    CrazyBreakoutView crazyBreakoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        crazyBreakoutView = new CrazyBreakoutView(this);
        setContentView(crazyBreakoutView);
    }//se crea una variable para darle un estado a lo que pasa en el juego
    //para un mejor control de los metodos que se van a utilizar
    // ademas para poder realizar ordenes de manera ordenada dependiendo del estado del juego y
    //para un mejor  uso de los hilos
    private enum estadoDelJuego {jugando,  completado, salirse, verMenu}

    public static class CrazyBreakoutView extends SurfaceView implements Runnable {
        static Thread hiloPrincipal = null;
        private static SurfaceHolder ourHolder;
        private static Canvas canvas;
        private static Paint paint;

        private long fps;
        private long tiempoTrascurrido;
        public static int screenX, screenY;
        private static estadoDelJuego gameState;

        private static CreadorDeObjetos creadorDeObjetos;
        private static Puntuacion puntuacion;
        private static Bola bola;
        private static Raqueta raqueta;
        ///////////////////////////////////////////////
        private static PantallaInicio menuPrincipal;


        //se crea el rectangulo del boton de play
        Rect playBottom;
        // se crea el areglo de los bloques como la variable que va a contener el numero de bloques que se crean
        Bloque[] bloques;
        private static int numBloques;

        private CrazyBreakoutView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenX = size.x;
            screenY = size.y;

            //////////////////////////////////////////////
            gameState =estadoDelJuego.verMenu;

            //se instancian los objetos

            creadorDeObjetos =new CreadorDeObjetos();
            menuPrincipal = new PantallaInicio();


            //se instancia la raqueta y se setea la posicion en que se va a dibujar en el canvas
            raqueta= new Raqueta();
            raqueta.setPosicionIni(screenX / 2 - 65, screenY - 30);
            creadorDeObjetos.añadir("Raqueta",raqueta);
            //se instancia la bola y se setea la posicion en que se va a dibujar en el canvas
            bola=new Bola();
            bola.setPosicionIni(screenX / 2, screenY/2+250);
            creadorDeObjetos.añadir("Bola",bola);
            // se instancian los bloques y se setea la posicion en que se va a dibujar en el canvas
            bloques=new Bloque[200];
            numBloques=0;
            crearBloques();
            //se instancia la puntuacion y las vidas
            puntuacion=new Puntuacion(numBloques);

            //se instancia el boton de play
            playBottom = new Rect();
            ///////////////////
            fps=0;
            tiempoTrascurrido=0;

        }
        // se crean metodos para controlar el flujo del juego
        @Override
        public void run(){
            while(!isExiting()){
                GameLoop();
            }
        }

        private boolean isExiting(){
            if(gameState == estadoDelJuego.salirse)
                return true;
            else
                return false;
        }
        // se crea el bucle principal del juego
        private void GameLoop(){
            switch(gameState){
                /////////////////////////////////////

                case verMenu:
                    MostarMenuPrincipal();
                    break;

                /////////////////////////////////////
                case completado:
                    finDelJuego();
                    break;
                case jugando:

                    long startFrameTime = System.currentTimeMillis();

                    creadorDeObjetos.actualizaTodo(fps, tiempoTrascurrido);
                    creadorDeObjetos.dibujarTodo(ourHolder, canvas, paint);

                    tiempoTrascurrido = System.currentTimeMillis() - startFrameTime;
                    if(tiempoTrascurrido > 1){
                        fps = 1000 / tiempoTrascurrido;
                    }
                    break;
                default:
                    break;
            }
        }


        private void resume() {
            if (gameState == estadoDelJuego.salirse) {
                gameState = estadoDelJuego.jugando;
            } else {
                gameState = estadoDelJuego.verMenu;

                hiloPrincipal = new Thread(this);
                hiloPrincipal.start();
            }
        }
        // se crea el metodo para verificar los touchs en pantalla
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent){
            switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){

                // en el caso que el jugador toque la pantalla
                case MotionEvent.ACTION_DOWN:
                    if(gameState == estadoDelJuego.jugando) {
                        if (motionEvent.getX() > screenX / 2) {
                            raqueta.setEstadoDMovimiento(Raqueta.EstadoDeMovimiento.derecha);
                        } else {
                            raqueta.setEstadoDMovimiento(Raqueta.EstadoDeMovimiento.izquierda);
                        }
                    }

                    else if(gameState == estadoDelJuego.verMenu){
                        float xPos = motionEvent.getX();
                        float yPos = motionEvent.getY();

                        Iterator itr = menuPrincipal.getMenuItems().iterator();
                        while(itr.hasNext()){
                            PantallaInicio.MenuItem button = (PantallaInicio.MenuItem)itr.next();
                            playBottom = button.rect;
                            if(xPos > playBottom.left && xPos < playBottom.right &&
                                    yPos > playBottom.top && yPos < playBottom.bottom){
                                switch (button.action){

                                    case Play:
                                        gameState = estadoDelJuego.jugando;

                                        break;
                                }
                            }
                        }
                    }




                    else if(gameState == estadoDelJuego.completado){
                        resetearJuego();
                        gameState = estadoDelJuego.verMenu;
                    }
                    break;

                // player removed finger from screen
                case MotionEvent.ACTION_UP:
                    if(gameState == estadoDelJuego.jugando) {
                        raqueta.setEstadoDMovimiento(Raqueta.EstadoDeMovimiento.detenida);
                    }
                    break;
            }
            return true;
        }
         // este metodo es el encargado de crear los bloques de una forma recursiba
        private void crearBloques(){
            int anchoBlo = screenX/8;
            int largoBlo = screenY/10;
            float x,y;
            int relleno =1;
            numBloques=0;
            int offset = 30;
            //estos for se encargar de verificar e ir creando los bloques por filas y columnas
            for (int fila = 0; fila <6;fila++){
                for (int columna = 0 ; columna<8; columna++){
                    bloques[numBloques]=new Bloque();
                    x=columna*anchoBlo+relleno;
                    y=offset+(fila*largoBlo)+relleno;
                    bloques[numBloques].setSize(anchoBlo-relleno,largoBlo-relleno);
                    bloques[numBloques].setPosition(x, y);
                    creadorDeObjetos.añadir("Bloque"+Integer.toString(numBloques),bloques[numBloques]);
                    numBloques++;
                }

            }
        }
        // este metodo se encarga de dibujar la puntuacion sobre el canvas principal
        public static void dibujarPuntuacion(Canvas canbas, Paint pait){
            puntuacion.draw(canbas, pait);
        }
        //////////////////////////////////////////////////////
        // este metodo se encarga de monstar la pantalla de inicio
        public static void MostarMenuPrincipal(){
            PantallaInicio mainMenu = new PantallaInicio();
            mainMenu.show(ourHolder, canvas, paint);
        }


        //////////////////////////////////////////////////////
        // en este metodo se verifica si el estado del juego es completado y se muestra la pantalla
        //la leyenda "HAS GANADO"
        public static void finDelJuego(){
            if (ourHolder.getSurface().isValid()){
                canvas=ourHolder.lockCanvas();
                canvas.drawColor(Color.BLACK);
                dibujarPuntuacion(canvas, paint);
                ourHolder.unlockCanvasAndPost(canvas);

            }
        }
        // se crean get  nesesarios para enviar objetos al creador de objetos y actualizar la puntuacion
        public  static  CreadorDeObjetos getObject (){
            return creadorDeObjetos;
        }
        public static Puntuacion getpuntuacion(){return puntuacion;}
        public static boolean validarVictoria() {
            if (puntuacion.getResultadoJuego() != Puntuacion.ResultadoJuego.jugando) {
                gameState = estadoDelJuego.completado;
                return true;
            } else {

                return false;
            }
        }
        public static int getNumBloques(){
            return numBloques;}

        public void resetearJuego(){
            puntuacion.ResetPun();
            creadorDeObjetos.resetearTodo();
        }


    }
    //metodos principales del SurfaceView
    @Override
    protected void onResume(){
        super.onResume();
        crazyBreakoutView.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    protected void onStop(){
        super.onStop();

    }


}
