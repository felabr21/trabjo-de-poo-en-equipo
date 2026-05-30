public class Teatro extends Evento {
    private String obra;

    public Teatro(String id, String nombre, int capacidad, String ubicacion, String obra, double precio) {
        super(id, nombre, capacidad, ubicacion,precio);
        this.obra = obra;
    }

    public String getObra() { return obra; }

    @Override
    public double calcularPrecio() { return 1000.0; }

    @Override
    public String getTipo() { return "Teatro"; }

    
    @Override
    public String formatearParaArchivo() {
        return super.formatearParaArchivo() + ";" + obra;
    }
}