import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/*
 * ============================================================================
 * SISTEMA DE GESTION DE VUELOS Y AEROPUERTOS
 * Taller practico Java - Fundamentos + POO
 * ----------------------------------------------------------------------------
 * Archivo unico. Solo la clase Main es publica; las demas son package-private
 * (visibilidad por defecto), lo cual es legal en Java dentro del mismo archivo.
 * Ejecucion:  java Main.java     (Java 11+)
 *      o:     javac Main.java && java Main
 * ============================================================================
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final SistemaVuelos sistema = new SistemaVuelos();

    public static void main(String[] args) {
        sistema.cargarDatosIniciales();

        int opcion = 0;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1 -> registrarAeropuerto();
                case 2 -> registrarPasajero();
                case 3 -> registrarVuelo();
                case 4 -> listarVuelos();
                case 5 -> buscarPorDestino();
                case 6 -> reservarVuelo();
                case 7 -> verPasajerosDeVuelo();
                case 8 -> cambiarEstadoVuelo();
                case 9 -> verEstadisticas();
                case 10 -> System.out.println("\nSaliendo del sistema. Hasta pronto.");
                case 11 -> consultarRutas();
                case 12 -> verPorcentajesOcupacion();
                default -> System.out.println("\n[ERROR] Opcion no valida. Intente nuevamente.");
            }

        } while (opcion != 10);
    }

    // ------------------------------------------------------------------
    // MENU
    // ------------------------------------------------------------------
    private static void mostrarMenu() {
        System.out.println("\n==================================");
        System.out.println("SISTEMA AEROPUERTO JAVA");
        System.out.println("==================================");
        System.out.println("1. Registrar aeropuerto");
        System.out.println("2. Registrar pasajero");
        System.out.println("3. Registrar vuelo");
        System.out.println("4. Listar vuelos");
        System.out.println("5. Buscar vuelos por destino");
        System.out.println("6. Reservar vuelo");
        System.out.println("7. Ver pasajeros de un vuelo");
        System.out.println("8. Cambiar estado de vuelo");
        System.out.println("9. Ver estadisticas");
        System.out.println("10. Salir");
        System.out.println("--- Retos adicionales ---");
        System.out.println("11. Consultar rutas disponibles");
        System.out.println("12. Porcentaje de ocupacion por vuelo");
        System.out.println();
    }

    // ------------------------------------------------------------------
    // OPCION 1
    // ------------------------------------------------------------------
    private static void registrarAeropuerto() {
        System.out.println("\n--- REGISTRAR AEROPUERTO ---");
        String codigo = leerTexto("Codigo: ");
        String nombre = leerTexto("Nombre: ");
        String ciudad = leerTexto("Ciudad: ");
        String pais = leerTexto("Pais: ");

        try {
            sistema.registrarAeropuerto(codigo, nombre, ciudad, pais);
            System.out.println("Aeropuerto registrado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // OPCION 2
    // ------------------------------------------------------------------
    private static void registrarPasajero() {
        System.out.println("\n--- REGISTRAR PASAJERO ---");
        String documento = leerTexto("Documento: ");
        String nombre = leerTexto("Nombre: ");
        int edad = leerEntero("Edad: ");

        try {
            sistema.registrarPasajero(documento, nombre, edad);
            System.out.println("Pasajero registrado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // OPCION 3
    // ------------------------------------------------------------------
    private static void registrarVuelo() {
        System.out.println("\n--- REGISTRAR VUELO ---");
        String codigo = leerTexto("Codigo del vuelo: ");
        String origen = leerTexto("Codigo aeropuerto origen: ");
        String destino = leerTexto("Codigo aeropuerto destino: ");
        int capacidad = leerEntero("Capacidad maxima: ");

        try {
            sistema.registrarVuelo(codigo, origen, destino, capacidad);
            System.out.println("Vuelo registrado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // OPCION 4
    // ------------------------------------------------------------------
    private static void listarVuelos() {
        System.out.println("\n--- LISTADO DE VUELOS ---");
        List<Vuelo> vuelos = sistema.obtenerVuelos();

        if (vuelos.isEmpty()) {
            System.out.println("No hay vuelos registrados.");
            return;
        }

        for (Vuelo v : vuelos) {
            System.out.println("--------------------------------");
            System.out.println("Vuelo: " + v.getCodigo());
            System.out.println("Origen: " + v.getOrigen().getCiudad());
            System.out.println("Destino: " + v.getDestino().getCiudad());
            System.out.println("Capacidad: " + v.getCapacidad());
            System.out.println("Pasajeros: " + v.getPasajeros().size());
            System.out.println("Estado: " + v.getEstado());
        }
        System.out.println("--------------------------------");
    }

    // ------------------------------------------------------------------
    // OPCION 5
    // ------------------------------------------------------------------
    private static void buscarPorDestino() {
        System.out.println("\n--- BUSCAR VUELOS POR DESTINO ---");
        String ciudad = leerTexto("Ingrese destino: ");

        List<Vuelo> encontrados = sistema.buscarVuelosPorDestino(ciudad);

        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron vuelos hacia esa ciudad.");
            return;
        }

        System.out.println("\nResultados encontrados:\n");
        for (Vuelo v : encontrados) {
            System.out.println(v.getCodigo());
            System.out.println(v.getOrigen().getCiudad() + " -> " + v.getDestino().getCiudad());
            System.out.println("Estado: " + v.getEstado());
            System.out.println();
        }
    }

    // ------------------------------------------------------------------
    // OPCION 6
    // ------------------------------------------------------------------
    private static void reservarVuelo() {
        System.out.println("\n--- RESERVAR VUELO ---");
        String documento = leerTexto("Documento del pasajero: ");
        String codigoVuelo = leerTexto("Codigo del vuelo: ");

        try {
            sistema.reservarVuelo(documento, codigoVuelo);
            System.out.println("Reserva realizada correctamente.");
        } catch (VueloLlenoException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // OPCION 7
    // ------------------------------------------------------------------
    private static void verPasajerosDeVuelo() {
        System.out.println("\n--- PASAJEROS DE UN VUELO ---");
        String codigo = leerTexto("Codigo del vuelo: ");

        Vuelo vuelo = sistema.buscarVuelo(codigo);
        if (vuelo == null) {
            System.out.println("[ERROR] El vuelo no existe.");
            return;
        }

        List<Pasajero> lista = vuelo.getPasajeros();
        if (lista.isEmpty()) {
            System.out.println("El vuelo aun no tiene pasajeros reservados.");
            return;
        }

        System.out.println("\nVuelo " + vuelo.getCodigo() + " | "
                + vuelo.getOrigen().getCiudad() + " -> " + vuelo.getDestino().getCiudad());
        System.out.println("--------------------------------");
        for (Pasajero p : lista) {
            System.out.println(p.obtenerDescripcion());
        }
        System.out.println("--------------------------------");
        System.out.println("Total: " + lista.size() + " / " + vuelo.getCapacidad());
    }

    // ------------------------------------------------------------------
    // OPCION 8
    // ------------------------------------------------------------------
    private static void cambiarEstadoVuelo() {
        System.out.println("\n--- CAMBIAR ESTADO DE VUELO ---");
        String codigo = leerTexto("Codigo del vuelo: ");

        Vuelo vuelo = sistema.buscarVuelo(codigo);
        if (vuelo == null) {
            System.out.println("[ERROR] El vuelo no existe.");
            return;
        }

        System.out.println("\nEstado actual: " + vuelo.getEstado());
        System.out.println("1. PROGRAMADO");
        System.out.println("2. ABORDANDO");
        System.out.println("3. EN_VUELO");
        System.out.println("4. FINALIZADO");
        System.out.println("5. CANCELADO");
        int opcion = leerEntero("Nuevo estado: ");

        EstadoVuelo nuevo;
        switch (opcion) {
            case 1 -> nuevo = EstadoVuelo.PROGRAMADO;
            case 2 -> nuevo = EstadoVuelo.ABORDANDO;
            case 3 -> nuevo = EstadoVuelo.EN_VUELO;
            case 4 -> nuevo = EstadoVuelo.FINALIZADO;
            case 5 -> nuevo = EstadoVuelo.CANCELADO;
            default -> {
                System.out.println("[ERROR] Estado no valido.");
                return;
            }
        }

        vuelo.setEstado(nuevo);
        System.out.println("Estado actualizado a: " + vuelo.getEstado());
    }

    // ------------------------------------------------------------------
    // OPCION 9
    // ------------------------------------------------------------------
    private static void verEstadisticas() {
        System.out.println("\n========= ESTADISTICAS =========");
        System.out.println("Aeropuertos registrados: " + sistema.obtenerAeropuertos().size());
        System.out.println("Vuelos registrados: " + sistema.obtenerVuelos().size());
        System.out.println("Pasajeros registrados: " + sistema.obtenerPasajeros().size());
        System.out.println("Vuelos programados: " + sistema.contarVuelosPorEstado(EstadoVuelo.PROGRAMADO));
        System.out.println("Vuelos abordando: " + sistema.contarVuelosPorEstado(EstadoVuelo.ABORDANDO));
        System.out.println("Vuelos en vuelo: " + sistema.contarVuelosPorEstado(EstadoVuelo.EN_VUELO));
        System.out.println("Vuelos finalizados: " + sistema.contarVuelosPorEstado(EstadoVuelo.FINALIZADO));
        System.out.println("Vuelos cancelados: " + sistema.contarVuelosPorEstado(EstadoVuelo.CANCELADO));

        System.out.println("\nTotal reservas realizadas: " + sistema.contarReservas());

        Vuelo mayor = sistema.vueloConMayorOcupacion();
        System.out.println("\nVuelo con mayor ocupacion:");
        if (mayor == null) {
            System.out.println("No hay vuelos con reservas.");
        } else {
            System.out.println(mayor.getCodigo() + " - "
                    + mayor.getOrigen().getCiudad() + " -> " + mayor.getDestino().getCiudad());
            System.out.println(mayor.getPasajeros().size() + " / " + mayor.getCapacidad() + " pasajeros");
        }

        System.out.println("\n=== DESTINOS DISPONIBLES ===");
        Set<String> destinos = sistema.obtenerCiudadesDestino();
        if (destinos.isEmpty()) {
            System.out.println("Sin destinos registrados.");
        } else {
            for (String ciudad : destinos) {
                System.out.println(ciudad);
            }
        }

        System.out.println("\n=== VUELOS POR DESTINO ===");
        Map<String, Integer> resumen = sistema.contarVuelosPorDestino();
        if (resumen.isEmpty()) {
            System.out.println("Sin datos.");
        } else {
            for (Map.Entry<String, Integer> entrada : resumen.entrySet()) {
                System.out.println(entrada.getKey() + ": " + entrada.getValue());
            }
        }

        System.out.println("\n=== PERSONAS EN EL SISTEMA (POLIMORFISMO) ===");
        List<Persona> personas = sistema.obtenerPersonas();
        for (Persona persona : personas) {
            System.out.println(persona.obtenerDescripcion());
        }
        System.out.println("================================");
    }

    // ------------------------------------------------------------------
    // OPCION 11 - RETO ADICIONAL
    // ------------------------------------------------------------------
    private static void consultarRutas() {
        System.out.println("\n--- CONSULTAR RUTAS DISPONIBLES ---");
        String origen = leerTexto("Ciudad origen: ");
        String destino = leerTexto("Ciudad destino: ");

        List<Vuelo> ruta = sistema.buscarVuelosPorRuta(origen, destino);

        if (ruta.isEmpty()) {
            System.out.println("No existen vuelos para esa ruta.");
            return;
        }

        System.out.println("\nVuelos " + ruta.get(0).getOrigen().getCiudad()
                + " -> " + ruta.get(0).getDestino().getCiudad() + "\n");

        for (Vuelo v : ruta) {
            System.out.println(v.getCodigo());
            System.out.println("Estado: " + v.getEstado());
            System.out.println("Disponibles: " + v.asientosDisponibles() + " asientos");
            System.out.println();
        }
    }

    // ------------------------------------------------------------------
    // OPCION 12 - RETO ADICIONAL
    // ------------------------------------------------------------------
    private static void verPorcentajesOcupacion() {
        System.out.println("\n--- PORCENTAJE DE OCUPACION ---");
        List<Vuelo> vuelos = sistema.obtenerVuelos();

        if (vuelos.isEmpty()) {
            System.out.println("No hay vuelos registrados.");
            return;
        }

        for (Vuelo v : vuelos) {
            System.out.println(v.getCodigo() + " | "
                    + v.getOrigen().getCiudad() + " -> " + v.getDestino().getCiudad());
            System.out.println("Ocupacion: " + v.getPasajeros().size() + " / " + v.getCapacidad());
            System.out.printf("Porcentaje: %.1f%%%n%n", v.porcentajeOcupacion());
        }
    }

    // ------------------------------------------------------------------
    // UTILIDADES DE ENTRADA
    // ------------------------------------------------------------------
    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Debe ingresar un numero entero.");
            }
        }
    }
}

/* ==========================================================================
 * ENUMERACION
 * ======================================================================== */
enum EstadoVuelo {
    PROGRAMADO,
    ABORDANDO,
    EN_VUELO,
    FINALIZADO,
    CANCELADO
}

/* ==========================================================================
 * CLASE ABSTRACTA
 * ======================================================================== */
abstract class Persona {

    private String documento;
    private String nombre;

    public Persona(String documento, String nombre) {
        this.documento = documento;
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public abstract String obtenerDescripcion();
}

/* ==========================================================================
 * SUBCLASES
 * ======================================================================== */
class Pasajero extends Persona {

    private int edad;

    public Pasajero(String documento, String nombre, int edad) {
        super(documento, nombre);
        this.edad = edad;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    @Override
    public String obtenerDescripcion() {
        return "Pasajero: " + getNombre();
    }
}

class Piloto extends Persona {

    private String licencia;
    private int horasVuelo;

    public Piloto(String documento, String nombre, String licencia, int horasVuelo) {
        super(documento, nombre);
        this.licencia = licencia;
        this.horasVuelo = horasVuelo;
    }

    public String getLicencia() {
        return licencia;
    }

    public int getHorasVuelo() {
        return horasVuelo;
    }

    @Override
    public String obtenerDescripcion() {
        return "Piloto: " + getNombre() + " - " + horasVuelo + " horas de vuelo";
    }
}

/* ==========================================================================
 * AEROPUERTO
 * ======================================================================== */
class Aeropuerto {

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
}

/* ==========================================================================
 * VUELO
 * ======================================================================== */
class Vuelo {

    private String codigo;
    private Aeropuerto origen;
    private Aeropuerto destino;
    private int capacidad;
    private EstadoVuelo estado;
    private List<Pasajero> pasajeros;

    public Vuelo(String codigo, Aeropuerto origen, Aeropuerto destino, int capacidad) {
        this.codigo = codigo;
        this.origen = origen;
        this.destino = destino;
        this.capacidad = capacidad;
        this.estado = EstadoVuelo.PROGRAMADO;
        this.pasajeros = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public Aeropuerto getOrigen() {
        return origen;
    }

    public Aeropuerto getDestino() {
        return destino;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public EstadoVuelo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVuelo estado) {
        this.estado = estado;
    }

    public List<Pasajero> getPasajeros() {
        return pasajeros;
    }

    /**
     * Unico punto de entrada para agregar un pasajero.
     * La regla de negocio "no hay cupos" vive aqui, junto al dato que protege.
     */
    public void agregarPasajero(Pasajero pasajero) throws VueloLlenoException {
        if (pasajeros.size() >= capacidad) {
            throw new VueloLlenoException("No existen asientos disponibles.");
        }
        pasajeros.add(pasajero);
    }

    public boolean tienePasajero(String documento) {
        for (Pasajero p : pasajeros) {
            if (p.getDocumento().equalsIgnoreCase(documento)) {
                return true;
            }
        }
        return false;
    }

    public int asientosDisponibles() {
        return capacidad - pasajeros.size();
    }

    public double porcentajeOcupacion() {
        if (capacidad == 0) {
            return 0.0;
        }
        return (pasajeros.size() * 100.0) / capacidad;
    }
}

/* ==========================================================================
 * EXCEPCION PERSONALIZADA
 * ======================================================================== */
class VueloLlenoException extends Exception {

    public VueloLlenoException(String mensaje) {
        super(mensaje);
    }
}

/* ==========================================================================
 * CLASE GENERICA
 * ======================================================================== */
class Repositorio<T> {

    private List<T> elementos = new ArrayList<>();

    public void agregar(T elemento) {
        elementos.add(elemento);
    }

    public List<T> obtenerTodos() {
        return elementos;
    }
}

/* ==========================================================================
 * SERVICIO / LOGICA DE NEGOCIO
 * ======================================================================== */
class SistemaVuelos {

    private Repositorio<Aeropuerto> aeropuertos = new Repositorio<>();
    private Repositorio<Vuelo> vuelos = new Repositorio<>();
    private Repositorio<Pasajero> pasajeros = new Repositorio<>();
    private Repositorio<Piloto> pilotos = new Repositorio<>();

    // ---------------- BUSQUEDAS ----------------
    public Aeropuerto buscarAeropuerto(String codigo) {
        for (Aeropuerto a : aeropuertos.obtenerTodos()) {
            if (a.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return a;
            }
        }
        return null;
    }

    public Vuelo buscarVuelo(String codigo) {
        for (Vuelo v : vuelos.obtenerTodos()) {
            if (v.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return v;
            }
        }
        return null;
    }

    public Pasajero buscarPasajero(String documento) {
        for (Pasajero p : pasajeros.obtenerTodos()) {
            if (p.getDocumento().equalsIgnoreCase(documento.trim())) {
                return p;
            }
        }
        return null;
    }

    // ---------------- REGISTROS ----------------
    public void registrarAeropuerto(String codigo, String nombre, String ciudad, String pais) {
        if (codigo.isEmpty() || nombre.isEmpty() || ciudad.isEmpty() || pais.isEmpty()) {
            throw new IllegalArgumentException("Ningun campo puede quedar vacio.");
        }
        if (buscarAeropuerto(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un aeropuerto con el codigo " + codigo + ".");
        }
        aeropuertos.agregar(new Aeropuerto(codigo.toUpperCase(), nombre, ciudad, pais));
    }

    public void registrarPasajero(String documento, String nombre, int edad) {
        if (documento.isEmpty() || nombre.isEmpty()) {
            throw new IllegalArgumentException("Documento y nombre son obligatorios.");
        }
        if (buscarPasajero(documento) != null) {
            throw new IllegalArgumentException("El documento ya se encuentra registrado.");
        }
        if (edad <= 0) {
            throw new IllegalArgumentException("La edad debe ser mayor que 0.");
        }
        pasajeros.agregar(new Pasajero(documento, nombre, edad));
    }

    public void registrarPiloto(String documento, String nombre, String licencia, int horasVuelo) {
        pilotos.agregar(new Piloto(documento, nombre, licencia, horasVuelo));
    }

    public void registrarVuelo(String codigo, String codigoOrigen, String codigoDestino, int capacidad) {
        if (codigo.isEmpty()) {
            throw new IllegalArgumentException("El codigo del vuelo es obligatorio.");
        }
        if (buscarVuelo(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un vuelo con el codigo " + codigo + ".");
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

        vuelos.agregar(new Vuelo(codigo.toUpperCase(), origen, destino, capacidad));
    }

    // ---------------- RESERVA ----------------
    public void reservarVuelo(String documento, String codigoVuelo) throws VueloLlenoException {
        Pasajero pasajero = buscarPasajero(documento);
        if (pasajero == null) {
            throw new IllegalArgumentException("El pasajero no existe.");
        }

        Vuelo vuelo = buscarVuelo(codigoVuelo);
        if (vuelo == null) {
            throw new IllegalArgumentException("El vuelo no existe.");
        }
        if (vuelo.getEstado() != EstadoVuelo.PROGRAMADO) {
            throw new IllegalArgumentException("Solo se puede reservar en vuelos PROGRAMADO. Estado actual: "
                    + vuelo.getEstado() + ".");
        }
        if (vuelo.tienePasajero(documento)) {
            throw new IllegalArgumentException("El pasajero ya tiene reserva en este vuelo.");
        }

        vuelo.agregarPasajero(pasajero);
    }

    // ---------------- CONSULTAS ----------------
    public List<Vuelo> buscarVuelosPorDestino(String ciudad) {
        List<Vuelo> resultado = new ArrayList<>();
        String filtro = ciudad.trim().toLowerCase();

        for (Vuelo v : vuelos.obtenerTodos()) {
            if (v.getDestino().getCiudad().toLowerCase().contains(filtro)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public List<Vuelo> buscarVuelosPorRuta(String ciudadOrigen, String ciudadDestino) {
        List<Vuelo> resultado = new ArrayList<>();
        String filtroOrigen = ciudadOrigen.trim().toLowerCase();
        String filtroDestino = ciudadDestino.trim().toLowerCase();

        for (Vuelo v : vuelos.obtenerTodos()) {
            boolean coincideOrigen = v.getOrigen().getCiudad().toLowerCase().contains(filtroOrigen);
            boolean coincideDestino = v.getDestino().getCiudad().toLowerCase().contains(filtroDestino);
            if (coincideOrigen && coincideDestino) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public int contarVuelosPorEstado(EstadoVuelo estado) {
        int contador = 0;
        for (Vuelo v : vuelos.obtenerTodos()) {
            if (v.getEstado() == estado) {
                contador++;
            }
        }
        return contador;
    }

    public int contarReservas() {
        int total = 0;
        for (Vuelo v : vuelos.obtenerTodos()) {
            total += v.getPasajeros().size();
        }
        return total;
    }

    public Vuelo vueloConMayorOcupacion() {
        Vuelo mayor = null;
        for (Vuelo v : vuelos.obtenerTodos()) {
            if (v.getPasajeros().isEmpty()) {
                continue;
            }
            if (mayor == null || v.porcentajeOcupacion() > mayor.porcentajeOcupacion()) {
                mayor = v;
            }
        }
        return mayor;
    }

    public Set<String> obtenerCiudadesDestino() {
        Set<String> ciudades = new HashSet<>();
        for (Vuelo v : vuelos.obtenerTodos()) {
            ciudades.add(v.getDestino().getCiudad());
        }
        return ciudades;
    }

    public Map<String, Integer> contarVuelosPorDestino() {
        Map<String, Integer> resumen = new LinkedHashMap<>();
        for (Vuelo v : vuelos.obtenerTodos()) {
            String ciudad = v.getDestino().getCiudad();
            resumen.put(ciudad, resumen.getOrDefault(ciudad, 0) + 1);
        }
        return resumen;
    }

    /**
     * Construye la coleccion polimorfica: pasajeros y pilotos tratados
     * como Persona, cada uno respondiendo con su propia implementacion.
     */
    public List<Persona> obtenerPersonas() {
        List<Persona> personas = new ArrayList<>();
        personas.addAll(pasajeros.obtenerTodos());
        personas.addAll(pilotos.obtenerTodos());
        return personas;
    }

    public List<Aeropuerto> obtenerAeropuertos() {
        return aeropuertos.obtenerTodos();
    }

    public List<Vuelo> obtenerVuelos() {
        return vuelos.obtenerTodos();
    }

    public List<Pasajero> obtenerPasajeros() {
        return pasajeros.obtenerTodos();
    }

    // ---------------- DATOS INICIALES ----------------
    public void cargarDatosIniciales() {
        registrarAeropuerto("BOG", "El Dorado", "Bogota", "Colombia");
        registrarAeropuerto("MDE", "Jose Maria Cordova", "Medellin", "Colombia");
        registrarAeropuerto("CLO", "Alfonso Bonilla Aragon", "Cali", "Colombia");
        registrarAeropuerto("CTG", "Rafael Nunez", "Cartagena", "Colombia");
        registrarAeropuerto("PSO", "Antonio Narino", "Pasto", "Colombia");

        registrarVuelo("AV101", "BOG", "MDE", 180);
        registrarVuelo("AV202", "BOG", "CTG", 150);
        registrarVuelo("AV303", "MDE", "CLO", 120);
        registrarVuelo("AV404", "CLO", "BOG", 100);
        registrarVuelo("AV505", "BOG", "PSO", 80);

        registrarPasajero("1001", "Laura Gomez", 25);
        registrarPasajero("1002", "Andres Martinez", 32);
        registrarPasajero("1003", "Carolina Diaz", 28);
        registrarPasajero("1004", "Daniel Torres", 41);

        registrarPiloto("2001", "Carlos Rodriguez", "LIC-4587", 2500);
        registrarPiloto("2002", "Marcela Ruiz", "LIC-9021", 1800);

        // Reservas de ejemplo para que las estadisticas no arranquen vacias.
        try {
            reservarVuelo("1001", "AV101");
            reservarVuelo("1002", "AV101");
            reservarVuelo("1003", "AV202");
            reservarVuelo("1004", "AV505");
        } catch (VueloLlenoException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}
