package Fiche;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author LukashouSiarhei
 * @author Santiago Gonzalez
 */
public class ProgramaC {

    private static final String NOMBRE_FICHERO = "Ficheros" + File.separator + "FICHE.DAT";
    private static final double PRECIO_TRIENIO = 24;
    private static final double PORCENTAJE_DESTINO = 0.10;
    private static final double COMPLEMENTO_POR_LA_CARA = 120;

    public static void main(String[] args) {
        DataInputStream dis = null;
        long contador = 0;
        GregorianCalendar hoy = new GregorianCalendar();

        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(NOMBRE_FICHERO)));
            FicheRegistro reg = FicheDAO.leer(dis);

            if (!FicheDAO.ff()) {
                System.out.println("NOMBRE Y APELLIDOS | SUELDO");
            }
            
            while (!FicheDAO.ff()) {
                System.out.println(reg.getNombreApe() + " | " + String.format("%.2f", calcularSueldo(reg, hoy)));
                contador++;
                reg = FicheDAO.leer(dis);
            }

            if (contador == 0) {
                System.out.println("El fichero está vacío.");
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

    private static double calcularSueldo(FicheRegistro reg, GregorianCalendar hoy) {
        double complementos = calcularTrienios(reg, hoy) * PRECIO_TRIENIO;

        Provincia prov = reg.getProvincia();
        if (prov == Provincia.JAEN || prov == Provincia.HUELVA || prov == Provincia.ALMERIA) {
            complementos += reg.getSalario() * PORCENTAJE_DESTINO;
        }

        if (reg.getSexo() == 'H') {
            complementos += COMPLEMENTO_POR_LA_CARA;
        }

        return reg.getSalario() + complementos;
    }

    private static int calcularTrienios(FicheRegistro reg, GregorianCalendar hoy) {
        GregorianCalendar ingreso = new GregorianCalendar(reg.getAnoIngreso(), reg.getMesIngreso() - 1, reg.getDiaIngreso());
        
        int anos = hoy.get(Calendar.YEAR) - ingreso.get(Calendar.YEAR);
        
        ingreso.add(Calendar.YEAR, anos);
        
        if (ingreso.after(hoy)) {
            anos--;
        }
        
        int trienios = 0;
        
        if (anos > 0) {
            trienios = anos / 3;
        }
        
        return trienios;
    }
}
