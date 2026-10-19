package Fiche;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author LukashouSiarhei
 * @author Santiago Gonzalez
 */
public class ProgramaA {

    private static final String NOMBRE_FICHERO = "Ficheros" + File.separator + "FICHE.DAT";
    private static final int MAX_NOMBRE = 50;
    private static final int ANO_MINIMO = 1900;
    private static final String REGEX_NOMBRE = "\\p{L}+([ '\\-]\\p{L}+)*";
    private static final String REGEX_SALARIO = "\\d{1,6}([.,]\\d{1,2})?";
    private static final String REGEX_ENTERO = "\\d{1,9}";

    private static boolean finEntrada = false;

    public static void main(String[] args) {
        BufferedReader teclado = null;
        DataOutputStream dos = null;
        long contador = 0;
        try {
            teclado = new BufferedReader(new InputStreamReader(System.in));
            boolean anadir = false;
            if (new File(NOMBRE_FICHERO).exists()) {
                anadir = pedirAnadir(teclado);
                System.out.println();
            }
            if (!finEntrada) {
                dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(NOMBRE_FICHERO, anadir)));
                boolean otro;
                do {
                    FicheRegistro reg = pedirRegistro(teclado);
                    if (!finEntrada) {
                        FicheDAO.escribir(dos, reg);
                        contador++;
                    }
                    otro = !finEntrada && pedirSiNo(teclado, "¿Otro registro? (S/N): ");
                    System.out.println();
                } while (otro);
            }
            if (finEntrada) {
                System.out.println("Entrada de teclado cerrada.");
            }
            if (anadir) {
                System.out.println("Se han añadido " + contador + " registro(s) al fichero " + NOMBRE_FICHERO + ".");
            } else if (dos != null) {
                System.out.println("Fichero " + NOMBRE_FICHERO + " creado con " + contador + " registro(s).");
            }
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el fichero: " + e.getMessage());
                }
            }
            if (teclado != null) {
                try {
                    teclado.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el teclado: " + e.getMessage());
                }
            }
        }
    }

    private static String leerLinea(BufferedReader teclado) throws IOException {
        String linea = teclado.readLine();
        if (linea == null) {
            finEntrada = true;
            linea = "";
        }
        return linea.trim();
    }

    private static FicheRegistro pedirRegistro(BufferedReader teclado) throws IOException {
        FicheRegistro reg = new FicheRegistro();
        reg.setNombreApe(pedirNombre(teclado));
        reg.setSexo(pedirSexo(teclado));
        reg.setSalario(pedirSalario(teclado));
        pedirFechaIngreso(teclado, reg);
        reg.setTipoEmpleado(pedirTipoEmpleado(teclado));
        reg.setProvincia(pedirProvincia(teclado));
        return reg;
    }

    private static String pedirNombre(BufferedReader teclado) throws IOException {
        String nombre;
        boolean valido;
        do {
            System.out.print("Nombre y apellidos: ");
            StringBuffer sb = new StringBuffer(leerLinea(teclado).replaceAll("\\s+", " "));
            nombre = sb.toString();
            valido = sb.length() <= MAX_NOMBRE;
            if (valido) {
                valido = nombre.matches(REGEX_NOMBRE);
            } else {
                System.out.println("Nombre demasiado largo (máximo " + MAX_NOMBRE + " caracteres).");
            }
        } while (!valido && !finEntrada);
        return nombre;
    }

    private static char pedirSexo(BufferedReader teclado) throws IOException {
        char sexo = ' ';
        boolean valido;
        do {
            System.out.print("Sexo (H/M): ");
            String linea = leerLinea(teclado).toUpperCase();
            valido = linea.equals("H") || linea.equals("M");
            if (valido) {
                sexo = linea.charAt(0);
            }
        } while (!valido && !finEntrada);
        return sexo;
    }

    private static double pedirSalario(BufferedReader teclado) throws IOException {
        double salario = 0;
        boolean valido;
        do {
            System.out.print("Salario: ");
            String linea = leerLinea(teclado);
            valido = linea.matches(REGEX_SALARIO);
            if (valido) {
                salario = Double.parseDouble(linea.replace(',', '.'));
                valido = salario > 0;
            }
        } while (!valido && !finEntrada);
        return salario;
    }

    private static void pedirFechaIngreso(BufferedReader teclado, FicheRegistro reg) throws IOException {
        GregorianCalendar hoy = new GregorianCalendar();
        GregorianCalendar fecha;
        do {
            int ano = pedirEntero(teclado, "Año: ", ANO_MINIMO, hoy.get(Calendar.YEAR));
            int mes = pedirEntero(teclado, "Mes: ", 1, 12);
            fecha = new GregorianCalendar(ano, mes - 1, 1);
            int dia = pedirEntero(teclado, "Día: ", 1, fecha.getActualMaximum(Calendar.DAY_OF_MONTH));
            fecha.set(Calendar.DAY_OF_MONTH, dia);
        } while (fecha.after(hoy) && !finEntrada);
        reg.setAnoIngreso((short) fecha.get(Calendar.YEAR));
        reg.setMesIngreso((byte) (fecha.get(Calendar.MONTH) + 1));
        reg.setDiaIngreso((byte) fecha.get(Calendar.DAY_OF_MONTH));
    }

    private static int pedirEntero(BufferedReader teclado, String mensaje, int min, int max) throws IOException {
        int valor = 0;
        boolean valido;
        do {
            System.out.print(mensaje);
            String linea = leerLinea(teclado);
            valido = linea.matches(REGEX_ENTERO);
            if (valido) {
                valor = Integer.parseInt(linea);
                valido = valor >= min && valor <= max;
            }
        } while (!valido && !finEntrada);
        return valor;
    }

    private static TipoEmpleado pedirTipoEmpleado(BufferedReader teclado) throws IOException {
        StringBuilder menu = new StringBuilder("Tipo de empleado (");
        TipoEmpleado[] valores = TipoEmpleado.values();
        for (int i = 0; i < valores.length; i++) {
            menu.append(valores[i].getCodigo()).append('=').append(valores[i].getDescripcion());
            if (i < valores.length - 1) {
                menu.append(", ");
            }
        }
        menu.append("): ");
        TipoEmpleado tipo = null;
        do {
            System.out.print(menu);
            String linea = leerLinea(teclado).toUpperCase();
            try {
                if (linea.length() == 1) {
                    tipo = TipoEmpleado.fromCodigo(linea.charAt(0));
                }
            } catch (IllegalArgumentException e) {
                tipo = null;
            }
        } while (tipo == null && !finEntrada);
        return tipo;
    }

    private static Provincia pedirProvincia(BufferedReader teclado) throws IOException {
        System.out.println();
        for (Provincia p : Provincia.values()) {
            System.out.println(p.getCodigo() + " - " + p.getNombre());
        }
        System.out.println();
        Provincia prov = null;
        do {
            System.out.print("Código de provincia: ");
            String linea = leerLinea(teclado);
            try {
                if (linea.matches("\\d{1,3}")) {
                    prov = Provincia.fromCodigo(Byte.parseByte(linea));
                }
            } catch (IllegalArgumentException e) {
                prov = null;
            }
        } while (prov == null && !finEntrada);
        return prov;
    }

    private static boolean pedirAnadir(BufferedReader teclado) throws IOException {
        String opcion;
        boolean confirmado;
        do {
            do {
                System.out.print("El fichero " + NOMBRE_FICHERO + " ya existe. ¿Borrarlo y crear uno nuevo (B) o añadir registros al existente (A)? (B/A): ");
                opcion = leerLinea(teclado).toUpperCase();
            } while (!opcion.equals("B") && !opcion.equals("A") && !finEntrada);
            confirmado = opcion.equals("A")
                    || finEntrada
                    || pedirSiNo(teclado, "¿Está seguro de que desea borrar el fichero existente y crear uno nuevo? (S/N): ");
        } while (!confirmado && !finEntrada);
        return opcion.equals("A");
    }

    private static boolean pedirSiNo(BufferedReader teclado, String mensaje) throws IOException {
        String respuesta;
        do {
            System.out.print(mensaje);
            respuesta = leerLinea(teclado).toUpperCase();
        } while (!respuesta.equals("S") && !respuesta.equals("N") && !finEntrada);
        return respuesta.equals("S");
    }
}
