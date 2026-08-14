package models;

/**
 * Entidad de referencia. No tiene setters: un aeropuerto no cambia de ciudad
 * ni de código una vez registrado (objeto prácticamente inmutable).
 */
public class Aeropuerto {

    private String codigo;
    private String nombre;
    private String ciudad;
    private String pais;

    public Aeropuerto(String codigo, String nombre, String ciudad, String pais) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getPais() {
        return pais;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " - " + ciudad + " (" + pais + ")";
    }
}
