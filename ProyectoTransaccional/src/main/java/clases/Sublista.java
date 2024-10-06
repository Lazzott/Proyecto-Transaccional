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

    public void setArea(String area) {
        this.area = area;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
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

    public boolean contieneCliente(Cliente cliente) {
        for (Cliente c : clientes) {
            if (c.getNombre().equalsIgnoreCase(cliente.getNombre()) && c.getEdad() == cliente.getEdad()) {
                return true; // El cliente ya existe
            }
        }
        return false; // El cliente no existe
    }
}


