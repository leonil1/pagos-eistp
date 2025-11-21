package com.iestpdj.iestpdjpagos.model;

public class NumeroALetras {
    private static final String[] UNIDADES = {
            "", "UNO", "DOS", "TRES", "CUATRO", "CINCO",
            "SEIS", "SIETE", "OCHO", "NUEVE", "DIEZ",
            "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE",
            "DIECISÉIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE", "VEINTE"
    };

    private static final String[] DECENAS = {
            "VEINTI", "TREINTA", "CUARENTA", "CINCUENTA",
            "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"
    };

    private static final String[] CENTENAS = {
            "", "CIENTO", "DOSCIENTOS", "TRESCIENTOS",
            "CUATROCIENTOS", "QUINIENTOS", "SEISCIENTOS",
            "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"
    };

    public static String convertir(long numero) {
        if (numero == 0) return "CERO";
        if (numero == 100) return "CIEN";

        return convertirNumero(numero).trim();
    }

    private static String convertirNumero(long numero) {
        if (numero <= 20) return UNIDADES[(int) numero];

        if (numero < 100) {
            int decena = (int) numero / 10;
            int unidad = (int) numero % 10;
            if (numero <= 29) return DECENAS[0] + UNIDADES[unidad].toLowerCase();
            return DECENAS[decena - 2] + (unidad > 0 ? " Y " + UNIDADES[unidad] : "");
        }

        if (numero < 1000) {
            int centena = (int) numero / 100;
            long resto = numero % 100;
            return CENTENAS[centena] + (resto > 0 ? " " + convertirNumero(resto) : "");
        }

        if (numero < 1_000_000) {
            long miles = numero / 1000;
            long resto = numero % 1000;
            return (miles == 1 ? "MIL" : convertirNumero(miles) + " MIL")
                    + (resto > 0 ? " " + convertirNumero(resto) : "");
        }

        return "";
    }
}
