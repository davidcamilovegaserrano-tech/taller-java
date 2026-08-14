package exceptions;

/**
 * Excepción CHECKED (extiende Exception, no RuntimeException).
 *
 * Motivo de diseño: "vuelo lleno" no es un error de programación, es una
 * situación de negocio esperable y recuperable. Al ser checked, el compilador
 * obliga a quien llame a reservarVuelo() a decidir qué hacer con ella.
 */
public class VueloLlenoException extends Exception {

    public VueloLlenoException(String mensaje) {
        super(mensaje);
    }
}
