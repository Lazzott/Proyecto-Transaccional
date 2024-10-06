package clases;

class TramitesLegales extends Sublista {

    public TramitesLegales(String area) {
        super(area);
    }

    @Override
    public void agregarCliente(Cliente cliente) throws ClienteDuplicadoException {
        // Reglas específicas para Tramites Legales: solo clientes mayores de 21 años.
        if (cliente.getEdad() >= 18) {
            super.agregarCliente(cliente);
        } else {
            System.out.println("El cliente debe ser mayor de 18 años para tramites legales.");
        }
    }

    @Override
    public String obtenerTipo() {
        return "Tramites Legales";
    }
}

