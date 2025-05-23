package com.gussanxz.orgafacil.model;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.util.Calendar;

public class TimePickerHelper {
    public static void showTimePickerDialog (Context context, EditText campoHora) {
        campoHora.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hora = calendar.get(Calendar.HOUR_OF_DAY);
            int minuto =  calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    (view, hourOfDay, minute) -> {
                        String horaFormatada = String.format("%02d:%02d", hourOfDay, minute);
                        campoHora.setText(horaFormatada);
                    },
                    hora,
                    minuto,
                    true
            );
            timePickerDialog.show();
        });
    }
}
