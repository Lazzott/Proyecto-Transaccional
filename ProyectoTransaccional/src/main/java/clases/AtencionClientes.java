package clases;

import java.io.*;
import java.util.*;

class AtencionClientes {

    private static final String CSV_FILE_PATH = "clientes.csv";  // Ruta del archivo CSV

    public static void main(String[] args) throws ClienteDuplicadoException, ClienteNoEncontradoException, NoHayClientesException {
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
            System.out.println("6. Eliminar Cliente de un Área");
            System.out.println("7. Modificar Datos de un Cliente");
            System.out.println("8. Resumen de Estadísticas");
            System.out.println("9. Salir (Guarda los datos)");
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
                    eliminarClienteSublista(coleccionAnidada, scanner);
                    Utilidades.presioneTeclaParaContinuar();
                    break;
                case 7:
                    Utilidades.limpiarPantalla();
                    modificarClienteSublista(coleccionAnidada, scanner); // Llamar al nuevo metodo
                    Utilidades.presioneTeclaParaContinuar();
                    break;
                case 8:
                    Utilidades.limpiarPantalla();
                    mostrarEstadisticas(coleccionAnidada);
                    Utilidades.presioneTeclaParaContinuar();
                    break;
                case 9:
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
        } while (opcion != 9); // Actualizar la condición de salida

        scanner.close();
    }

    // Métodos de creación y visualización
    public static void crearSublista(Map<String, Sublista> coleccionAnidada, Scanner scanner) {
        try {
            System.out.print("Ingrese el nombre del Nuevo Area: ");
            String area = scanner.nextLine().trim();

            if (area.isEmpty()) {
                throw new IllegalArgumentException("Error: El nombre del área no puede estar vacío.");
            }

            // Verificación si el área ya existe, sin importar mayúsculas/minúsculas
            for (String nombreArea : coleccionAnidada.keySet()) {
                if (nombreArea.equalsIgnoreCase(area)) {
                    throw new AreaDuplicadaException("El área '" + area + "' ya existe.");
                }
            }

            System.out.println("Seleccione el tipo de área:");
            System.out.println("1. Asuntos Médicos");
            System.out.println("2. Tramites Legales");
            System.out.println("3. Otros");
            int tipo = scanner.nextInt();
            scanner.nextLine();  // Consumir la nueva línea

            Sublista nuevaSublista = switch (tipo) {
                case 1 -> new AsuntosMedicos(area);
                case 2 -> new TramitesLegales(area);
                case 3 -> new Otros(area);
                default -> throw new IllegalArgumentException("Tipo de área no válida.");
            };

            coleccionAnidada.put(area, nuevaSublista);
            System.out.println("Nuevo " + nuevaSublista.obtenerTipo() + " creado.");

        } catch (AreaDuplicadaException e) {
            System.out.println("Error: " + e.getMessage());  // Manejamos la excepción de área duplicada
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
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
    public static void eliminarClienteSublista(Map<String, Sublista> coleccionAnidada, Scanner scanner) {
        try {
            if (coleccionAnidada.isEmpty()) {
                System.out.println("No hay Áreas creadas. Cree un Área primero.");
                return;
            }

            System.out.println("Seleccione el número del Área de la que desea eliminar un Cliente:");
            List<String> areas = new ArrayList<>(coleccionAnidada.keySet());
            for (int i = 0; i < areas.size(); i++) {
                System.out.println((i + 1) + ". " + areas.get(i));
            }

            int areaSeleccionada = scanner.nextInt();
            scanner.nextLine();

            if (areaSeleccionada < 1 || areaSeleccionada > areas.size()) {
                System.out.println("Área no existente.");
                return;
            }

            Sublista sublista = coleccionAnidada.get(areas.get(areaSeleccionada - 1));

            // Verificar si hay clientes en el área seleccionada
            if (sublista.getClientes().isEmpty()) {
                throw new NoHayClientesException("No hay clientes en el Área '" + sublista.getArea() + "'.");
            }

            // Mostrar lista de clientes en el área seleccionada
            System.out.println("Clientes en el Área '" + sublista.getArea() + "':");
            List<Cliente> clientes = sublista.getClientes();
            for (int i = 0; i < clientes.size(); i++) {
                System.out.println((i + 1) + ". " + clientes.get(i));
            }

            System.out.print("Seleccione el número del Cliente a eliminar: ");
            int clienteSeleccionado = scanner.nextInt();
            scanner.nextLine();

            if (clienteSeleccionado < 1 || clienteSeleccionado > clientes.size()) {
                System.out.println("Cliente no existente.");
                return;
            }

            // Eliminar el cliente de la sublista
            Cliente cliente = clientes.get(clienteSeleccionado - 1);
            sublista.eliminarCliente(cliente.getNombre()); // Asegúrate de que este metodo exista en la clase Sublista
            System.out.println("Cliente '" + cliente.getNombre() + "' eliminado correctamente.");
        } catch (NoHayClientesException e) {
            System.out.println(e.getMessage());
        } catch (ClienteNoEncontradoException e) {
            throw new RuntimeException(e);
        }
    }

    public static void modificarClienteSublista(Map<String, Sublista> coleccionAnidada, Scanner scanner) {
        try {
            if (coleccionAnidada.isEmpty()) {
                System.out.println("No hay Áreas creadas. Cree un Área primero.");
                return;
            }

            System.out.println("Seleccione el número del Área en la que desea modificar un Cliente:");
            List<String> areas = new ArrayList<>(coleccionAnidada.keySet());
            for (int i = 0; i < areas.size(); i++) {
                System.out.println((i + 1) + ". " + areas.get(i));
            }

            int areaSeleccionada = scanner.nextInt();
            scanner.nextLine();

            if (areaSeleccionada < 1 || areaSeleccionada > areas.size()) {
                System.out.println("Área no existente.");
                return;
            }

            Sublista sublista = coleccionAnidada.get(areas.get(areaSeleccionada - 1));

            // Verificar si hay clientes en el área seleccionada
            if (sublista.getClientes().isEmpty()) {
                throw new NoHayClientesException("No hay clientes en el Área '" + sublista.getArea() + "'.");
            }

            // Mostrar lista de clientes en el área seleccionada
            System.out.println("Clientes en el Área '" + sublista.getArea() + "':");
            List<Cliente> clientes = sublista.getClientes();
            for (int i = 0; i < clientes.size(); i++) {
                System.out.println((i + 1) + ". " + clientes.get(i));
            }

            System.out.print("Seleccione el número del Cliente a modificar: ");
            int clienteSeleccionado = scanner.nextInt();
            scanner.nextLine();

            if (clienteSeleccionado < 1 || clienteSeleccionado > clientes.size()) {
                System.out.println("Cliente no existente.");
                return;
            }

            Cliente cliente = clientes.get(clienteSeleccionado - 1); // Obtener cliente por el índice

            System.out.print("Ingrese el nuevo nombre del Cliente (actual: " + cliente.getNombre() + "): ");
            String nuevoNombre = scanner.nextLine().trim();
            System.out.print("Ingrese la nueva edad del Cliente (actual: " + cliente.getEdad() + "): ");
            int nuevaEdad = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            // Modificar los datos del cliente
            cliente.setNombre(nuevoNombre);
            cliente.setEdad(nuevaEdad); // Asegúrate de que estas funciones existan en la clase Cliente

            System.out.println("Datos del Cliente modificados correctamente.");
        } catch (NoHayClientesException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void mostrarColeccion(Map<String, Sublista> coleccionAnidada) {
        if (coleccionAnidada.isEmpty()) {
            System.out.println("No hay Áreas creadas.");
        } else {
            System.out.println("Lista de Clientes Por Cada Área:");
            for (Sublista sublista : coleccionAnidada.values()) {
                System.out.println(sublista.obtenerTipo() + " '" + sublista.getArea() + "':");
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
                    continue;
                }

                String[] datos = linea.split(",");
                String area = datos[0].trim();
                String tipoArea = datos[1].trim();
                String nombreCliente = datos[2].trim();
                int edadCliente = Integer.parseInt(datos[3].trim());

                Sublista sublista = switch (tipoArea.toLowerCase()) {
                    case "asuntos medicos" -> coleccionAnidada.getOrDefault(area, new AsuntosMedicos(area));
                    case "tramites legales" -> coleccionAnidada.getOrDefault(area, new TramitesLegales(area));
                    case "otros" -> coleccionAnidada.getOrDefault(area, new Otros(area));
                    default -> throw new IllegalArgumentException("Tipo de área desconocido: " + tipoArea);
                };

                // Crear la sublista del tipo adecuado

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
            writer.println("Area,Tipo de Area,Nombre Cliente,Edad"); // Escribir encabezado con el nuevo campo
            for (Sublista sublista : coleccionAnidada.values()) {
                for (Cliente cliente : sublista.getClientes()) {
                    writer.printf("%s,%s,%s,%d%n", sublista.getArea(), sublista.obtenerTipo(), cliente.getNombre(), cliente.getEdad());
                }
            }
            System.out.println("Datos guardados correctamente en el archivo CSV.");
        } catch (IOException e) {
            System.out.println("Error al guardar los datos en el archivo CSV: " + e.getMessage());
        }
    }

    public static void mostrarEstadisticas(Map<String, Sublista> coleccionAnidada) {
        if (coleccionAnidada.isEmpty()) {
            System.out.println("No hay Áreas creadas.");
            return;
        }

        int totalClientes = 0;
        int totalEdad = 0;
        Cliente clienteMasJoven = null;
        Cliente clienteMasViejo = null;
        Sublista areaConMasClientes = null;
        Sublista areaConMenosClientes = null;

        for (Sublista sublista : coleccionAnidada.values()) {
            List<Cliente> clientes = sublista.getClientes();
            totalClientes += clientes.size();

            for (Cliente cliente : clientes) {
                totalEdad += cliente.getEdad();

                // Encontrar el cliente más joven
                if (clienteMasJoven == null || cliente.getEdad() < clienteMasJoven.getEdad()) {
                    clienteMasJoven = cliente;
                }

                // Encontrar el cliente más viejo
                if (clienteMasViejo == null || cliente.getEdad() > clienteMasViejo.getEdad()) {
                    clienteMasViejo = cliente;
                }
            }

            // Encontrar el área con más y menos clientes
            if (areaConMasClientes == null || clientes.size() > areaConMasClientes.getClientes().size()) {
                areaConMasClientes = sublista;
            }

            if (areaConMenosClientes == null || clientes.size() < areaConMenosClientes.getClientes().size()) {
                areaConMenosClientes = sublista;
            }
        }

        double promedioEdad = totalClientes == 0 ? 0 : (double) totalEdad / totalClientes;
        System.out.println("Estadísticas Generales:");
        System.out.printf("Total de clientes: %d%n", totalClientes);
        System.out.printf("Promedio de edad: %.2f%n", promedioEdad);

        if (clienteMasJoven != null && clienteMasViejo != null) {
            System.out.printf("Cliente más joven: %s (%d años)%n", clienteMasJoven.getNombre(), clienteMasJoven.getEdad());
            System.out.printf("Cliente más viejo: %s (%d años)%n", clienteMasViejo.getNombre(), clienteMasViejo.getEdad());
        }

        if (areaConMasClientes != null && areaConMenosClientes != null) {
            System.out.printf("Área con más clientes: %s (%d clientes, %.2f%% del total)%n",
                    areaConMasClientes.getArea(),
                    areaConMasClientes.getClientes().size(),
                    (double) areaConMasClientes.getClientes().size() / totalClientes * 100);

            System.out.printf("Área con menos clientes: %s (%d clientes, %.2f%% del total)%n",
                    areaConMenosClientes.getArea(),
                    areaConMenosClientes.getClientes().size(),
                    (double) areaConMenosClientes.getClientes().size() / totalClientes * 100);
        }
    }
}