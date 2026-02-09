package Util;

import java.util.Random;

public class CodigoBarrasGenerator {

    public static String generarEAN13() {
        Random random = new Random();

        // Genera los primeros 12 digitos
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            codigo.append(random.nextInt(10));
        }

        // Calcula el dígito verificador
        int digitoVerificador = calcularDigitoVerificador(codigo.toString());

        return codigo.toString() + digitoVerificador;
    }

    private static int calcularDigitoVerificador(String code12) {
        int sumaPar = 0;
        int sumaImpar = 0;

        for (int i = 0; i < code12.length(); i++) {
            int digito = Character.getNumericValue(code12.charAt(i));

            if ((i + 1) % 2 == 0) {
                sumaPar += digito;  // posiciones pares
            } else {
                sumaImpar += digito; // posiciones impares
            }
        }

        int total = sumaImpar + (sumaPar * 3);
        int verificador = 10 - (total % 10);

        return verificador == 10 ? 0 : verificador;
    }
}

