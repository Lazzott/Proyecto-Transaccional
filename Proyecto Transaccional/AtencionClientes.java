import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AtencionClientes {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Sublista> coleccionAnidada = new ArrayList<>();
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
                    agregarValoresSublista(coleccionAnidada, scanner);
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

    public static void crearSublista(List<Sublista> coleccionAnidada, Scanner scanner) {
        System.out.print("Ingrese el nombre del Nuevo Area: ");
        String nombre = scanner.nextLine();
        Sublista nuevaSublista = new Sublista(nombre);
        coleccionAnidada.add(nuevaSublista);
        System.out.println("Nuevo Area '" + nombre + "' creada.");
    }

    public static void agregarValoresSublista(List<Sublista> coleccionAnidada, Scanner scanner) {
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

        System.out.println("Ingrese Clientes Para un Area '" + sublista.getArea() + "' (escriba 'salir' para terminar):");
        String input;
        while (true) {
            System.out.print("Valor: ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("salir")) {
                break;
            }
            sublista.getClientes().add(input);  // Añadiendo el valor a la sublista seleccionada
        }

        System.out.println("Valores agregados a la sublista '" + sublista.getArea() + "'.");
    }

    public static void mostrarColeccion(List<Sublista> coleccionAnidada) {
        if (coleccionAnidada.isEmpty()) {
            System.out.println("La colección está vacía.");
        } else {
            System.out.println("Listado de elementos en la colección anidada:");
            for (Sublista sublista : coleccionAnidada) {
                System.out.println("Sublista '" + sublista.getArea() + "': " + sublista.getClientes());
            }
        }
    }
}

class Sublista {
    private String Area;
    private List<Object> valores;

    public Sublista(String Area) {
        this.Area = Area;
        this.valores = new ArrayList<>();
    }

    public String getArea() {
        return Area;
    }

    public List<Object> getClientes() {
        return valores;
    }
}

