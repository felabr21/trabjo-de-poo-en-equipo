public class Conferencia extends Evento { 
    private String orador; 

    public Conferencia(String id, String nombre, int capacidad, String ubicacion, String orador,double precio) {
        super(id, nombre, capacidad, ubicacion, precio); 
        this.orador = orador;
    }

    @Override 
    public double calcularPrecio() { return 300.0; }

    @Override 
    public String getTipo() { return "Conferencia"; }

    public String getOrador() { return orador; }

  
    @Override
    public String formatearParaArchivo() {
        return super.formatearParaArchivo() + ";" + orador;
    }
}