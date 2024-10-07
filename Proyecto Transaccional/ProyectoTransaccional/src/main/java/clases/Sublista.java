package clases;

import java.util.ArrayList;
import java.util.List;

public class Sublista {
    private String area;
    private List<Cliente> clientes;

    public Sublista(String area) {
        this.area = area;
        this.clientes = new ArrayList<>();
    }

    // Getters y Setters
    public String getArea() {
        return area;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void agregarCliente(Cliente cliente) throws ClienteDuplicadoException {
        // Verificar si ya existe un cliente con el mismo nombre y edad
        for (Cliente c : clientes) {
            if (c.getNombre().equals(cliente.getNombre()) && c.getEdad() == cliente.getEdad()) {
                throw new ClienteDuplicadoException("Error: El cliente '" + cliente.getNombre() + "' con edad " + cliente.getEdad() + " ya existe.");
            }
        }
        clientes.add(cliente);
    }
    public void eliminarCliente(String nombre) throws ClienteNoEncontradoException {
        boolean eliminado = clientes.removeIf(cliente -> cliente.getNombre().equalsIgnoreCase(nombre));
        if (!eliminado) {
            throw new ClienteNoEncontradoException("Cliente '" + nombre + "' no encontrado en el área '" + area + "'.");
        }
    }

    public boolean contieneCliente(Cliente cliente) {
        for (Cliente c : clientes) {
            if (c.getNombre().equalsIgnoreCase(cliente.getNombre()) && c.getEdad() == cliente.getEdad()) {
                return true; // El cliente ya existe
            }
        }
        return false; // El cliente no existe
    }

    public Cliente buscarCliente(String nombre) {
        for (Cliente cliente : clientes) {
            if (cliente.getNombre().equalsIgnoreCase(nombre)) {
                return cliente;
            }
        }
        return null; // Retorna null si no se encuentra
    }

    public String obtenerTipo() {
        return "Area General";
    }

}


