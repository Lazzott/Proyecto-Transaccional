package clases;

class AsuntosMedicos extends Sublista {

    public AsuntosMedicos(String area) {
        super(area);
    }

    @Override
    public void agregarCliente(Cliente cliente) throws ClienteDuplicadoException {
        super.agregarCliente(cliente);
    }

    @Override
    public String obtenerTipo() {
        return "Asuntos Medicos";
    }
}

