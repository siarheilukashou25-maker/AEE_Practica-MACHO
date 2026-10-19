package Fiche;

/**
 *
 * @author LukashouSiarhei
 * @author Santiago Gonzalez
 */
public enum Provincia {
    ALMERIA(1, "Almería"),
    CADIZ(2, "Cádiz"),
    CORDOBA(3, "Córdoba"),
    GRANADA(4, "Granada"),
    HUELVA(5, "Huelva"),
    JAEN(6, "Jaén"),
    MALAGA(7, "Málaga"),
    SEVILLA(8, "Sevilla");

    private final byte codigo;
    private final String nombre;

    private Provincia(int codigo, String nombre) {
        this.codigo = (byte) codigo;
        this.nombre = nombre;
    }

    public byte getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public static Provincia fromCodigo(byte codigo) {
        Provincia resultado = null;
        Provincia[] valores = values();
        int i = 0;
        while (resultado == null && i < valores.length) {
            if (valores[i].codigo == codigo) {
                resultado = valores[i];
            }
            i++;
        }
        if (resultado == null) {
            throw new IllegalArgumentException("Código de provincia no válido: " + codigo);
        }
        return resultado;
    }
}
