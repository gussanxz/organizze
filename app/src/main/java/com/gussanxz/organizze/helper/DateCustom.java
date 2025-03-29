package com.gussanxz.organizze.helper;

import static android.content.ContentValues.TAG;
import android.util.Log;
import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual() {

        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;

    }

    public static String mesAnoDataEscolhida( String data ) {

        String retornoData[] = data.split("/"); //ex: 23/01/2018
        String dia = retornoData[0]; //dia 23
        String mes = retornoData[1]; //mes 01
        String ano = retornoData[2]; //ano 2018
        Log.i(TAG, "Data informada: " + mes + "/" + ano );
        String mesAno = mes + ano;
        return  mesAno;

    }
}
