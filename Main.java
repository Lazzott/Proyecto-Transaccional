import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> nombres = new ArrayList<>();

        System.out.println("Ingresa los nombres. Presiona Enter sin ingresar texto para terminar:");

        while (true) {
            String nombre = scanner.nextLine();

            if (nombre.isEmpty()) {
                break;
            }

            nombres.add(nombre);
        }

        System.out.println("Los nombres ingresados son:");
        for (String nombre : nombres) {
            System.out.println(nombre);
        }

        scanner.close();
    }
}
