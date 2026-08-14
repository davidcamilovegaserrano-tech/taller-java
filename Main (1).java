import exceptions.VueloLlenoException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import models.Aeropuerto;
import models.EstadoVuelo;
import models.Pasajero;
import models.Persona;
import models.Vuelo;
import services.SistemaVuelos;

/**
 * Capa de presentación. Aquí SOLO se lee del teclado y se imprime.
 * Ninguna regla de negocio vive en este archivo.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final SistemaVuelos sistema = new SistemaVuelos();

    public static void main(String[] args) {
        sistema.cargarDatosIniciales();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    registrarAeropuerto();
                    break;
                case 2:
                    registrarPasajero();
                    break;
                case 3:
                    registrarVuelo();
                    break;
                case 4:
                    listarVuelos();
                    break;
                case 5:
                    buscarPorDestino();
                    break;
                case 6:
                    reservarVuelo();
                    break;
                case 7:
                    verPasajerosDeVuelo();
                    break;
                case 8:
                    cambiarEstadoVuelo();
                    break;
                case 9:
                    verEstadisticas();
                    break;
                case 10:
                    System.out.println("\nSaliendo del sistema...");
                    break;
                case 11:
                    consultarRutas();
                    break;
                default:
                    System.out.println("\nOpción inválida. Intente nuevamente.");
                    break;
            }
        } while (opcion != 10);

        sc.close();
    }

    // ============================================================
    // MENÚ
    // ============================================================

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
        System.out.println("9. Ver estadísticas");
        System.out.println("10. Salir");
        System.out.println("11. Consultar rutas disponibles (reto)");
        System.out.println();
    }

    // ============================================================
    // OPCIÓN 1
    // ============================================================

    private static void registrarAeropuerto() {
        System.out.println("\n--- REGISTRAR AEROPUERTO ---");
        String codigo = leerTexto("Código: ");
        String nombre = leerTexto("Nombre: ");
        String ciudad = leerTexto("Ciudad: ");
        String pais = leerTexto("País: ");

        try {
            sistema.registrarAeropuerto(codigo, nombre, ciudad, pais);
            System.out.println("Aeropuerto registrado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ============================================================
    // OPCIÓN 2
    // ============================================================

    private static void registrarPasajero() {
        System.out.println("\n--- REGISTRAR PASAJERO ---");
        String documento = leerTexto("Documento: ");
        String nombre = leerTexto("Nombre: ");
        int edad = leerEntero("Edad: ");

        try {
            sistema.registrarPasajero(documento, nombre, edad);
            System.out.println("Pasajero registrado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ============================================================
    // OPCIÓN 3
    // ============================================================

    private static void registrarVuelo() {
        System.out.println("\n--- REGISTRAR VUELO ---");
        String codigo = leerTexto("Código del vuelo: ");
        String origen = leerTexto("Código aeropuerto origen: ");
        String destino = leerTexto("Código aeropuerto destino: ");
        int capacidad = leerEntero("Capacidad máxima: ");

        try {
            sistema.registrarVuelo(codigo, origen, destino, capacidad);
            System.out.println("Vuelo registrado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ============================================================
    // OPCIÓN 4
    // ============================================================

    private static void listarVuelos() {
        List<Vuelo> vuelos = sistema.obtenerVuelos();

        if (vuelos.isEmpty()) {
            System.out.println("\nNo hay vuelos registrados.");
            return;
        }

        System.out.println();
        for (Vuelo v : vuelos) {
            System.out.println("--------------------------------");
            imprimirVuelo(v);
        }
        System.out.println("--------------------------------");
    }

    private static void imprimirVuelo(Vuelo v) {
        System.out.println("Vuelo: " + v.getCodigo());
        System.out.println("Origen: " + v.getOrigen().getCiudad());
        System.out.println("Destino: " + v.getDestino().getCiudad());
        System.out.println("Capacidad: " + v.getCapacidad());
        System.out.println("Pasajeros: " + v.getCantidadPasajeros());
        System.out.println("Estado: " + v.getEstado());
        System.out.println("Ocupación: " + formatearPorcentaje(v.getPorcentajeOcupacion()));
    }

    // ============================================================
    // OPCIÓN 5
    // ============================================================

    private static void buscarPorDestino() {
        String ciudad = leerTexto("\nIngrese destino: ");
        List<Vuelo> encontrados = sistema.buscarVuelosPorDestino(ciudad);

        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron vuelos hacia ese destino.");
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

    // ============================================================
    // OPCIÓN 6
    // ============================================================

    private static void reservarVuelo() {
        System.out.println("\n--- RESERVAR VUELO ---");
        String documento = leerTexto("Documento del pasajero: ");
        String codigoVuelo = leerTexto("Código del vuelo: ");

        try {
            sistema.reservarVuelo(documento, codigoVuelo);
            System.out.println("Reserva realizada correctamente.");
        } catch (VueloLlenoException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ============================================================
    // OPCIÓN 7
    // ============================================================

    private static void verPasajerosDeVuelo() {
        String codigo = leerTexto("\nCódigo del vuelo: ");
        Vuelo vuelo = sistema.buscarVuelo(codigo);

        if (vuelo == null) {
            System.out.println("El vuelo no existe.");
            return;
        }

        List<Pasajero> lista = vuelo.getPasajeros();
        if (lista.isEmpty()) {
            System.out.println("El vuelo no tiene pasajeros registrados.");
            return;
        }

        System.out.println("\n=== PASAJEROS DEL VUELO " + vuelo.getCodigo() + " ===");
        for (Pasajero p : lista) {
            System.out.println(p.getDocumento() + " - " + p.getNombre() + " - " + p.getEdad() + " años");
        }
        System.out.println("Total: " + lista.size() + " / " + vuelo.getCapacidad());
    }

    // ============================================================
    // OPCIÓN 8
    // ============================================================

    private static void cambiarEstadoVuelo() {
        String codigo = leerTexto("\nCódigo del vuelo: ");

        System.out.println("\nNuevo estado:");
        System.out.println("1. PROGRAMADO");
        System.out.println("2. ABORDANDO");
        System.out.println("3. EN_VUELO");
        System.out.println("4. FINALIZADO");
        System.out.println("5. CANCELADO");

        int opcion = leerEntero("Seleccione una opción: ");

        try {
            EstadoVuelo nuevoEstado = EstadoVuelo.porOpcion(opcion);
            sistema.cambiarEstadoVuelo(codigo, nuevoEstado);
            System.out.println("Estado actualizado a " + nuevoEstado + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ============================================================
    // OPCIÓN 9
    // ============================================================

    private static void verEstadisticas() {
        System.out.println("\n========= ESTADÍSTICAS =========");
        System.out.println("Aeropuertos registrados: " + sistema.obtenerAeropuertos().size());
        System.out.println("Vuelos registrados: " + sistema.obtenerVuelos().size());
        System.out.println("Pasajeros registrados: " + sistema.obtenerPasajeros().size());
        System.out.println("Vuelos programados: " + sistema.contarVuelosPorEstado(EstadoVuelo.PROGRAMADO));
        System.out.println("Vuelos abordando: " + sistema.contarVuelosPorEstado(EstadoVuelo.ABORDANDO));
        System.out.println("Vuelos en vuelo: " + sistema.contarVuelosPorEstado(EstadoVuelo.EN_VUELO));
        System.out.println("Vuelos finalizados: " + sistema.contarVuelosPorEstado(EstadoVuelo.FINALIZADO));
        System.out.println("Vuelos cancelados: " + sistema.contarVuelosPorEstado(EstadoVuelo.CANCELADO));

        System.out.println("\nTotal reservas realizadas: " + sistema.totalReservas());

        Vuelo mayor = sistema.obtenerVueloConMayorOcupacion();
        if (mayor != null) {
            System.out.println("\nVuelo con mayor ocupación:");
            System.out.println(mayor.getCodigo() + " - "
                    + mayor.getOrigen().getCiudad() + " -> " + mayor.getDestino().getCiudad());
            System.out.println(mayor.getCantidadPasajeros() + " / " + mayor.getCapacidad()
                    + " pasajeros (" + formatearPorcentaje(mayor.getPorcentajeOcupacion()) + ")");
        }

        // Set -> sin ciudades repetidas
        Set<String> destinos = sistema.obtenerDestinosDisponibles();
        System.out.println("\n=== DESTINOS DISPONIBLES ===");
        for (String ciudad : destinos) {
            System.out.println(ciudad);
        }

        // Map -> conteo por ciudad
        Map<String, Integer> vuelosPorDestino = sistema.obtenerVuelosPorDestino();
        System.out.println("\n=== VUELOS POR DESTINO ===");
        for (Map.Entry<String, Integer> entrada : vuelosPorDestino.entrySet()) {
            System.out.println(entrada.getKey() + ": " + entrada.getValue());
        }

        // Polimorfismo -> una sola lista, dos comportamientos distintos
        System.out.println("\n=== PERSONAL Y PASAJEROS DEL SISTEMA ===");
        for (Persona persona : sistema.obtenerPersonas()) {
            System.out.println(persona.obtenerDescripcion());
        }

        // Ocupación por vuelo
        System.out.println("\n=== OCUPACIÓN POR VUELO ===");
        for (Vuelo v : sistema.obtenerVuelos()) {
            System.out.println(v.getCodigo() + " | "
                    + v.getOrigen().getCiudad() + " -> " + v.getDestino().getCiudad());
            System.out.println("Ocupación: " + v.getCantidadPasajeros() + " / " + v.getCapacidad());
            System.out.println("Porcentaje: " + formatearPorcentaje(v.getPorcentajeOcupacion()));
        }
    }

    // ============================================================
    // OPCIÓN 11 (reto)
    // ============================================================

    private static void consultarRutas() {
        System.out.println("\n--- CONSULTAR RUTAS ---");
        String origen = leerTexto("Ciudad origen: ");
        String destino = leerTexto("Ciudad destino: ");

        List<Vuelo> rutas = sistema.buscarRuta(origen, destino);

        if (rutas.isEmpty()) {
            System.out.println("No existen vuelos para esa ruta.");
            return;
        }

        System.out.println("\nVuelos " + origen.trim() + " -> " + destino.trim() + "\n");
        for (Vuelo v : rutas) {
            System.out.println(v.getCodigo());
            System.out.println("Estado: " + v.getEstado());
            System.out.println("Disponibles: " + v.getAsientosDisponibles() + " asientos");
            System.out.println();
        }
    }

    // ============================================================
    // UTILIDADES DE ENTRADA
    // ============================================================

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    /**
     * Se lee SIEMPRE con nextLine() y se convierte con Integer.parseInt().
     * Mezclar nextInt() con nextLine() deja el salto de línea en el buffer y
     * "salta" la siguiente lectura de texto: es el bug número uno en consolas
     * Java de nivel junior.
     */
    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }

    private static String formatearPorcentaje(double valor) {
        return String.format(Locale.US, "%.1f%%", valor);
    }
}
