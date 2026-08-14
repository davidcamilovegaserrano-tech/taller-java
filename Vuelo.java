package models;

import exceptions.VueloLlenoException;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad central del dominio.
 *
 * Relaciones:
 *   Vuelo 1 --- 1 Aeropuerto (origen)
 *   Vuelo 1 --- 1 Aeropuerto (destino)
 *   Vuelo 1 --- N Pasajero
 *
 * La regla "no se puede exceder la capacidad" vive DENTRO de esta clase.
 * Si la dejáramos en el servicio o en el Main, cualquier código nuevo podría
 * saltársela. Una invariante se protege donde vive el dato.
 */
public class Vuelo {

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
        this.estado = EstadoVuelo.PROGRAMADO; // todo vuelo nace PROGRAMADO
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

    /**
     * Copia defensiva: si devolviéramos la lista real, el Main podría hacer
     * vuelo.getPasajeros().add(...) y romper el control de capacidad.
     */
    public List<Pasajero> getPasajeros() {
        return new ArrayList<>(pasajeros);
    }

    public int getCantidadPasajeros() {
        return pasajeros.size();
    }

    public int getAsientosDisponibles() {
        return capacidad - pasajeros.size();
    }

    public double getPorcentajeOcupacion() {
        if (capacidad == 0) {
            return 0;
        }
        return (pasajeros.size() * 100.0) / capacidad;
    }

    public boolean tienePasajero(String documento) {
        for (Pasajero p : pasajeros) {
            if (p.getDocumento().equalsIgnoreCase(documento)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Único punto de entrada para ocupar un asiento.
     */
    public void agregarPasajero(Pasajero pasajero) throws VueloLlenoException {
        if (pasajeros.size() >= capacidad) {
            throw new VueloLlenoException("No existen asientos disponibles.");
        }
        pasajeros.add(pasajero);
    }
}
