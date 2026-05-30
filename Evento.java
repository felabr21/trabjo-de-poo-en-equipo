public abstract class Evento {
    protected String id;
    protected String nombre;
    protected int capacidad;
    protected int disponibles;
    protected String ubicacion;
    protected double precio;  // única línea nueva

    public Evento(String id, String nombre, int capacidad, String ubicacion, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.disponibles = capacidad;
        this.ubicacion = ubicacion;
        this.precio = precio;
    }
    
    public String getNombre(){
        return nombre;
    }
    public String getId() { 
    return id; }
    public int getDisponibles() { 
    return disponibles; }
    public void reducirDisponible() { 
    this.disponibles--; }
    public void aumentarDisponible() { 
    this.disponibles++; }
    public boolean hayDisponibilidad() { 
    return disponibles > 0; }

    public static double validarPrecio(double precio) throws PrecioInvalidoException {
        if (precio <= 0) {
            throw new PrecioInvalidoException("El precio debe ser mayor a $0.00. Se recibió: $" + precio);
        }
        return precio;
    }

    public abstract double calcularPrecio();
    
    public abstract String getTipo();

    public String formatearParaArchivo() {
        return id + ";" + nombre + ";" + getTipo() + ";" + capacidad + ";" + disponibles + ";" + ubicacion + ";" + precio;
    }
}