package com.mijael.appsupport.utils;

public class CalendarUtil {

    public static final String AM = "am";
    public static final String PM = "pm";

    public static final String nombreDia[] = {"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

    public static final String nombreMes011[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    public static final String nombreMes112[] = {"","Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    public static int convert24hToPm(int hora){

        if (hora>12)
            return hora-12;
        else if (hora==0)
            return 12;
        else
            return hora;

    }


    public static String getAmOrPm(int hora){

        if (hora>12)
            return CalendarUtil.PM;
        else if (hora==0)
            return "";
        else
            return CalendarUtil.AM;

    }

}
