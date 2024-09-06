import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Sublista {
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

class Cliente {
    private String nombre;
    private int edad;

    public Cliente(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Edad: " + edad;
    }

    // Sobrecarga de métodos
    public String toString(boolean formatoCorto) {
        if (formatoCorto) {
            return nombre;
        }
        return toString();
    }
}

public class AtencionClientes {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Usamos un Map para almacenar las áreas por nombre
        Map<String, Sublista> coleccionAnidada = new HashMap<>();

        // Datos iniciales
        Sublista area1 = new Sublista("Soporte Técnico");
        area1.agregarCliente(new Cliente("Juan Perez", 30));
        area1.agregarCliente(new Cliente("Ana Gomez", 25));

        Sublista area2 = new Sublista("Atención al Cliente");
        area2.agregarCliente(new Cliente("Carlos Diaz", 40));
        area2.agregarCliente(new Cliente("Maria Lopez", 35));

        coleccionAnidada.put(area1.getArea(), area1);
        coleccionAnidada.put(area2.getArea(), area2);

        int opcion;

        do {
            System.out.println("Menú:");
            System.out.println("1. Crear Area de Atención");
            System.out.println("2. Añadir Clientes a un Area");
            System.out.println("3. Mostrar listado de Clientes");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    crearSublista(coleccionAnidada, scanner);
                    break;
                case 2:
                    agregarClientesSublista(coleccionAnidada, scanner);
                    break;
                case 3:
                    mostrarColeccion(coleccionAnidada);
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        } while (opcion != 4);

        scanner.close();
    }

    public static void crearSublista(Map<String, Sublista> coleccionAnidada, Scanner scanner) {
        System.out.print("Ingrese el nombre del Nuevo Area: ");
        String area = scanner.nextLine();
        Sublista nuevaSublista = new Sublista(area);
        coleccionAnidada.put(area, nuevaSublista);
        System.out.println("Nuevo Area '" + area + "' creada.");
    }

    public static void agregarClientesSublista(Map<String, Sublista> coleccionAnidada, Scanner scanner) {
        if (coleccionAnidada.isEmpty()) {
            System.out.println("No hay Areas creadas. Cree un Area primero.");
            return;
        }

        System.out.println("Seleccione el nombre del Area a la que desea agregar Clientes:");
        for (String area : coleccionAnidada.keySet()) {
            System.out.println("- " + area);
        }

        String areaSeleccionada = scanner.nextLine();

        if (!coleccionAnidada.containsKey(areaSeleccionada)) {
            System.out.println("Area no existente.");
            return;
        }

        Sublista sublista = coleccionAnidada.get(areaSeleccionada);

        System.out.println("Ingrese los datos del Cliente para el Area '" + sublista.getArea() + "' (escriba 'salir' para terminar):");

        while (true) {
            System.out.print("Nombre del Cliente: ");
            String nombre = scanner.nextLine();
            if (nombre.equalsIgnoreCase("salir")) {
                break;
            }

            System.out.print("Edad del Cliente: ");
            int edad = scanner.nextInt();
            scanner.nextLine();

            Cliente cliente = new Cliente(nombre, edad);
            sublista.agregarCliente(cliente);

            System.out.println("Cliente agregado: " + cliente);
        }

        System.out.println("Clientes agregados al Area '" + sublista.getArea() + "'.");
    }

    public static void mostrarColeccion(Map<String, Sublista> coleccionAnidada) {
        if (coleccionAnidada.isEmpty()) {
            System.out.println("No hay Areas Creadas");
        } else {
            System.out.println("Lista de Clientes Por Cada Area:");
            for (Sublista sublista : coleccionAnidada.values()) {
                System.out.println("Area '" + sublista.getArea() + "':");
                for (Cliente cliente : sublista.getClientes()) {
                    System.out.println(cliente);
                }
            }
        }
    }
}
