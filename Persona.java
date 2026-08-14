package models;

/**
 * Clase base abstracta. No tiene sentido instanciar "una persona" en este
 * dominio: siempre es un Pasajero o un Piloto. Por eso es abstract.
 *
 * Los atributos son private (no protected): las subclases acceden por getters.
 * Esto mantiene el encapsulamiento incluso dentro de la jerarquía.
 */
public abstract class Persona {

    private String documento;
    private String nombre;

    protected Persona(String documento, String nombre) {
        this.documento = documento;
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Contrato polimórfico: cada subclase decide cómo se describe a sí misma.
     */
    public abstract String obtenerDescripcion();
}
