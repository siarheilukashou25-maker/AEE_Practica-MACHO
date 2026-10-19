
package fiche;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Santiago González
 */
public class Teclado {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private Teclado() {
    }

    private static String leerLinea(String mensaje) throws IOException {
        System.out.print(mensaje);
        String linea = br.readLine();
        if (linea == null) {
            throw new IOException("Fin de la entrada de teclado");
        }
        return linea.trim();
    }

    public static String leerCadena(String mensaje, int max) throws IOException {
        while (true) {
            String s = leerLinea(mensaje);
            if (s.isEmpty()) {
                System.out.println("  No puede estar vacío.");
            } else if (s.length() > max) {
                System.out.println("  Máximo " + max + " caracteres.");
            } else {
                return s;
            }
        }
    }

    public static int leerEntero(String mensaje, int min, int max) throws IOException {
        while (true) {
            try {
                int n = Integer.parseInt(leerLinea(mensaje));
                if (n >= min && n <= max) {
                    return n;
                }
                System.out.println("  Debe estar entre " + min + " y " + max + ".");
            } catch (NumberFormatException nfe) {
                System.out.println("  Introduce un número entero.");
            }
        }
    }

    public static double leerDouble(String mensaje, double min, double max)
            throws IOException {
        while (true) {
            try {
                double n = Double.parseDouble(leerLinea(mensaje).replace(',', '.'));
                if (n >= min && n <= max) {
                    return n;
                }
                System.out.println("  Debe estar entre " + min + " y " + max + ".");
            } catch (NumberFormatException nfe) {
                System.out.println("  Introduce un número (ej. 1234.56).");
            }
        }
    }

    public static char leerChar(String mensaje, String validos) throws IOException {
        while (true) {
            String s = leerLinea(mensaje).toUpperCase();
            if (s.length() == 1 && validos.indexOf(s.charAt(0)) >= 0) {
                return s.charAt(0);
            }
            System.out.println("  Valores válidos: " + validos);
        }
    }

    public static boolean leerSiNo(String mensaje) throws IOException {
        return leerChar(mensaje + " (S/N): ", "SN") == 'S';
    }
}
