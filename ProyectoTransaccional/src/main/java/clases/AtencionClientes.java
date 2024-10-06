package clases;

import java.io.*;
import java.util.*;

class AtencionClientes {

    private static final String CSV_FILE_PATH = "clientes.csv";  // Ruta del archivo CSV

    public static void main(String[] args) throws ClienteDuplicadoException {
        Scanner scanner = new Scanner(System.in);
        Map<String, Sublista> coleccionAnidada = new HashMap<>();

        // Cargar datos desde el archivo CSV al iniciar la aplicación
        cargarDatosDesdeCSV(coleccionAnidada, CSV_FILE_PATH);

        int opcion;
        do {
            Utilidades.limpiarPantalla();
            System.out.println("Menú:");
            System.out.println("1. Crear Area de Atención");
            System.out.println("2. Crear Area con Clientes Iniciales");
            System.out.println("3. Añadir Clientes a un Area");
            System.out.println("4. Mostrar listado de Clientes");
            System.out.println("5. Mostrar Clientes de un Area Específica");
            System.out.println("6. Salir (Guarda los datos)");
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
                    System.out.println("Saliendo y guardando datos...");
                    // Guardar datos al archivo CSV al salir
                    guardarDatosCSV(coleccionAnidada, CSV_FILE_PATH);
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
    public static void crearSublista(Map<String, Sublista> coleccionAnidada, Scanner scanner){
        try {
            System.out.print("Ingrese el nombre del Nuevo Area: ");
            String area = scanner.nextLine().trim();  // Eliminar espacios en blanco al inicio y al final

            // Verificar si el nombre del área está vacío
            if (area.isEmpty()) {
                throw new IllegalArgumentException("Error: El nombre del área no puede estar vacío.");
            }

            // Verificar si el área ya existe (sin diferenciar mayúsculas/minúsculas)
            for (String key : coleccionAnidada.keySet()) {
                if (key.equalsIgnoreCase(area)) {
                    throw new AreaDuplicadaException("Error: El área '" + area + "' ya existe.");
                }
            }

            Sublista nuevaSublista = new Sublista(area);
            coleccionAnidada.put(area, nuevaSublista);
            System.out.println("Nuevo Area '" + area + "' creada.");

        } catch (IllegalArgumentException e) {
            // Manejar la excepción de nombre de área vacío
            System.out.println(e.getMessage());
            // Opcional: lanzar nuevamente la excepción si quieres que el llamador también lo sepa
            throw e;
        } catch (Exception e) {
            // Manejar otras excepciones que puedan surgir
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }



    public static void crearSublista(Map<String, Sublista> coleccionAnidada, Scanner scanner, List<Cliente> clientesIniciales) {
        System.out.print("Ingrese el nombre del Nuevo Area: ");
        String area = scanner.nextLine().trim();  // Eliminar espacios en blanco

        // Verificar si el nombre del área está vacío
        try {
            if (area.isEmpty()) {
                throw new IllegalArgumentException("Error: El nombre del área no puede estar vacío.");
            }

            // Verificar si el área ya existe (sin diferenciar mayúsculas/minúsculas)
            if (coleccionAnidada.containsKey(area.toLowerCase())) {
                throw new AreaDuplicadaException("Error: El área '" + area + "' ya existe.");
            }

            Sublista nuevaSublista = new Sublista(area);
            List<Cliente> clientesNoIngresados = new ArrayList<>(); // Lista para almacenar clientes no ingresados

            // Agregar clientes iniciales
            for (Cliente cliente : clientesIniciales) {
                // Verificar si el cliente ya existe en otras áreas
                boolean clienteExiste = coleccionAnidada.values().stream()
                        .anyMatch(sublista -> sublista.contieneCliente(cliente));

                if (clienteExiste) {
                    // Agregar a la lista de clientes no ingresados si ya existe en otra área
                    clientesNoIngresados.add(cliente);
                    continue; // Saltar a la siguiente iteración
                }

                try {
                    nuevaSublista.agregarCliente(cliente); // Agregar cliente a la nueva sublista
                } catch (ClienteDuplicadoException e) {
                    // Agregar a la lista de clientes no ingresados si se produce un duplicado
                    clientesNoIngresados.add(cliente);
                }
            }

            // Agregar la nueva sublista al mapa
            coleccionAnidada.put(area.toLowerCase(), nuevaSublista); // Usar .toLowerCase() para mantener la uniformidad
            Utilidades.limpiarPantalla();
            System.out.println("Nuevo Area '" + area + "' creada con clientes iniciales.");

            // Mostrar clientes que no fueron ingresados
            if (!clientesNoIngresados.isEmpty()) {
                System.out.println("Los siguientes clientes no pudieron ser ingresados por repeticion:");
                for (Cliente clienteNoIngresado : clientesNoIngresados) {
                    System.out.println("- " + clienteNoIngresado.getNombre() + ", " + clienteNoIngresado.getEdad());
                }
            }

        } catch (AreaDuplicadaException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
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

            try {
                sublista.agregarCliente(cliente);
                System.out.println("Cliente agregado: " + cliente);
            } catch (ClienteDuplicadoException e) {
                System.out.println(e.getMessage());  // Mostrar el mensaje de error si hay duplicado
            }
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
                System.out.println("--------------------------");
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

    // Metodo para cargar datos desde CSV al iniciar la aplicación
    public static void cargarDatosDesdeCSV(Map<String, Sublista> coleccionAnidada, String archivo) throws ClienteDuplicadoException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.startsWith("Area")) {
                    continue; // Saltar encabezado o líneas vacías
                }
                String[] datos = linea.split(",");
                String area = datos[0].trim();
                String nombreCliente = datos[1].trim();
                int edadCliente = Integer.parseInt(datos[2].trim());

                Sublista sublista = coleccionAnidada.getOrDefault(area, new Sublista(area));
                sublista.agregarCliente(new Cliente(nombreCliente, edadCliente));
                coleccionAnidada.putIfAbsent(area, sublista);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los datos desde el archivo CSV: " + e.getMessage());
        }
    }

    // Metodo para guardar los datos en un archivo CSV al salir de la aplicación
    public static void guardarDatosCSV(Map<String, Sublista> coleccionAnidada, String archivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            writer.println("Area,Nombre Cliente,Edad"); // Escribir encabezado
            for (Sublista sublista : coleccionAnidada.values()) {
                for (Cliente cliente : sublista.getClientes()) {
                    writer.printf("%s,%s,%d%n", sublista.getArea(), cliente.getNombre(), cliente.getEdad());
                }
            }
            System.out.println("Datos guardados correctamente en el archivo CSV.");
        } catch (IOException e) {
            System.out.println("Error al guardar los datos en el archivo CSV: " + e.getMessage());
        }
    }
}
