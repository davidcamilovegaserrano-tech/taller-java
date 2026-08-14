package models;

/**
 * Estados posibles del ciclo de vida de un vuelo.
 * El enum garantiza que el estado solo pueda tomar valores válidos
 * (no se usan String ni int "mágicos").
 */
public enum EstadoVuelo {
    PROGRAMADO,
    ABORDANDO,
    EN_VUELO,
    FINALIZADO,
    CANCELADO;

    /**
     * Traduce la opción numérica del menú (1..5) al enum correspondiente.
     * Mantiene la conversión dentro del propio tipo y evita un switch
     * repetido en la capa de presentación.
     */
    public static EstadoVuelo porOpcion(int opcion) {
        EstadoVuelo[] valores = values();
        if (opcion < 1 || opcion > valores.length) {
            throw new IllegalArgumentException("Opción de estado inválida.");
        }
        return valores[opcion - 1];
    }
}
