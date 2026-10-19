package Fiche;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author LukashouSiarhei
 * @author Santiago Gonzalez
 */
public class LeerFiche {
    private static final String NOMBRE_FICHERO = "Ficheros" + File.separator + "FICHE.DAT";

    public static void main(String[] args) {
        DataInputStream dis = null;
        long contador = 0;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(NOMBRE_FICHERO)));
            System.out.println("NOMBRE Y APELLIDOS | SEXO | SALARIO | INGRESO | TIPO | PROVINCIA");
            FicheRegistro reg = FicheDAO.leer(dis);
            while (!FicheDAO.ff()) {
                System.out.println(reg.getNombreApe() + " | " + reg.getSexo() + " | " + reg.getSalario() + " | "
                        + reg.getDiaIngreso() + "/" + reg.getMesIngreso() + "/" + reg.getAnoIngreso() + " | "
                        + reg.getTipoEmpleado().getDescripcion() + " | " + reg.getProvincia().getNombre());
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
