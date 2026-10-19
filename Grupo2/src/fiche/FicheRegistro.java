
package fiche;

/**
 *
 * @author Santiago González
 */
public class FicheRegistro {
    private String nombreApe;
    private char sexo;
    private double salario;
    private short anoIngreso;
    private byte mesIngreso;
    private byte diaIngreso;
    private TipoEmpleado tipoEmpleado;
    private Provincia provincia;

    public FicheRegistro() {
    }

    public FicheRegistro(String nombreApe, char sexo, double salario, short anoIngreso, 
            byte mesIngreso, byte diaIngreso, TipoEmpleado tipoEmpleado, Provincia provincia) {
        this.nombreApe = nombreApe;
        this.sexo = sexo;
        this.salario = salario;
        this.anoIngreso = anoIngreso;
        this.mesIngreso = mesIngreso;
        this.diaIngreso = diaIngreso;
        this.tipoEmpleado = tipoEmpleado;
        this.provincia = provincia;
    }

    

    public String getNombreApe() {
        return nombreApe;
    }

    public char getSexo() {
        return sexo;
    }

    public double getSalario() {
        return salario;
    }

    public short getAnoIngreso() {
        return anoIngreso;
    }

    public byte getMesIngreso() {
        return mesIngreso;
    }

    public byte getDiaIngreso() {
        return diaIngreso;
    }

    public TipoEmpleado getTipoEmpleado() {
        return tipoEmpleado;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setNombreApe(String nombreApe) {
        this.nombreApe = nombreApe;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public void setAnoIngreso(short anoIngreso) {
        this.anoIngreso = anoIngreso;
    }

    public void setMesIngreso(byte mesIngreso) {
        this.mesIngreso = mesIngreso;
    }

    public void setDiaIngreso(byte diaIngreso) {
        this.diaIngreso = diaIngreso;
    }

    public void setTipoEmpleado(TipoEmpleado tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    @Override
    public String toString() {
        return nombreApe + " | " + sexo + " | " + salario + " € | "
                + diaIngreso + "/" + mesIngreso + "/" + anoIngreso + " | "
                + tipoEmpleado.getDescripcion() + " | " + provincia.getNombre();
    }
}
