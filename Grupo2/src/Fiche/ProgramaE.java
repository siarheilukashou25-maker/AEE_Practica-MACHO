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
public class ProgramaE {
    private static final String NOMBRE_FICHERO = "Ficheros" + File.separator + "FICHE.DAT";
    
    public static void main(String[] args) {

        int[] numeroEmplePorProvincias = new int[8];
        
        DataInputStream dis = null;
        long contador = 0;
        
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(NOMBRE_FICHERO)));
            FicheRegistro reg = FicheDAO.leer(dis);
            
            while (!FicheDAO.ff()) {
                numeroEmplePorProvincias[reg.getProvincia().getCodigo() - 1]++;
                contador++;
                reg = FicheDAO.leer(dis);
            }
            
            if (contador == 0) {
                System.out.println("El fichero está vacío.");
            } else {
                
                int maximo = 0;
                for (int num : numeroEmplePorProvincias) {
                    if (num > maximo) {
                        maximo = num;
                    }
                }
                System.out.println("Provincia(s) con más empleados (" + maximo + "):");
                for (int i = 0; i < numeroEmplePorProvincias.length; i++) {
                    if (numeroEmplePorProvincias[i] == maximo) {
                        System.out.println(Provincia.fromCodigo((byte) (i + 1)).getNombre());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No existe el fichero " + ".");
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
