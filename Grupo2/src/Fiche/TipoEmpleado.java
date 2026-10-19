package Fiche;

public enum TipoEmpleado {
    C('C', "A comisión"),
    F('F', "Fijos"),
    D('D', "A domicilio");

    private final char codigo;
    private final String descripcion;

    private TipoEmpleado(char codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public char getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoEmpleado fromCodigo(char codigo) {
        TipoEmpleado resultado = null;
        TipoEmpleado[] valores = values();
        int i = 0;
        while (resultado == null && i < valores.length) {
            if (valores[i].codigo == codigo) {
                resultado = valores[i];
            }
            i++;
        }
        if (resultado == null) {
            throw new IllegalArgumentException("Código de tipo de empleado no válido: " + codigo);
        }
        return resultado;
    }
}
