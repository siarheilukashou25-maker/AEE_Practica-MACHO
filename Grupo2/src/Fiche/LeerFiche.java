package Fiche;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Lee y muestra por pantalla el fichero FICHE.DAT.
 * Se lee registro a registro: en memoria solo hay un registro a la vez.
 */
public class LeerFiche {

    private static final String NOMBRE_FICHERO = "FICHE.DAT";

    public static void main(String[] args) {
        DataInputStream dis = null;
        long contador = 0;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(NOMBRE_FICHERO)));
            System.out.printf("%-30s %s %10s %-10s %-12s %-8s%n",
                    "NOMBRE Y APELLIDOS", "S", "SALARIO", "INGRESO", "TIPO", "PROVINCIA");
            FicheRegistro reg = FicheDAO.leer(dis);
            while (!FicheDAO.ff()) {
                System.out.printf("%-30s %c %10.2f %02d/%02d/%04d %-12s %-8s%n",
                        reg.getNombreApe(), reg.getSexo(), reg.getSalario(),
                        reg.getDiaIngreso(), reg.getMesIngreso(), reg.getAnoIngreso(),
                        reg.getTipoEmpleado().getDescripcion(), reg.getProvincia().getNombre());
                contador++;
                reg = FicheDAO.leer(dis);
            }
            if (contador == 0) {
                System.out.println("El fichero está vacío.");
            } else {
                System.out.println("Total: " + contador + " registro(s).");
            }
        } catch (FileNotFoundException e) {
            System.err.println("No existe el fichero " + NOMBRE_FICHERO + ".");
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Fichero corrupto tras " + contador + " registro(s): " + e.getMessage());
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el fichero: " + e.getMessage());
                }
            }
        }
    }
}
