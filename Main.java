import java.io.*;
import java.util.ArrayList;

public class Main {

    static ArrayList<Evento> eventos = new ArrayList<>();
    static GestorArchivosBoletos gestor = new GestorArchivosBoletos();
    static MenuEventos menu = new MenuEventos();
    static CreadorBoletos creadorBoletos = new CreadorBoletos(gestor);

    public static void main(String[] args) {

        cargarEventos();

        int opcion;
        try {
            do {
                opcion = menu.mostrarMenuPrincipal();

                if (opcion == 1) registrarEvento();
                else if (opcion == 2) mostrarEventos();
                else if (opcion == 3) creadorBoletos.menuCrearBoletos();
                else if (opcion == 4) cancelarBoleto();
                else if (opcion == 5) creadorBoletos.verBoletosEvento();
                else if (opcion == 6) guardarEventos();
                else if (opcion == 7) {
                    guardarEventos();
                    System.out.println("Hasta luego!");
                    menu.cerrar();
                }

            } while (opcion != 7);

        } catch (java.util.NoSuchElementException e) {
            throw new EntradaInterrumpidaException();
        } catch (EntradaInterrumpidaException e) {
            System.out.println("\n⚠  " + e.getMessage());
            System.out.println("Guardando datos antes de cerrar...");
            guardarEventos();
            menu.cerrar();
        }
    }

    // Carga eventos del archivo, o precarga ejemplos si no existe
    static void cargarEventos() {
        File archivo = new File("eventos.txt");

        if (!archivo.exists() || archivo.length() == 0) {
            
            eventos.add(new Concierto("E001", "Luis Miguel - Tour 2025", 500, "Auditorio Guelaguetza", "Luis Miguel", 1800.0));
            eventos.add(new Concierto("E002", "Homenaje a Jose Jose", 300, "Teatro Macedonio Alcala", "Orquesta Homenaje", 1200.0));
            eventos.add(new Concierto("E003", "Juan Gabriel Sinfonico", 400, "Foro Sol CDMX", "Banda Sinfonica", 2500.0));
            eventos.add(new Concierto("E004", "Mana - Mexico Lindo Tour", 800, "Estadio Universitario", "Mana", 950.0));
            eventos.add(new Conferencia("E005", "IA en la Educacion", 200, "ITO Auditorio", "Dr. Carlos Herrera", 350.0));
            eventos.add(new Conferencia("E006", "Emprendimiento Tecnologico", 150, "Centro de Convenciones", "Ing. Sofia Ramirez", 500.0));
            eventos.add(new Teatro("E007", "La Casa de Bernarda Alba", 180, "Teatro Macedonio Alcala", "La Casa de Bernarda Alba", 750.0));
            eventos.add(new Teatro("E008", "El Tenorio Comico", 220, "Centro Cultural San Pablo", "El Tenorio Comico", 600.0));

            guardarEventos();
            System.out.println("Eventos de ejemplo cargados.");
        } else {
            // Leer desde archivo
            try {
                BufferedReader br = new BufferedReader(new FileReader(archivo));
                String linea;
                while ((linea = br.readLine()) != null) {
                    Evento e = parsearEvento(linea);
                    if (e != null) eventos.add(e);
                }
                br.close();
                System.out.println(eventos.size() + " eventos cargados.");
            } catch (IOException ex) {
                System.out.println("Error al leer eventos: " + ex.getMessage());
            }
        }
    }

    
    static Evento parsearEvento(String linea) {
        String[] d = linea.split(";");
        if (d.length < 8) return null;

        String id          = d[0];
        String nombre      = d[1];
        String tipo        = d[2];
        int    capacidad   = Integer.parseInt(d[3]);
        int    disponibles = Integer.parseInt(d[4]);
        String ubicacion   = d[5];
        double precio      = Double.parseDouble(d[6]);
        String datoExtra   = d[7];

        Evento e = null;
        if (tipo.equals("Concierto"))   e = new Concierto(id, nombre, capacidad, ubicacion, datoExtra, precio);
        if (tipo.equals("Conferencia")) e = new Conferencia(id, nombre, capacidad, ubicacion, datoExtra, precio);
        if (tipo.equals("Teatro"))      e = new Teatro(id, nombre, capacidad, ubicacion, datoExtra, precio);

        // Restaurar cuántos ya se vendieron
        if (e != null) {
            int vendidos = capacidad - disponibles;
            for (int i = 0; i < vendidos; i++) e.reducirDisponible();
        }

        return e;
    }

    // Muestra la lista de eventos en consola
    static void mostrarEventos() {
        if (eventos.isEmpty()) {
            System.out.println("No hay eventos registrados.");
            return;
        }

        System.out.println("\n--- LISTA DE EVENTOS ---");
        for (Evento e : eventos) {
            System.out.println("ID: " + e.getId()
                + " | " + e.getNombre()
                + " | " + e.getTipo()
                + " | $" + e.calcularPrecio()
                + " | Disponibles: " + e.getDisponibles());
        }
        System.out.println("* 5% de descuento para menores de 3 años y mayores de 60.");
    }

    // Registrar un evento nuevo desde el teclado
    static void registrarEvento() {
        int tipo = menu.seleccionarTipoEvento();
        if (tipo == 0) return;

        String id = menu.pedirIdEvento().toUpperCase();

        // Verificar que el ID no exista
        if (buscarEvento(id) != null) {
            System.out.println("Ya existe un evento con ese ID.");
            return;
        }

        String nombre    = menu.pedirNombreEvento();
        int    capacidad = menu.pedirCapacidad();
        String ubicacion = menu.pedirUbicacion();
        String datoExtra = menu.pedirDatoEspecifico(tipo);
        double precio    = menu.pedirPrecio();

        Evento nuevo = null;
        if (tipo == 1) nuevo = new Concierto(id, nombre, capacidad, ubicacion, datoExtra, precio);
        if (tipo == 2) nuevo = new Conferencia(id, nombre, capacidad, ubicacion, datoExtra, precio);
        if (tipo == 3) nuevo = new Teatro(id, nombre, capacidad, ubicacion, datoExtra, precio);

        if (nuevo != null) {
            eventos.add(nuevo);
            guardarEventos();
            System.out.println("Evento registrado correctamente.");
        }
    }

    // Cancela un boleto y devuelve el lugar al evento
    static void cancelarBoleto() {
        String idBoleto = menu.pedirIdBoleto().toUpperCase();
        String[] datos  = gestor.buscarPorId("boletos.txt", idBoleto);

        if (datos == null) {
            System.out.println("No se encontro el boleto " + idBoleto);
            return;
        }

        // datos[1] es el idEvento
        Evento evento = buscarEvento(datos[1]);
        if (evento != null) {
            evento.aumentarDisponible();
            gestor.actualizarDisponibilidad(datos[1], evento.getDisponibles());
        }

        gestor.eliminarBoleto(idBoleto);
        System.out.println("Boleto cancelado. Lugar liberado.");
    }

    // Sobreescribe eventos.txt con la lista actual
    static void guardarEventos() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("eventos.txt", false));
            for (Evento e : eventos) {
                bw.write(e.formatearParaArchivo());
                bw.newLine();
            }
            bw.close();
            System.out.println("Eventos guardados.");
        } catch (IOException ex) {
            System.out.println("Error al guardar: " + ex.getMessage());
        }
    }

    // Busca un evento en la lista por su ID
    static Evento buscarEvento(String id) {
        for (Evento e : eventos) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }
}