package repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * Contenedor genérico. El parámetro <T> permite reutilizar la MISMA clase para
 * Aeropuerto, Pasajero, Vuelo y Piloto sin duplicar código y sin perder el
 * tipo (a diferencia de usar List<Object> y andar casteando).
 *
 * @param <T> tipo de entidad administrada
 */
public class Repositorio<T> {

    private List<T> elementos = new ArrayList<>();

    public void agregar(T elemento) {
        elementos.add(elemento);
    }

    public List<T> obtenerTodos() {
        return elementos;
    }

    public int contar() {
        return elementos.size();
    }
}
