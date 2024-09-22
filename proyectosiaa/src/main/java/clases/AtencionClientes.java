package clases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class AtencionClientes {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
            Utilidades.limpiarPantalla();
            System.out.println("Menú:");
            System.out.println("1. Crear Area de Atención");
            System.out.println("2. Crear Area con Clientes Iniciales");
            System.out.println("3. Añadir Clientes a un Area");
            System.out.println("4. Mostrar listado de Clientes");
            System.out.println("5. Mostrar Clientes de un Area Específica");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    Utilidades.limpiarPantalla();
                    crearSublista(coleccionAnidada, scanner);
                    Utilidades.presioneTeclaParaContinuar();
                    break;
                case 2:
                    Utilidades.limpiarPantalla();
                    crearSublista(coleccionAnidada, scanner, obtenerClientes(scanner));
                    Utilidades.presioneTeclaParaContinuar();
                    break;
                case 3:
                    Utilidades.limpiarPantalla();
                    agregarClientesSublista(coleccionAnidada, scanner);
                    Utilidades.presioneTeclaParaContinuar();
                    break;
                case 4:
                    Utilidades.limpiarPantalla();
                    mostrarColeccion(coleccionAnidada);
                    Utilidades.presioneTeclaParaContinuar();
                    break;
                case 5:
                    Utilidades.limpiarPantalla();
                    mostrarColeccion(coleccionAnidada, scanner);
                    Utilidades.presioneTeclaParaContinuar();
                    break;
                case 6:
                    Utilidades.limpiarPantalla();
                    System.out.println("Saliendo...");
                    Utilidades.presioneTeclaParaContinuar();
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        } while (opcion != 6);

        scanner.close();
    }

    // Métodos de creación y visualización
    public static void crearSublista(Map<String, Sublista> coleccionAnidada, Scanner scanner) {
        System.out.print("Ingrese el nombre del Nuevo Area: ");
        String area = scanner.nextLine();
        Sublista nuevaSublista = new Sublista(area);
        coleccionAnidada.put(area, nuevaSublista);
        System.out.println("Nuevo Area '" + area + "' creada.");
    }

    public static void crearSublista(Map<String, Sublista> coleccionAnidada, Scanner scanner, List<Cliente> clientesIniciales) {
        System.out.print("Ingrese el nombre del Nuevo Area: ");
        String area = scanner.nextLine();
        Sublista nuevaSublista = new Sublista(area);
        for (Cliente cliente : clientesIniciales) {
            nuevaSublista.agregarCliente(cliente);
        }
        coleccionAnidada.put(area, nuevaSublista);
        System.out.println("Nuevo Area '" + area + "' creada con clientes iniciales.");
    }

    public static List<Cliente> obtenerClientes(Scanner scanner) {
        List<Cliente> clientes = new ArrayList<>();
        System.out.println("Ingrese los datos de los clientes iniciales (escriba 'salir' para terminar):");

        while (true) {
            System.out.print("Nombre del Cliente: ");
            String nombre = scanner.nextLine();
            if (nombre.equalsIgnoreCase("salir")) {
                break;
            }

            System.out.print("Edad del Cliente: ");
            int edad = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            Cliente cliente = new Cliente(nombre, edad);
            clientes.add(cliente);
        }
        return clientes;
    }

    public static void agregarClientesSublista(Map<String, Sublista> coleccionAnidada, Scanner scanner) {
        if (coleccionAnidada.isEmpty()) {
            System.out.println("No hay Areas creadas. Cree un Area primero.");
            return;
        }

        System.out.println("Seleccione el número del Area a la que desea agregar Clientes:");
        List<String> areas = new ArrayList<>(coleccionAnidada.keySet());
        for (int i = 0; i < areas.size(); i++) {
            System.out.println((i + 1) + ". " + areas.get(i));
        }

        int areaSeleccionada = scanner.nextInt();
        scanner.nextLine();

        if (areaSeleccionada < 1 || areaSeleccionada > areas.size()) {
            System.out.println("Area no existente.");
            return;
        }

        Sublista sublista = coleccionAnidada.get(areas.get(areaSeleccionada - 1));

        System.out.println("Ingrese los datos del Cliente para el Area '" + sublista.getArea() + "' (escriba 'salir' para terminar):");

        while (true) {
            System.out.print("Nombre del Cliente: ");
            String nombre = scanner.nextLine();
            if (nombre.equalsIgnoreCase("salir")) {
                break;
            }

            System.out.print("Edad del Cliente: ");
            int edad = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

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

    public static void mostrarColeccion(Map<String, Sublista> coleccionAnidada, Scanner scanner) {
        if (coleccionAnidada.isEmpty()) {
            System.out.println("No hay Areas Creadas");
            return;
        }

        System.out.println("Seleccione el número del Area:");
        List<String> areas = new ArrayList<>(coleccionAnidada.keySet());
        for (int i = 0; i < areas.size(); i++) {
            System.out.println((i + 1) + ". " + areas.get(i));
        }

        int areaSeleccionada = scanner.nextInt();
        scanner.nextLine();

        if (areaSeleccionada < 1 || areaSeleccionada > areas.size()) {
            System.out.println("Area no existente.");
            return;
        }

        Sublista sublista = coleccionAnidada.get(areas.get(areaSeleccionada - 1));

        System.out.println("Clientes en el Area '" + sublista.getArea() + "':");
        for (Cliente cliente : sublista.getClientes()) {
            System.out.println(cliente);
        }
    }
}
