package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private GestureDetector gestureDetector;
    // Tiempo que se mantiene el gesto
    private static final int SWIPE_THRESHOLD = 100;
    // Velocidad del gesto
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    TextView[][] listaCasillas;
    Button newGame;
    TextView score;
    TextView best_score;
    int valor_score;
    // Fila y columna de numero generado aleatoriamente en el tablero
    int nfila_aleatorio;
    int ncolumna_aleatorio;
    // Boolean para comprobar si un numero se ha movido en el tablero
    boolean mover = false;
    DataBase db;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DataBase(this);
        this.gestureDetector = new GestureDetector(this, this);
        gestureDetector.setOnDoubleTapListener(this);

        user = "Guest";
        score = (TextView) findViewById(R.id.score);
        best_score = (TextView) findViewById(R.id.best_score);
        best_score.setText("BEST \n "+ db.getBestScore(user));

        listaCasillas = new TextView[4][4];
        // Fila 1
        listaCasillas[0][0] = (TextView) findViewById(R.id.txt0);
        listaCasillas[0][1] = (TextView) findViewById(R.id.txt1);
        listaCasillas[0][2] = (TextView) findViewById(R.id.txt2);
        listaCasillas[0][3] = (TextView) findViewById(R.id.txt3);
        // Fila 2
        listaCasillas[1][0] = (TextView) findViewById(R.id.txt4);
        listaCasillas[1][1] = (TextView) findViewById(R.id.txt5);
        listaCasillas[1][2] = (TextView) findViewById(R.id.txt6);
        listaCasillas[1][3] = (TextView) findViewById(R.id.txt7);
        // Fila 3
        listaCasillas[2][0] = (TextView) findViewById(R.id.txt8);
        listaCasillas[2][1] = (TextView) findViewById(R.id.txt9);
        listaCasillas[2][2] = (TextView) findViewById(R.id.txt10);
        listaCasillas[2][3] = (TextView) findViewById(R.id.txt11);
        // Fila 4
        listaCasillas[3][0] = (TextView) findViewById(R.id.txt12);
        listaCasillas[3][1] = (TextView) findViewById(R.id.txt13);
        listaCasillas[3][2] = (TextView) findViewById(R.id.txt14);
        listaCasillas[3][3] = (TextView) findViewById(R.id.txt15);

        // Generar un numero en la cuadricula
        generarNumero();

        // Resetear el juego para volver a empezar
        newGame = (Button) findViewById(R.id.new_game);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Borrar todos los numeros
                for (int fila = 0; fila < listaCasillas.length; fila++) {
                    for (int columna = 0; columna < listaCasillas[fila].length; columna++) {
                        listaCasillas[fila][columna].setText("");
                    }
                }
                // Cambiar la puntuacion a 0
                valor_score = 0;
                score.setText("SCORE \n 0");

                // Generar un numero para volver a empezar a jugar
                generarNumero();

                dialogo_fin();
            }
        });

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            // Si la cordenada x es mayor a la y el deslizamiento ha sido horizontal
            if (Math.abs(diffX) > Math.abs(diffY)) {
                // El deslizamiento debe cumplir unas condiciones de distancia y tiempo
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    // Deslizar a la derecha
                    if (diffX > 0) {
                        Log.d(LOG_TAG, "--------Derecha--------");
                        // Comprobar si el numero se puede mover desde la primera fila
                        for (int iFila = 0; iFila < 4; iFila++) {
                            Log.d(LOG_TAG, "Fila: " + iFila);
                            // Comprobar si el numero se puede mover desde la ultima columna
                            for (int iColumna = 3; iColumna >= 0; iColumna--) {
                                Log.d(LOG_TAG, "Columna: " + iColumna);
                                // Comprobar si hay numero en la casilla derecha
                                if (!listaCasillas[iFila][iColumna].getText().toString().isEmpty()) {
                                    moverNumDerecha(iFila, iColumna);
                                }
                            }
                        }
                        Log.d(LOG_TAG, "----------------------");
                        // Generar un nuevo numero, si se ha movido un numero
                        if (mover) {
                            generarNumero();
                            mover = false;
                        }else{
                            comprobarFin();
                        }
                        // Deslizar a la izquierda
                    } else {
                        Log.d(LOG_TAG, "--------Izquierda--------");
                        // Comprobar si el numero se puede mover desde la primera fila
                        for (int iFila = 0; iFila < 4; iFila++) {
                            Log.d(LOG_TAG, "Fila: " + iFila);
                            // Comprobar si el numero se puede mover desde la primera columna
                            for (int iColumna = 0; iColumna < 4; iColumna++) {
                                Log.d(LOG_TAG, "Columna: " + iColumna);
                                // Comprobar si hay numero en la casilla derecha
                                if (!listaCasillas[iFila][iColumna].getText().toString().isEmpty()) {
                                    moverNumIzquierda(iFila, iColumna);
                                }
                            }
                        }
                        Log.d(LOG_TAG, "----------------------");
                        // Generar un nuevo numero, si se ha movido un numero
                        if (mover) {
                            generarNumero();
                            mover = false;
                        }else{
                            comprobarFin();
                        }
                    }
                }
                result = true;
            }// el deslizamiento vertical tambien debe cumplir unas condiciones de distancia y tiempo
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                // Deslizar abajo
                if (diffY > 0) {
                    Log.d(LOG_TAG, "--------Abajo--------");
                    // Comprobar si el numero se puede mover desde la ultima fila
                    for (int iFila = 3; iFila >= 0; iFila--) {
                        Log.d(LOG_TAG, "Fila: " + iFila);
                        // Comprobar si el numero se puede mover desde la primera columna
                        for (int iColumna = 0; iColumna < 4; iColumna++) {
                            Log.d(LOG_TAG, "Columna: " + iColumna);
                            // Comprobar si hay numero en la casilla derecha
                            if (!listaCasillas[iFila][iColumna].getText().toString().isEmpty()) {
                                moverNumAbajo(iFila, iColumna);
                            }

                        }
                    }
                    Log.d(LOG_TAG, "----------------------");
                    // Generar un nuevo numero, si se ha movido un numero
                    if (mover) {
                        generarNumero();
                        mover = false;
                    }else{
                        comprobarFin();
                    }

                    // Deslizar arriba
                } else {
                    Log.d(LOG_TAG, "--------Arriba--------");
                    // Comprobar si el numero se puede mover desde la primera fila
                    for (int iFila = 0; iFila < 4; iFila++) {
                        Log.d(LOG_TAG, "Fila: " + iFila);
                        // Comprobar si el numero se puede mover desde la primera columna
                        for (int iColumna = 0; iColumna < 4; iColumna++) {
                            Log.d(LOG_TAG, "Columna: " + iColumna);
                            // Comprobar si hay numero en la casilla derecha
                            if (!listaCasillas[iFila][iColumna].getText().toString().isEmpty()) {
                                moverNumArriba(iFila, iColumna);
                            }
                        }
                    }
                    Log.d(LOG_TAG, "----------------------");
                    // Generar un nuevo numero, si se ha movido un numero
                    if (mover) {
                        generarNumero();
                        mover = false;
                    }else{
                        comprobarFin();
                    }
                }
            }
//            comprobarFin();
            result = true;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void mostrar_toast(String texto) {
        Context context = getApplicationContext();
        CharSequence text = texto;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void moverNumDerecha(int nfila, int ncolumna) {
        int ncolumna_original = ncolumna;
        String valor_original = (String) listaCasillas[nfila][ncolumna_original].getText();

        while (ncolumna < 3) {
            ncolumna++;
            // Si estamos en la ultima columna y esta vacia pintar numero
            if (ncolumna == 3 && listaCasillas[nfila][ncolumna].getText().toString().isEmpty()) {
//                Animation animacion_titulo = AnimationUtils.loadAnimation(this,R.anim.animacion_prueba);//-----------Intento animacion-------------
//                listaCasillas[nfila][ncolumna_original].startAnimation(animacion_titulo);
//                Animation animation = new TranslateAnimation(0, 500,0, 0);
//                animation.setDuration(1000);
////                animation.setFillAfter(true);
//                listaCasillas[nfila][ncolumna_original].startAnimation(animation);
//                listaCasillas[nfila][ncolumna_original].setVisibility(0);
                //------------
//                TranslateAnimation anim = new TranslateAnimation( 0, 500 , 0, 0 );
//                anim.setDuration(1000);
//                anim.setZAdjustment(2);
//                Log.d(LOG_TAG, "--------------Z" + anim.getZAdjustment());
//                anim.setFillAfter( true );
//                listaCasillas[nfila][ncolumna_original].startAnimation(anim);
//                TextView nuevo = new TextView(this);
//                nuevo.setText("nuevo");
                //-----------
//                ObjectAnimator anim = ObjectAnimator.ofFloat(listaCasillas[nfila][ncolumna_original],"translationY",1300f);
//                anim.setDuration(1000);
//                anim.start();
// -------------
                listaCasillas[nfila][ncolumna_original].setText("");
                listaCasillas[nfila][ncolumna].setText(valor_original);

            }// Comprobar si hay numero en la casilla derecha
            else if (!listaCasillas[nfila][ncolumna].getText().toString().isEmpty()) {
                Log.d(LOG_TAG, "OCUPADA Fila: " + nfila + " Columna: " + ncolumna);
                String valor_ocupado = (String) listaCasillas[nfila][ncolumna].getText();
                if (Integer.parseInt(valor_original) == Integer.parseInt(valor_ocupado)) {
                    int suma = Integer.parseInt(valor_original) + Integer.parseInt(valor_ocupado);
                    Log.d(LOG_TAG, "SUMA: " + suma);
                    listaCasillas[nfila][ncolumna_original].setText("");
                    listaCasillas[nfila][ncolumna].setText(suma + "");
                    // Sumar numeros a la puntuacion total
                    sumar_score(suma);
                    break;
                } else {
                    // Colocar el numero en la posicion anterior a la ocupada
                    ncolumna--;
                    listaCasillas[nfila][ncolumna_original].setText("");
                    listaCasillas[nfila][ncolumna].setText(valor_original);
                    break;
                }


            }
        }

        // Comprobar si el numero se ha movido
        if (ncolumna != ncolumna_original) {
            mover = true;
        }
    }

    public void moverNumIzquierda(int nfila, int ncolumna) {
        int ncolumna_original = ncolumna;
        String valor_original = (String) listaCasillas[nfila][ncolumna_original].getText();

        while (ncolumna > 0) {
            ncolumna--;
            // Si estamos en la ultima columna y esta vacia pintar numero
            if (ncolumna == 0 && listaCasillas[nfila][ncolumna].getText().toString().isEmpty()) {
                listaCasillas[nfila][ncolumna_original].setText("");
                listaCasillas[nfila][ncolumna].setText(valor_original);
            }// Comprobar si hay numero en la casilla derecha
            else if (!listaCasillas[nfila][ncolumna].getText().toString().isEmpty()) {
                Log.d(LOG_TAG, "OCUPADA Fila: " + nfila + " Columna: " + ncolumna);
                String valor_ocupado = (String) listaCasillas[nfila][ncolumna].getText();
                if (Integer.parseInt(valor_original) == Integer.parseInt(valor_ocupado)) {
                    int suma = Integer.parseInt(valor_original) + Integer.parseInt(valor_ocupado);
                    Log.d(LOG_TAG, "SUMA: " + suma);
                    listaCasillas[nfila][ncolumna_original].setText("");
                    listaCasillas[nfila][ncolumna].setText(suma + "");
                    // Sumar numeros a la puntuacion total
                    sumar_score(suma);
                    break;
                } else {
                    // Colocar el numero en la posicion anterior a la ocupada
                    ncolumna++;
                    listaCasillas[nfila][ncolumna_original].setText("");
                    listaCasillas[nfila][ncolumna].setText(valor_original);
                    break;
                }
            }
        }
        // Comprobar si el numero se ha movido
        if (ncolumna != ncolumna_original) {
            mover = true;
        }
    }

    public void moverNumAbajo(int nfila, int ncolumna) {
        int nfila_original = nfila;
        String valor_original = (String) listaCasillas[nfila_original][ncolumna].getText();

        while (nfila < 3) {
            nfila++;
            // Si estamos en la ultima columna y esta vacia pintar numero
            if (nfila == 3 && listaCasillas[nfila][ncolumna].getText().toString().isEmpty()) {
                listaCasillas[nfila_original][ncolumna].setText("");
                listaCasillas[nfila][ncolumna].setText(valor_original);
            }// Comprobar si hay numero en la casilla derecha
            else if (!listaCasillas[nfila][ncolumna].getText().toString().isEmpty()) {
                Log.d(LOG_TAG, "OCUPADA Fila: " + nfila + " Columna: " + ncolumna);
                String valor_ocupado = (String) listaCasillas[nfila][ncolumna].getText();
                if (Integer.parseInt(valor_original) == Integer.parseInt(valor_ocupado)) {
                    int suma = Integer.parseInt(valor_original) + Integer.parseInt(valor_ocupado);
                    Log.d(LOG_TAG, "SUMA: " + suma);
                    listaCasillas[nfila_original][ncolumna].setText("");
                    listaCasillas[nfila][ncolumna].setText(suma + "");
                    // Sumar numeros a la puntuacion total
                    sumar_score(suma);
                    break;
                } else {
                    // Colocar el numero en la posicion anterior a la ocupada
                    nfila--;
                    listaCasillas[nfila_original][ncolumna].setText("");
                    listaCasillas[nfila][ncolumna].setText(valor_original);
                    break;
                }
            }
        }
        // Comprobar si el numero se ha movido
        if (nfila != nfila_original) {
            mover = true;
        }
    }

    public void moverNumArriba(int nfila, int ncolumna) {
        int nfila_original = nfila;
        String valor_original = (String) listaCasillas[nfila_original][ncolumna].getText();

        while (nfila > 0) {
            nfila--;
            // Si estamos en la ultima columna y esta vacia pintar numero
            if (nfila == 0 && listaCasillas[nfila][ncolumna].getText().toString().isEmpty()) {
                listaCasillas[nfila_original][ncolumna].setText("");
                listaCasillas[nfila][ncolumna].setText(valor_original);
            }// Comprobar si hay numero en la casilla derecha
            else if (!listaCasillas[nfila][ncolumna].getText().toString().isEmpty()) {
                Log.d(LOG_TAG, "OCUPADA Fila: " + nfila + " Columna: " + ncolumna);
                String valor_ocupado = (String) listaCasillas[nfila][ncolumna].getText();
                if (Integer.parseInt(valor_original) == Integer.parseInt(valor_ocupado)) {
                    int suma = Integer.parseInt(valor_original) + Integer.parseInt(valor_ocupado);
                    Log.d(LOG_TAG, "SUMA: " + suma);
                    listaCasillas[nfila_original][ncolumna].setText("");
                    listaCasillas[nfila][ncolumna].setText(suma + "");
                    // Sumar numeros a la puntuacion total
                    sumar_score(suma);
                    break;
                } else {
                    // Colocar el numero en la posicion anterior a la ocupada
                    nfila++;
                    listaCasillas[nfila_original][ncolumna].setText("");
                    listaCasillas[nfila][ncolumna].setText(valor_original);
                    break;
                }
            }
        }
        // Comprobar si el numero se ha movido
        if (nfila != nfila_original) {
            mover = true;
        }
    }

    public void generarNumero() {
        boolean numero_puesto = false;
        while (!numero_puesto) {
            nfila_aleatorio = (int) (Math.random() * (3 + 1 - 0)) + 0;
            ncolumna_aleatorio = (int) (Math.random() * (3 + 1 - 0)) + 0;
            if (!listaCasillas[nfila_aleatorio][ncolumna_aleatorio].getText().toString().isEmpty()) {
                Log.d(LOG_TAG, "OCUPADA Fila: " + nfila_aleatorio + " Columna: " + ncolumna_aleatorio);
            } else {
                listaCasillas[nfila_aleatorio][ncolumna_aleatorio].setText("2");
                numero_puesto = true;
                Log.d(LOG_TAG, " Creado Fila: " + nfila_aleatorio + " Columna: " + ncolumna_aleatorio);
            }
        }

    }

    public void comprobarFin(){
        boolean fin_col = true;
        boolean fin_fil = true;
        boolean fin_v = true;
        for (int fila = 0; fila < listaCasillas.length; fila++) {
            for (int columna = 0; columna < listaCasillas[fila].length; columna++) {

                // Comprobar que el numero de al lado sea igual
                if(columna<=2){
                    int sig_columna = columna + 1;
                    if (listaCasillas[fila][columna].getText().toString().equals(listaCasillas[fila][sig_columna].getText().toString())){
                        fin_col = false;
                        break;
                    }
                }

                // Comprobar que el numero de debajo no sea igual
                if (fila<=2){
                    int fila_abajo = fila + 1;
                    if (listaCasillas[fila][columna].getText().toString().equals(listaCasillas[fila_abajo][columna].getText().toString())){
                        fin_fil = false;
                        break;
                    }
                }

                // Comprobar que no haya casillas vacias
                if(listaCasillas[fila][columna].getText().toString().isEmpty()){
                    fin_v = false;
                    break;
                }

            }
        }
        if (fin_col == true && fin_fil == true && fin_v == true){
            db.addScore(user,"2048",valor_score);
            dialogo_fin();

        }
    }

    // Suma la puntuacion cuando dos numeros se suman
    public void sumar_score(int suma_numeros) {

        // Sumar numeros a la puntuacion
        valor_score = valor_score + suma_numeros;

        // Mostrar por pantalla la puntuacion
        score.setText("SCORE \n " + valor_score);
    }

    public void dialogo_fin(){
        ArrayList<Score> score_list = db.getScores();
        String ranking = "";

        for (int i = 0; i < score_list.size(); i++) {
            if (i==10){
                break;
            }
            ranking += (i+1) + "- " + score_list.get(i).getPlayer() + " " + score_list.get(i).getScore() +"\n";

        }

        best_score.setText("BEST \n "+ db.getBestScore(user));
        Dialogo dialogo = new Dialogo(valor_score,ranking);
        dialogo.show(getSupportFragmentManager(),"Dialogo");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }


}