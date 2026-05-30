import java.util.Scanner;

public class CreadorBoletos {
    private Scanner scanner;
    private GestorArchivosBoletos gestor;
    private int contadorBoletos;

    public CreadorBoletos(GestorArchivosBoletos gestor) {
        this.scanner = new Scanner(System.in);
        this.gestor = gestor;
        this.contadorBoletos = contarBoletosExistentes();
    }

    private int contarBoletosExistentes() {
        return 0; // ya no se usa globalmente
    }

    // Cuenta el número más alto de boletos para un evento específico
    private int contarBoletosPorEvento(String idEvento) {
        int maxId = 0;
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader("boletos.txt"));
            String linea;
            String prefijo = idEvento + "-BOL-";
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos[0].startsWith(prefijo)) {
                    try {
                        int num = Integer.parseInt(datos[0].substring(prefijo.length()));
                        if (num > maxId) maxId = num;
                    } catch (NumberFormatException ignored) {}
                }
            }
            reader.close();
        } catch (Exception e) {
            return 0;
        }
        return maxId;
    }

    public void menuCrearBoletos() {
        int opcion;
        do {
            mostrarMenuBoletos();
            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> crearBoletoIndividual();
                case 2 -> crearMultiplesBoletos();
                case 3 -> verBoletosEvento();
                case 4 -> imprimirBoleto();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void mostrarMenuBoletos() {
        System.out.println("\n");
        System.out.println("CREAR BOLETOS");
        System.out.println("");
        System.out.println("  1. Crear boleto individual           ");
        System.out.println("  2. Crear múltiples boletos           ");
        System.out.println("  3. Ver boletos de un evento          ");
        System.out.println("  4. Imprimir boleto                   ");
        System.out.println("  0. Volver                            ");
        System.out.println("");
        System.out.print("Seleccione una opción: ");
    }

    // ✅ Método centralizado — todos los nextLine() pasan por aquí
    private String leerLinea() {
        try {
            return scanner.nextLine().trim();
        } catch (java.util.NoSuchElementException e) {
            throw new EntradaInterrumpidaException();
        }
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(leerLinea());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Obtiene el precio del evento desde el archivo (índice 6 = precio)
    private double obtenerPrecioEvento(String[] datosEvento) throws PrecioInvalidoException {
        double precio = 0;
        try {
            precio = Double.parseDouble(datosEvento[6]);
        } catch (Exception e) {
            throw new PrecioInvalidoException("No se pudo leer el precio del evento.");
        }
        return Evento.validarPrecio(precio);
    }

    // OPCIÓN 1: Crear boleto individual
    public void crearBoletoIndividual() {
        System.out.println("\n--- CREAR BOLETO INDIVIDUAL ---");

        // ✅ CAMBIO 2: toUpperCase en ID del evento
        System.out.print("ID del evento: ");
        String idEvento = leerLinea().toUpperCase();

        String[] datosEvento = gestor.buscarPorId("eventos.txt", idEvento);
        if (datosEvento == null) {
            System.out.println(" ERROR: El evento '" + idEvento + "' no existe.");
            return;
        }

        // ✅ CAMBIO 1: Mostrar boletos disponibles
        int disponibles = Integer.parseInt(datosEvento[4]);
        if (disponibles <= 0) {
            System.out.println(" ERROR: No hay lugares disponibles para este evento.");
            return;
        }

        System.out.println(" Evento encontrado : " + datosEvento[1]);
        System.out.println(" Tipo              : " + datosEvento[2]);
        System.out.println(" Boletos disponibles: " + disponibles + " de " + datosEvento[3]);

        // ✅ CAMBIO 3 & 4: Precio tomado del evento, no del usuario + validación
        double precio;
        try {
            precio = obtenerPrecioEvento(datosEvento);
            System.out.println(" Precio del boleto : $" + String.format("%.2f", precio));
        } catch (PrecioInvalidoException e) {
            System.out.println(" ERROR: " + e.getMessage());
            return;
        }

        // ID del boleto generado automáticamente
        String idBoleto = generarIdBoleto(idEvento);
        System.out.println(" ID del boleto     : " + idBoleto);

        System.out.print("Nombre del asistente: ");
        String nombreAsistente = leerLinea();

        Boleto boleto = new Boleto(idBoleto, idEvento, nombreAsistente, precio);
        gestor.escribirLinea("boletos.txt", boleto.formatearParaArchivo());
        gestor.actualizarDisponibilidad(idEvento, disponibles - 1);

        System.out.println("\n ¡Boleto creado exitosamente!");
        System.out.println(boleto.mostrarBoleto());
    }

    // OPCIÓN 2: Crear múltiples boletos
    public void crearMultiplesBoletos() {
        System.out.println("\n--- CREAR MÚLTIPLES BOLETOS ---");

        // ✅ CAMBIO 2: toUpperCase
        System.out.print("ID del evento: ");
        String idEvento = leerLinea().toUpperCase();

        String[] datosEvento = gestor.buscarPorId("eventos.txt", idEvento);
        if (datosEvento == null) {
            System.out.println(" ERROR: El evento '" + idEvento + "' no existe.");
            return;
        }

        // ✅ CAMBIO 1: mostrar disponibles
        int disponibles = Integer.parseInt(datosEvento[4]);
        System.out.println(" Evento            : " + datosEvento[1]);
        System.out.println(" Boletos disponibles: " + disponibles + " de " + datosEvento[3]);

        // ✅ CAMBIO 3 & 4: precio del evento validado
        double precio;
        try {
            precio = obtenerPrecioEvento(datosEvento);
            System.out.println(" Precio por boleto : $" + String.format("%.2f", precio));
        } catch (PrecioInvalidoException e) {
            System.out.println(" ERROR: " + e.getMessage());
            return;
        }

        System.out.print("¿Cuántos boletos desea crear? ");
        int cantidad;
        try {
            cantidad = Integer.parseInt(leerLinea());
        } catch (NumberFormatException e) {
            System.out.println(" ERROR: Cantidad inválida.");
            return;
        }

        if (cantidad > disponibles) {
            System.out.println(" ERROR: Solo hay " + disponibles + " lugares disponibles.");
            return;
        }

        System.out.println("\nIngrese los nombres de los asistentes:");
        java.util.List<String[]> resumen = new java.util.ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            System.out.print("  Asistente " + (i + 1) + ": ");
            String nombre = leerLinea();

            String idBoleto = generarIdBoleto(idEvento);
            Boleto boleto = new Boleto(idBoleto, idEvento, nombre, precio);
            gestor.escribirLinea("boletos.txt", boleto.formatearParaArchivo());
            resumen.add(new String[]{idBoleto, nombre});
        }

        gestor.actualizarDisponibilidad(idEvento, disponibles - cantidad);

        System.out.println("\n✅ " + cantidad + " boletos creados exitosamente.");
        System.out.println("─".repeat(45));
        System.out.printf("  %-12s  %s%n", "ID Boleto", "Asistente");
        System.out.println("─".repeat(45));
        for (String[] r : resumen) {
            System.out.printf("  %-12s  %s%n", r[0], r[1]);
        }
        System.out.println("─".repeat(45));
        System.out.println("  Precio por boleto: $" + String.format("%.2f", precio));
    }

    // OPCIÓN 3: Boleto rápido con ID automático
    public void crearBoletoRapido() {
        System.out.println("\n--- BOLETO RÁPIDO ---");

        // ✅ CAMBIO 2: toUpperCase
        System.out.print("ID del evento: ");
        String idEvento = leerLinea().toUpperCase();

        String[] datosEvento = gestor.buscarPorId("eventos.txt", idEvento);
        if (datosEvento == null) {
            System.out.println(" ERROR: El evento '" + idEvento + "' no existe.");
            return;
        }

        // ✅ CAMBIO 1: mostrar disponibles
        int disponibles = Integer.parseInt(datosEvento[4]);
        if (disponibles <= 0) {
            System.out.println(" ERROR: Sin disponibilidad para este evento.");
            return;
        }
        System.out.println(" Evento            : " + datosEvento[1]);
        System.out.println(" Boletos disponibles: " + disponibles + " de " + datosEvento[3]);

        // ✅ CAMBIO 3 & 4: precio del evento validado
        double precio;
        try {
            precio = obtenerPrecioEvento(datosEvento);
            System.out.println(" Precio del boleto : $" + String.format("%.2f", precio));
        } catch (PrecioInvalidoException e) {
            System.out.println(" ERROR: " + e.getMessage());
            return;
        }

        System.out.print("Nombre del asistente: ");
        String nombre = leerLinea();

        String idBoleto = generarIdBoleto(idEvento);

        Boleto boleto = new Boleto(idBoleto, idEvento, nombre, precio);
        gestor.escribirLinea("boletos.txt", boleto.formatearParaArchivo());
        gestor.actualizarDisponibilidad(idEvento, disponibles - 1);

        System.out.println("\n✅ ¡Boleto creado!");
        System.out.println(boleto.mostrarBoleto());
    }

    // OPCIÓN 4: Ver boletos de un evento
    public void verBoletosEvento() {
        System.out.println("\n--- BOLETOS POR EVENTO ---");
        // ✅ CAMBIO 2: toUpperCase
        System.out.print("ID del evento: ");
        String idEvento = leerLinea().toUpperCase();

        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader("boletos.txt"));
            String linea;
            int contador = 0;

            System.out.println("\nBoletos encontrados:");
            System.out.println("─".repeat(60));

            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 3 && datos[1].equals(idEvento)) {
                    contador++;
                    System.out.printf("  %d. [%s] %s%n", contador, datos[0], datos[2]);
                }
            }
            reader.close();

            if (contador == 0) {
                System.out.println("  No hay boletos para el evento '" + idEvento + "'.");
            } else {
                System.out.println("─".repeat(60));
                System.out.println("Total: " + contador + " boleto(s)");
            }

        } catch (Exception e) {
            System.out.println("Error al leer boletos: " + e.getMessage());
        }
    }

    // OPCIÓN 5: Imprimir un boleto específico
    public void imprimirBoleto() {
        System.out.println("\n--- IMPRIMIR BOLETO ---");
        // ✅ CAMBIO 2: toUpperCase
        System.out.print("ID del boleto: ");
        String idBoleto = leerLinea().toUpperCase();

        String[] datos = gestor.buscarPorId("boletos.txt", idBoleto);
        if (datos == null) {
            System.out.println(" ERROR: Boleto '" + idBoleto + "' no encontrado.");
            return;
        }

        double precio = datos.length >= 5 ? Double.parseDouble(datos[4]) : 0.0;
        String fecha = datos.length >= 4 ? datos[3] : "N/A";

        Boleto boleto = new Boleto(datos[0], datos[1], datos[2], fecha, precio);
        System.out.println("\n" + boleto.mostrarBoleto());
    }

    private String generarIdBoleto(String idEvento) {
        int siguiente = contarBoletosPorEvento(idEvento) + 1;
        return idEvento + "-BOL-" + String.format("%04d", siguiente);
    }
}
