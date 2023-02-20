package com.example.a2048;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialogo extends AppCompatDialogFragment {
    String ranking;
    int puntuacion;
    public Dialogo(int puntuacion, String ranking) {
        this.puntuacion = puntuacion;
        this.ranking = ranking;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(ranking)
                .setTitle("GAME OVER");
//        builder.setMessage("FIN") // Titulo del dialog
//                .setPositiveButton("Nueva partida", new DialogInterface.OnClickListener() { // Crear boton positivo
//                    public void onClick(DialogInterface dialog, int id) {
//                        // Mandar el evento del boton positivo a la actividad que lo implementara
////                        TimePickerFragment tp = new TimePickerFragment();
////                        tp.show(getParentFragmentManager(),"DIALOG_TIME");
//                    }
//                })
//                .setNegativeButton("Cerrar", new DialogInterface.OnClickListener() { // Crear boton negativo
//                    public void onClick(DialogInterface dialog, int id) {
//                        // Mandar el evento del boton negativo a la actividad que lo implementara
////                        DatePickerFragment dp = new DatePickerFragment();
////                        dp.show(getParentFragmentManager(),"DIALOG_DATE");
//                    }
//                });
        return builder.create();
    }

}
