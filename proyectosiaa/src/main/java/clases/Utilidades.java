package clases;

import java.util.Scanner;

public class Utilidades {

    // Métodos de la Clase
    public static void limpiarPantalla() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public static void presioneTeclaParaContinuar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Presione enter para continuar..");
        scanner.nextLine();
    }
}

