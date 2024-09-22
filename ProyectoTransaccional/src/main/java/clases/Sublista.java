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

    // Sobrecarga de métodos
    public void agregarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void agregarCliente(String nombre, int edad) {
        clientes.add(new Cliente(nombre, edad));
    }
}

