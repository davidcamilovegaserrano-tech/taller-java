package services;

import exceptions.VueloLlenoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import models.Aeropuerto;
import models.EstadoVuelo;
import models.Pasajero;
import models.Persona;
import models.Piloto;
import models.Vuelo;
import repositories.Repositorio;

/**
 * Capa de SERVICIO: concentra las reglas de negocio y las validaciones.
 *
 * Regla de arquitectura aplicada aquí:
 *   - Main            -> solo entrada/salida por consola.
 *   - SistemaVuelos   -> reglas y coordinación entre entidades.
 *   - models          -> datos e invariantes propias de cada entidad.
 *   - Repositorio<T>  -> almacenamiento en memoria.
 *
 * Esta clase NUNCA imprime. Si imprimiera, no se podría reutilizar en una
 * interfaz web o en pruebas automatizadas.
 */
public class SistemaVuelos {

    private Repositorio<Aeropuerto> aeropuertos = new Repositorio<>();
    private Repositorio<Pasajero> pasajeros = new Repositorio<>();
    private Repositorio<Vuelo> vuelos = new Repositorio<>();
    private Repositorio<Piloto> pilotos = new Repositorio<>();

    // ============================================================
    // BÚSQUEDAS BASE
    // ============================================================

    public Aeropuerto buscarAeropuerto(String codigo) {
        String buscado = normalizar(codigo);
        for (Aeropuerto a : aeropuertos.obtenerTodos()) {
            if (a.getCodigo().equalsIgnoreCase(buscado)) {
                return a;
            }
        }
        return null;
    }

    public Pasajero buscarPasajero(String documento) {
        String buscado = normalizar(documento);
        for (Pasajero p : pasajeros.obtenerTodos()) {
            if (p.getDocumento().equalsIgnoreCase(buscado)) {
                return p;
            }
        }
        return null;
    }

    public Vuelo buscarVuelo(String codigo) {
        String buscado = normalizar(codigo);
        for (Vuelo v : vuelos.obtenerTodos()) {
            if (v.getCodigo().equalsIgnoreCase(buscado)) {
                return v;
            }
        }
        return null;
    }

    // ============================================================
    // REGISTROS
    // ============================================================

    public void registrarAeropuerto(String codigo, String nombre, String ciudad, String pais) {
        String cod = normalizar(codigo).toUpperCase();

        if (cod.isEmpty() || nombre.trim().isEmpty()
                || ciudad.trim().isEmpty() || pais.trim().isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }
        if (buscarAeropuerto(cod) != null) {
            throw new IllegalArgumentException("Ya existe un aeropuerto con el código " + cod + ".");
        }

        aeropuertos.agregar(new Aeropuerto(cod, nombre.trim(), ciudad.trim(), pais.trim()));
    }

    public void registrarPasajero(String documento, String nombre, int edad) {
        String doc = normalizar(documento);

        if (doc.isEmpty() || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Documento y nombre son obligatorios.");
        }
        if (buscarPasajero(doc) != null) {
            throw new IllegalArgumentException("El documento " + doc + " ya está registrado.");
        }
        if (edad <= 0) {
            throw new IllegalArgumentException("La edad debe ser mayor que 0.");
        }

        pasajeros.agregar(new Pasajero(doc, nombre.trim(), edad));
    }

    public void registrarVuelo(String codigo, String codigoOrigen, String codigoDestino, int capacidad) {
        String cod = normalizar(codigo).toUpperCase();

        if (cod.isEmpty()) {
            throw new IllegalArgumentException("El código del vuelo es obligatorio.");
        }
        if (buscarVuelo(cod) != null) {
            throw new IllegalArgumentException("Ya existe un vuelo con el código " + cod + ".");
        }

        Aeropuerto origen = buscarAeropuerto(codigoOrigen);
        if (origen == null) {
            throw new IllegalArgumentException("El aeropuerto de origen no existe.");
        }

        Aeropuerto destino = buscarAeropuerto(codigoDestino);
        if (destino == null) {
            throw new IllegalArgumentException("El aeropuerto de destino no existe.");
        }
        if (origen.getCodigo().equalsIgnoreCase(destino.getCodigo())) {
            throw new IllegalArgumentException("El origen y el destino deben ser diferentes.");
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que 0.");
        }

        vuelos.agregar(new Vuelo(cod, origen, destino, capacidad));
    }

    public void registrarPiloto(String documento, String nombre, String licencia, int horasVuelo) {
        pilotos.agregar(new Piloto(normalizar(documento), nombre.trim(), licencia, horasVuelo));
    }

    // ============================================================
    // OPERACIONES
    // ============================================================

    /**
     * Orden de validación (falla rápido y con el mensaje más específico
     * posible antes de tocar el estado del sistema):
     *   1. Pasajero existe.
     *   2. Vuelo existe.
     *   3. Vuelo en estado PROGRAMADO.
     *   4. Pasajero no duplicado en ese vuelo.
     *   5. Cupo disponible -> lo valida el propio Vuelo lanzando la excepción.
     */
    public void reservarVuelo(String documento, String codigoVuelo) throws VueloLlenoException {
        Pasajero pasajero = buscarPasajero(documento);
        if (pasajero == null) {
            throw new IllegalArgumentException("El pasajero no está registrado.");
        }

        Vuelo vuelo = buscarVuelo(codigoVuelo);
        if (vuelo == null) {
            throw new IllegalArgumentException("El vuelo no existe.");
        }
        if (vuelo.getEstado() != EstadoVuelo.PROGRAMADO) {
            throw new IllegalArgumentException(
                    "Solo se puede reservar en vuelos PROGRAMADOS. Estado actual: " + vuelo.getEstado() + ".");
        }
        if (vuelo.tienePasajero(pasajero.getDocumento())) {
            throw new IllegalArgumentException("El pasajero ya tiene una reserva en este vuelo.");
        }

        vuelo.agregarPasajero(pasajero);
    }

    public void cambiarEstadoVuelo(String codigoVuelo, EstadoVuelo nuevoEstado) {
        Vuelo vuelo = buscarVuelo(codigoVuelo);
        if (vuelo == null) {
            throw new IllegalArgumentException("El vuelo no existe.");
        }
        vuelo.setEstado(nuevoEstado);
    }

    // ============================================================
    // CONSULTAS
    // ============================================================

    public List<Vuelo> obtenerVuelos() {
        return vuelos.obtenerTodos();
    }

    public List<Aeropuerto> obtenerAeropuertos() {
        return aeropuertos.obtenerTodos();
    }

    public List<Pasajero> obtenerPasajeros() {
        return pasajeros.obtenerTodos();
    }

    /**
     * Búsqueda insensible a mayúsculas y a espacios sobrantes.
     * Se usa contains() para permitir coincidencias parciales ("carta").
     */
    public List<Vuelo> buscarVuelosPorDestino(String ciudad) {
        String buscada = ciudad.trim().toLowerCase();
        List<Vuelo> resultado = new ArrayList<>();

        for (Vuelo v : vuelos.obtenerTodos()) {
            if (v.getDestino().getCiudad().toLowerCase().contains(buscada)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public List<Vuelo> buscarRuta(String ciudadOrigen, String ciudadDestino) {
        String origen = ciudadOrigen.trim().toLowerCase();
        String destino = ciudadDestino.trim().toLowerCase();
        List<Vuelo> resultado = new ArrayList<>();

        for (Vuelo v : vuelos.obtenerTodos()) {
            boolean coincideOrigen = v.getOrigen().getCiudad().toLowerCase().contains(origen);
            boolean coincideDestino = v.getDestino().getCiudad().toLowerCase().contains(destino);
            if (coincideOrigen && coincideDestino) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    /**
     * Set: la estructura correcta cuando el requisito es "sin repetidos".
     * Evita el clásico if (!lista.contains(x)) lista.add(x).
     */
    public Set<String> obtenerDestinosDisponibles() {
        Set<String> destinos = new HashSet<>();
        for (Vuelo v : vuelos.obtenerTodos()) {
            destinos.add(v.getDestino().getCiudad());
        }
        return destinos;
    }

    /**
     * Map: clave = ciudad destino, valor = cantidad de vuelos.
     * getOrDefault evita el NullPointerException típico al acumular contadores.
     */
    public Map<String, Integer> obtenerVuelosPorDestino() {
        Map<String, Integer> conteo = new HashMap<>();
        for (Vuelo v : vuelos.obtenerTodos()) {
            String ciudad = v.getDestino().getCiudad();
            conteo.put(ciudad, conteo.getOrDefault(ciudad, 0) + 1);
        }
        return conteo;
    }

    public int contarVuelosPorEstado(EstadoVuelo estado) {
        int total = 0;
        for (Vuelo v : vuelos.obtenerTodos()) {
            if (v.getEstado() == estado) {
                total++;
            }
        }
        return total;
    }

    public int totalReservas() {
        int total = 0;
        for (Vuelo v : vuelos.obtenerTodos()) {
            total += v.getCantidadPasajeros();
        }
        return total;
    }

    public Vuelo obtenerVueloConMayorOcupacion() {
        Vuelo mayor = null;
        for (Vuelo v : vuelos.obtenerTodos()) {
            if (mayor == null || v.getCantidadPasajeros() > mayor.getCantidadPasajeros()) {
                mayor = v;
            }
        }
        return mayor;
    }

    /**
     * POLIMORFISMO: una sola lista de Persona que contiene Pasajero y Piloto.
     * Quien la recorra llamará obtenerDescripcion() sin saber el tipo real.
     */
    public List<Persona> obtenerPersonas() {
        List<Persona> personas = new ArrayList<>();
        personas.addAll(pasajeros.obtenerTodos());
        personas.addAll(pilotos.obtenerTodos());
        return personas;
    }

    // ============================================================
    // UTILIDADES
    // ============================================================

    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim();
    }

    // ============================================================
    // DATOS INICIALES
    // ============================================================

    public void cargarDatosIniciales() {
        registrarAeropuerto("BOG", "El Dorado", "Bogotá", "Colombia");
        registrarAeropuerto("MDE", "José María Córdova", "Medellín", "Colombia");
        registrarAeropuerto("CLO", "Alfonso Bonilla Aragón", "Cali", "Colombia");
        registrarAeropuerto("CTG", "Rafael Núñez", "Cartagena", "Colombia");
        registrarAeropuerto("PSO", "Antonio Nariño", "Pasto", "Colombia");

        registrarVuelo("AV101", "BOG", "MDE", 180);
        registrarVuelo("AV202", "BOG", "CTG", 150);
        registrarVuelo("AV303", "MDE", "CLO", 120);
        registrarVuelo("AV404", "CLO", "BOG", 180);
        registrarVuelo("AV505", "BOG", "PSO", 90);

        registrarPasajero("1001", "Laura Gómez", 25);
        registrarPasajero("1002", "Andrés Martínez", 32);
        registrarPasajero("1003", "Carolina Díaz", 28);
        registrarPasajero("1004", "Daniel Torres", 41);

        registrarPiloto("2001", "Carlos Rodríguez", "LIC-COL-4471", 2500);
        registrarPiloto("2002", "Mariana Ospina", "LIC-COL-8820", 1340);

        // Reservas de ejemplo para que las estadísticas no arranquen en cero.
        try {
            reservarVuelo("1001", "AV101");
            reservarVuelo("1002", "AV101");
            reservarVuelo("1003", "AV202");
            reservarVuelo("1004", "AV303");
        } catch (VueloLlenoException e) {
            System.out.println("Error cargando reservas iniciales: " + e.getMessage());
        }
    }
}
