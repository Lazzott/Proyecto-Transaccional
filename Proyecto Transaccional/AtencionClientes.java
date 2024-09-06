import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Clase Sublista que contiene una lista de clientes
class Sublista {
    private String area;
    private List<Cliente> clientes;

    public Sublista(String area) {
        this.area = area;
        this.clientes = new ArrayList<>();
    }

    public String getArea() {
        return area;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}

// Clase Cliente que contiene el nombre y la edad del cliente
class Cliente {
    private String nombre;
    private int edad;

    // Constructor
    public Cliente(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    // Getters y setters
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
}

// Clase principal que maneja el menú y la interacción con el usuario
public class AtencionClientes {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Sublista> coleccionAnidada = new ArrayList<>();

         // Datos iniciales
         Sublista area1 = new Sublista("Soporte Técnico");
         area1.getClientes().add(new Cliente("Juan Perez", 30));
         area1.getClientes().add(new Cliente("Ana Gomez", 25));
 
         Sublista area2 = new Sublista("Atención al Cliente");
         area2.getClientes().add(new Cliente("Carlos Diaz", 40));
         area2.getClientes().add(new Cliente("Maria Lopez", 35));
 
         coleccionAnidada.add(area1);
         coleccionAnidada.add(area2);
         
        int opcion;

        do {
            System.out.println("Menú:");
            System.out.println("1. Crear Area de Atencion");
            System.out.println("2. Añadir Clientes a un Area");
            System.out.println("3. Mostrar listado de Clientes");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();  // Consumir el salto de línea

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

    // Función para crear una nueva sublista (area de atención)
    public static void crearSublista(List<Sublista> coleccionAnidada, Scanner scanner) {
        System.out.print("Ingrese el nombre del Nuevo Area: ");
        String area = scanner.nextLine();
        Sublista nuevaSublista = new Sublista(area);
        coleccionAnidada.add(nuevaSublista);
        System.out.println("Nuevo Area '" + area + "' creada.");
    }

    // Función para agregar clientes a una sublista seleccionada
    public static void agregarClientesSublista(List<Sublista> coleccionAnidada, Scanner scanner) {
        if (coleccionAnidada.isEmpty()) {
            System.out.println("No hay Areas creadas. Cree un Area primero.");
            return;
        }

        System.out.println("Seleccione el número del Area a la que desea agregar Clientes:");
        for (int i = 0; i < coleccionAnidada.size(); i++) {
            System.out.println((i + 1) + ". " + coleccionAnidada.get(i).getArea());
        }

        int sublistaSeleccionada = scanner.nextInt();
        scanner.nextLine();  // Consumir el salto de línea

        if (sublistaSeleccionada < 1 || sublistaSeleccionada > coleccionAnidada.size()) {
            System.out.println("Area no existente.");
            return;
        }

        Sublista sublista = coleccionAnidada.get(sublistaSeleccionada - 1);

        System.out.println("Ingrese los datos del Cliente para el Area '" + sublista.getArea() + "' (escriba 'salir' para terminar):");

        while (true) {
            System.out.print("Nombre del Cliente: ");
            String nombre = scanner.nextLine();
            if (nombre.equalsIgnoreCase("salir")) {
                break;
            }

            System.out.print("Edad del Cliente: ");
            int edad = scanner.nextInt();
            scanner.nextLine();  // Consumir el salto de línea

            // Crear objeto Cliente y agregarlo a la sublista
            Cliente cliente = new Cliente(nombre, edad);
            sublista.getClientes().add(cliente);

            System.out.println("Cliente agregado: " + cliente);
        }

        System.out.println("Clientes agregados al Area '" + sublista.getArea() + "'.");
    }

    // Función para mostrar la colección de clientes agrupados por cada area
    public static void mostrarColeccion(List<Sublista> coleccionAnidada) {
        if (coleccionAnidada.isEmpty()) {
            System.out.println("No hay Areas Creadas");
        } else {
            System.out.println("Lista de Clientes Por Cada Area:");
            for (Sublista sublista : coleccionAnidada) {
                System.out.println("Area '" + sublista.getArea() + "':");
                for (Cliente cliente : sublista.getClientes()) {
                    System.out.println(cliente);  // Esto llamará al método toString() de Cliente
                }
            }
        }
    }
}
