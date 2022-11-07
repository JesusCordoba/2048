package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private GestureDetector gestureDetector;

    // Tiempo que se mantiene el gesto
    private static final int SWIPE_THRESHOLD = 100;

    // Velocidad del gesto
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gestureDetector = new GestureDetector(this,this);
        gestureDetector.setOnDoubleTapListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        //mostrar_toast("onSingleTapConfirmed");
        Log.d(LOG_TAG, "onSingleTapConfirmed");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        //mostrar_toast("onDoubleTap");
        Log.d(LOG_TAG, "onDoubleTap");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        //mostrar_toast("onDoubleTapEvent");
        Log.d(LOG_TAG, "onDoubleTapEvent");
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        //mostrar_toast("onDown");
        Log.d(LOG_TAG, "onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        //mostrar_toast("onShowPress");
        Log.d(LOG_TAG, "onShowPress");

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        //mostrar_toast("onSingleTapUp");
        Log.d(LOG_TAG, "onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        //mostrar_toast("onScroll");
        Log.d(LOG_TAG, "onScroll");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        //mostrar_toast("onLongPress");
        Log.d(LOG_TAG, "onLongPress");

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        //TextView texto = (TextView) findViewById(R.id.textView);
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            // Si la cordenada x es mayor a la y el deslizamiento ha sido horizontal
            if (Math.abs(diffX) > Math.abs(diffY)) {
                // El deslizamiento debe cumplir unas condiciones de distancia y tiempo
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        mostrar_toast("Hacia la derecha");

                    } else {
                        mostrar_toast("Hacia la izquierda");
                    }
                }
                result = true;
            }// el deslizamiento vertical tambien debe cumplir unas condiciones de distancia y tiempo
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    mostrar_toast("Hacia abajo");
                } else {
                    mostrar_toast("Hacia arriba");
                }
            }
            result = true;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void mostrar_toast(String texto){
        Context context = getApplicationContext();
        CharSequence text = texto;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


}