public class Concierto extends Evento {
    private String banda;

    public Concierto(String id, String nombre, int capacidad, String ubicacion, String banda, double precio) {
        super(id, nombre, capacidad, ubicacion,precio);
        this.banda = banda;
    }

    public String getBanda() { return banda; }

    @Override
    public double calcularPrecio() { return 1500.0; }

    @Override
    public String getTipo() { return "Concierto"; }

  
    @Override
    public String formatearParaArchivo() {
        return super.formatearParaArchivo() + ";" + banda;
    }
}