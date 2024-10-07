package clases;

class Otros extends Sublista {

    public Otros(String area) {
        super(area);
    }

    @Override
    public void agregarCliente(Cliente cliente) throws ClienteDuplicadoException {
        // No hay restricciones adicionales, se permite cualquier cliente.
        super.agregarCliente(cliente);
    }

    @Override
    public String obtenerTipo() {
        return "Otros";
    }
}

