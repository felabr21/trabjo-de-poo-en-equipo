
public class Boleto {
    private String idBoleto;
    private String idEvento;
    private String nombreAsistente;
    private String fechaCompra;
    private double precio;

    public Boleto(String idBoleto, String idEvento, String nombreAsistente, double precio) {
        this.idBoleto = idBoleto;
        this.idEvento = idEvento;
        this.nombreAsistente = nombreAsistente;
        this.precio = precio;
        this.fechaCompra = java.time.LocalDate.now().toString();
    }

    // Constructor para cargar desde archivo
    public Boleto(String idBoleto, String idEvento, String nombreAsistente, String fechaCompra, double precio) {
        this.idBoleto = idBoleto;
        this.idEvento = idEvento;
        this.nombreAsistente = nombreAsistente;
        this.fechaCompra = fechaCompra;
        this.precio = precio;
    }

    // Getters
    public String getIdBoleto() { return idBoleto; }
    public String getIdEvento() { return idEvento; }
    public String getNombreAsistente() { return nombreAsistente; }
    public String getFechaCompra() { return fechaCompra; }
    public double getPrecio() { return precio; }

    // Formato para guardar en archivo
    public String formatearParaArchivo() {
        return idBoleto + ";" + idEvento + ";" + nombreAsistente + ";" + fechaCompra + ";" + precio;
    }

    // Formato para mostrar en terminal
    public String mostrarBoleto() {
        return "\n" +
               "\n" +
               "\n" +
               "ID Boleto:    " + ajustar(idBoleto, 24) + "\n" +
               "ID Evento:    " + ajustar(idEvento, 24) + "\n" +
               "Asistente:    " + ajustar(nombreAsistente, 24) + "\n" +
               "Fecha:        " + ajustar(fechaCompra, 24) + "\n" +
               "Precio:       $" + ajustar(String.format("%.2f", precio), 22) + " \n" +
               "";
    }

    private String ajustar(String texto, int longitud) {
        if (texto.length() > longitud) {
            return texto.substring(0, longitud);
        }
        return String.format("%-" + longitud + "s", texto);
    }

    @Override
    public String toString() {
        return "Boleto [" + idBoleto + "] - Evento: " + idEvento + " - " + nombreAsistente;
    }
}