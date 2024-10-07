package clases;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AtencionClientesSwing {

    private JFrame frame;
    private final Map<String, Sublista> coleccionAnidada;
    private static final String CSV_FILE_PATH = "clientes.csv";  // Ruta del archivo CSV
    private static final String TXT_FILE_PATH = "reporte_clientes.txt";  // Ruta del archivo TXT

    public AtencionClientesSwing() {
        coleccionAnidada = new HashMap<>();
        initialize();
        cargarDatos();  // Cargar los datos al iniciar la aplicación
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AtencionClientesSwing window = new AtencionClientesSwing();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    // Metodo para inicializar los componentes del GUI
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gestión de Clientes");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Añadir un WindowListener para capturar el evento de cerrar ventana
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame, "¿Desea salir y guardar los datos?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    guardarDatos();  // Guardar los datos en el CSV
                    generarReporteTxt();  // Generar archivo TXT con estadísticas y clientes
                    frame.dispose();  // Cerrar la ventana y salir
                }
            }
        });

        JLabel lblTitulo = new JLabel("Menú de Gestión de Clientes");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setBounds(180, 20, 300, 20);
        frame.getContentPane().add(lblTitulo);

        JButton btnCrearArea = new JButton("Crear Área de Atención");
        btnCrearArea.setBounds(200, 70, 200, 30);
        frame.getContentPane().add(btnCrearArea);

        btnCrearArea.addActionListener(e -> crearArea());

        JButton btnAgregarClientes = new JButton("Añadir Clientes a un Área");
        btnAgregarClientes.setBounds(200, 120, 200, 30);
        frame.getContentPane().add(btnAgregarClientes);

        btnAgregarClientes.addActionListener(e -> agregarClientes());

        JButton btnMostrarClientes = new JButton("Mostrar Clientes de un Área");
        btnMostrarClientes.setBounds(200, 170, 200, 30);
        frame.getContentPane().add(btnMostrarClientes);

        btnMostrarClientes.addActionListener(e -> mostrarClientes());

        // Nuevo botón para mostrar todos los clientes de todas las áreas
        JButton btnMostrarTodosLosClientes = new JButton("Mostrar Todos los Clientes");
        btnMostrarTodosLosClientes.setBounds(200, 220, 200, 30);
        frame.getContentPane().add(btnMostrarTodosLosClientes);

        btnMostrarTodosLosClientes.addActionListener(e -> mostrarTodosLosClientes());

        // Botón para modificar clientes
        JButton btnModificarCliente = new JButton("Modificar Cliente");
        btnModificarCliente.setBounds(200, 270, 200, 30);
        frame.getContentPane().add(btnModificarCliente);

        btnModificarCliente.addActionListener(e -> modificarCliente());

        // Botón para eliminar clientes
        JButton btnEliminarCliente = new JButton("Eliminar Cliente");
        btnEliminarCliente.setBounds(200, 320, 200, 30);
        frame.getContentPane().add(btnEliminarCliente);

        btnEliminarCliente.addActionListener(e -> eliminarCliente());

        // Botón para mostrar estadísticas
        JButton btnMostrarEstadisticas = new JButton("Mostrar Estadísticas");
        btnMostrarEstadisticas.setBounds(200, 370, 200, 30);
        frame.getContentPane().add(btnMostrarEstadisticas);

        btnMostrarEstadisticas.addActionListener(e -> mostrarEstadisticas());

        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(200, 370, 200, 30);
        frame.getContentPane().add(btnSalir);

        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "¿Desea salir y guardar los datos?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                guardarDatos();  // Guardar los datos antes de salir
                frame.dispose();  // Cerrar la ventana y salir
            }
        });
    }

    // Metodo para cargar datos desde el archivo CSV al iniciar la aplicación
    private void cargarDatos() {
        File csvFile = new File(CSV_FILE_PATH);
        if (csvFile.exists()) {
            try {
                AtencionClientes.cargarDatosDesdeCSV(coleccionAnidada, CSV_FILE_PATH);
                JOptionPane.showMessageDialog(frame, "Datos cargados desde el archivo CSV.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error al cargar los datos desde el archivo CSV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Archivo CSV no encontrado. Iniciando con datos vacíos.");
        }
    }

    // Metodo para guardar datos en el archivo CSV al salir de la aplicación
    private void guardarDatos() {
        try {
            AtencionClientes.guardarDatosCSV(coleccionAnidada, CSV_FILE_PATH);
            JOptionPane.showMessageDialog(frame, "Datos guardados correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error al guardar los datos en el archivo CSV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo para crear un área de atención
    private void crearArea() {
        String nombreArea = JOptionPane.showInputDialog(frame, "Ingrese el nombre del área:");
        if (nombreArea == null || nombreArea.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nombre de área no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] opciones = {"Asuntos Médicos", "Trámites Legales", "Otros"};
        String tipoArea = (String) JOptionPane.showInputDialog(frame, "Seleccione el tipo de área:", "Tipo de Área",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (tipoArea == null) {
            return;
        }

        Sublista nuevaSublista = switch (tipoArea) {
            case "Asuntos Médicos" -> new AsuntosMedicos(nombreArea);
            case "Trámites Legales" -> new TramitesLegales(nombreArea);
            case "Otros" -> new Otros(nombreArea);
            default -> throw new IllegalArgumentException("Tipo de área no válida.");
        };

        coleccionAnidada.put(nombreArea, nuevaSublista);
        JOptionPane.showMessageDialog(frame, "Área de " + tipoArea + " creada exitosamente.");

        // Preguntar si se desean agregar clientes iniciales
        int confirm = JOptionPane.showConfirmDialog(frame, "¿Desea agregar clientes iniciales a esta área?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            agregarClientesIniciales(nuevaSublista);
        }
    }

    // Metodo para agregar clientes iniciales
    private void agregarClientesIniciales(Sublista sublista) {
        while (true) {
            String nombreCliente = JOptionPane.showInputDialog(frame, "Ingrese el nombre del cliente (o 'salir' para terminar):");
            if (nombreCliente == null || nombreCliente.equalsIgnoreCase("salir")) {
                break;  // Salir si el usuario escribe "salir"
            }

            String edadStr = JOptionPane.showInputDialog(frame, "Ingrese la edad del cliente:");
            if (edadStr == null || edadStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Edad no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            int edadCliente;
            try {
                edadCliente = Integer.parseInt(edadStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "La edad debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            Cliente cliente = new Cliente(nombreCliente, edadCliente);
            try {
                sublista.agregarCliente(cliente);
                JOptionPane.showMessageDialog(frame, "Cliente " + nombreCliente + " añadido exitosamente.");
            } catch (ClienteDuplicadoException e) {
                JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Metodo para añadir clientes a un área
    private void agregarClientes() {
        if (coleccionAnidada.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay áreas creadas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] areas = coleccionAnidada.keySet().toArray(new String[0]);
        String areaSeleccionada = (String) JOptionPane.showInputDialog(frame, "Seleccione un área:",
                "Añadir Clientes", JOptionPane.QUESTION_MESSAGE, null, areas, areas[0]);

        if (areaSeleccionada == null) {
            return;
        }

        Sublista sublista = coleccionAnidada.get(areaSeleccionada);
        agregarClientesIniciales(sublista);  // Llamar a metodo para añadir clientes
    }

    // Metodo para mostrar clientes de un área específica
    private void mostrarClientes() {
        if (coleccionAnidada.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay áreas creadas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] areas = coleccionAnidada.keySet().toArray(new String[0]);
        String areaSeleccionada = (String) JOptionPane.showInputDialog(frame, "Seleccione un área:",
                "Mostrar Clientes", JOptionPane.QUESTION_MESSAGE, null, areas, areas[0]);

        if (areaSeleccionada == null) {
            return;
        }

        Sublista sublista = coleccionAnidada.get(areaSeleccionada);
        StringBuilder clientesLista = new StringBuilder("Clientes en el área '" + areaSeleccionada + "':\n");
        for (Cliente cliente : sublista.getClientes()) {
            clientesLista.append(cliente).append("\n");
        }

        JOptionPane.showMessageDialog(frame, clientesLista.isEmpty() ? "No hay clientes en esta área." : clientesLista.toString());
    }

    // Metodo para mostrar todos los clientes de todas las áreas
    private void mostrarTodosLosClientes() {
        if (coleccionAnidada.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay áreas creadas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder todosLosClientes = new StringBuilder("Clientes en todas las áreas:\n");
        for (Map.Entry<String, Sublista> entry : coleccionAnidada.entrySet()) {
            String area = entry.getKey();
            Sublista sublista = entry.getValue();
            todosLosClientes.append("\nÁrea: ").append(area).append("\n");
            for (Cliente cliente : sublista.getClientes()) {
                todosLosClientes.append(cliente).append("\n");
            }
        }

        JOptionPane.showMessageDialog(frame, todosLosClientes.isEmpty() ? "No hay clientes en las áreas." : todosLosClientes.toString());
    }

    // Metodo para modificar un cliente existente
    private void modificarCliente() {
        if (coleccionAnidada.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay áreas creadas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] areas = coleccionAnidada.keySet().toArray(new String[0]);
        String areaSeleccionada = (String) JOptionPane.showInputDialog(frame, "Seleccione un área:",
                "Modificar Cliente", JOptionPane.QUESTION_MESSAGE, null, areas, areas[0]);

        if (areaSeleccionada == null) {
            return;
        }

        Sublista sublista = coleccionAnidada.get(areaSeleccionada);

        // Verificar si hay clientes en el área
        if (sublista.getClientes().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay clientes en esta área.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] clientes = sublista.getClientes().stream().map(Cliente::getNombre).toArray(String[]::new);
        String clienteSeleccionado = (String) JOptionPane.showInputDialog(frame, "Seleccione un cliente:",
                "Modificar Cliente", JOptionPane.QUESTION_MESSAGE, null, clientes, clientes[0]);

        if (clienteSeleccionado == null) {
            return;
        }

        Cliente cliente = sublista.buscarCliente(clienteSeleccionado);
        if (cliente != null) {
            String nuevoNombre = JOptionPane.showInputDialog(frame, "Ingrese el nuevo nombre del cliente:", cliente.getNombre());
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                String nuevaEdadStr = JOptionPane.showInputDialog(frame, "Ingrese la nueva edad del cliente:", cliente.getEdad());
                if (nuevaEdadStr != null && !nuevaEdadStr.trim().isEmpty()) {
                    try {
                        int nuevaEdad = Integer.parseInt(nuevaEdadStr);
                        cliente.setNombre(nuevoNombre);
                        cliente.setEdad(nuevaEdad);
                        JOptionPane.showMessageDialog(frame, "Cliente modificado exitosamente.");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "La edad debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo para eliminar un cliente existente
    private void eliminarCliente() {
        if (coleccionAnidada.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay áreas creadas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] areas = coleccionAnidada.keySet().toArray(new String[0]);
        String areaSeleccionada = (String) JOptionPane.showInputDialog(frame, "Seleccione un área:",
                "Eliminar Cliente", JOptionPane.QUESTION_MESSAGE, null, areas, areas[0]);

        if (areaSeleccionada == null) {
            return;
        }

        Sublista sublista = coleccionAnidada.get(areaSeleccionada);

        // Verificar si hay clientes en el área
        if (sublista.getClientes().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay clientes en esta área.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] clientes = sublista.getClientes().stream().map(Cliente::getNombre).toArray(String[]::new);
        String clienteSeleccionado = (String) JOptionPane.showInputDialog(frame, "Seleccione un cliente:",
                "Eliminar Cliente", JOptionPane.QUESTION_MESSAGE, null, clientes, clientes[0]);

        if (clienteSeleccionado == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "¿Está seguro de que desea eliminar al cliente " + clienteSeleccionado + "?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                sublista.eliminarCliente(clienteSeleccionado);
                JOptionPane.showMessageDialog(frame, "Cliente eliminado exitosamente.");
            } catch (ClienteNoEncontradoException e) {
                JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarEstadisticas() {
        if (coleccionAnidada.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay áreas creadas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int totalClientes = 0;
        int totalEdad = 0;
        int menorEdad = Integer.MAX_VALUE;
        int mayorEdad = Integer.MIN_VALUE;
        String clienteMasJoven = null;
        String clienteMasViejo = null;
        String areaMasClientes = null;
        String areaMenosClientes = null;
        int maxClientes = Integer.MIN_VALUE;
        int minClientes = Integer.MAX_VALUE;

        for (Sublista sublista : coleccionAnidada.values()) {
            int numClientesEnArea = sublista.getClientes().size();
            totalClientes += numClientesEnArea;

            for (Cliente cliente : sublista.getClientes()) {
                totalEdad += cliente.getEdad();
                if (cliente.getEdad() < menorEdad) {
                    menorEdad = cliente.getEdad();
                    clienteMasJoven = cliente.getNombre();
                }
                if (cliente.getEdad() > mayorEdad) {
                    mayorEdad = cliente.getEdad();
                    clienteMasViejo = cliente.getNombre();
                }
            }

            if (numClientesEnArea > maxClientes) {
                maxClientes = numClientesEnArea;
                areaMasClientes = sublista.getArea();
            }
            if (numClientesEnArea < minClientes) {
                minClientes = numClientesEnArea;
                areaMenosClientes = sublista.getArea();
            }
        }

        double promedioEdad = totalClientes > 0 ? (double) totalEdad / totalClientes : 0;

        String estadisticas = "Estadísticas de los Clientes:\n" +
                "Total de clientes: " + totalClientes + "\n" +
                "Promedio de edad: " + String.format("%.2f", promedioEdad) + "\n" +
                "Cliente más joven: " + clienteMasJoven + " (" + menorEdad + " años)\n" +
                "Cliente más viejo: " + clienteMasViejo + " (" + mayorEdad + " años)\n" +
                "Área con más clientes: " + areaMasClientes + " (" + maxClientes + " clientes)\n" +
                "Área con menos clientes: " + areaMenosClientes + " (" + minClientes + " clientes)\n";

        JOptionPane.showMessageDialog(frame, estadisticas);
    }

    private void generarReporteTxt() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TXT_FILE_PATH))) {
            // Primera parte: Mostrar estadísticas
            writer.println("=== Estadísticas ===");
            String estadisticas = generarEstadisticas();
            writer.println(estadisticas);
            writer.println();

            // Segunda parte: Mostrar clientes separados por tipo de área
            writer.println("=== Clientes por Área ===");
            for (Map.Entry<String, Sublista> entry : coleccionAnidada.entrySet()) {
                String nombreArea = entry.getKey();
                Sublista sublista = entry.getValue();
                writer.println("Área: " + nombreArea + " (" + sublista.obtenerTipo() + ")");
                for (Cliente cliente : sublista.getClientes()) {
                    writer.println(cliente);
                }
                writer.println();  // Salto de línea entre áreas
            }

            JOptionPane.showMessageDialog(frame, "Reporte generado exitosamente en " + TXT_FILE_PATH);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error al generar el reporte en TXT: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generarEstadisticas() {
        int totalClientes = 0;
        Map<String, Integer> clientesPorArea = new HashMap<>();

        for (Map.Entry<String, Sublista> entry : coleccionAnidada.entrySet()) {
            Sublista sublista = entry.getValue();
            int cantidadClientes = sublista.getClientes().size();
            clientesPorArea.put(sublista.obtenerTipo(), clientesPorArea.getOrDefault(sublista.obtenerTipo(), 0) + cantidadClientes);
            totalClientes += cantidadClientes;
        }

        StringBuilder estadisticas = new StringBuilder();
        estadisticas.append("Total de clientes: ").append(totalClientes).append("\n");
        for (Map.Entry<String, Integer> entry : clientesPorArea.entrySet()) {
            estadisticas.append("Clientes en área '").append(entry.getKey()).append("': ").append(entry.getValue()).append("\n");
        }

        return estadisticas.toString();
    }
}
