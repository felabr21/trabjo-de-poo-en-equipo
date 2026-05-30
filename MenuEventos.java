import java.util.Scanner;
import java.util.NoSuchElementException;

public class MenuEventos {
    private Scanner sc;

    public MenuEventos() {
        this.sc = new Scanner(System.in);
    }

    // ✅ Método centralizado — todos los nextLine() pasan por aquí
    private String leerLinea() {
        try {
            return sc.nextLine();
        } catch (NoSuchElementException e) {
            throw new EntradaInterrumpidaException();
        }
    }

    public int mostrarMenuPrincipal() {
        System.out.println("\n=== SISTEMA DE GESTIÓN DE EVENTOS Y BOLETOS ===");
        System.out.println("1. Registrar evento");
        System.out.println("2. Mostrar eventos");
        System.out.println("3. Vender boleto");
        System.out.println("4. Cancelar boleto");
        System.out.println("5. Historial de boletos");
        System.out.println("6. Guardar datos");
        System.out.println("7. Salir");
        System.out.print("Seleccione una opción: ");
        try {
            int decision = Integer.parseInt(leerLinea());
            if (decision < 1 || decision > 7) {
                System.out.println("Tipo inválido.");
                return 0;
            }
            return decision;
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Debe ingresar un número.");
            return 0;
        }
    }

    public int seleccionarTipoEvento() {
        System.out.println("\nTipo de evento:");
        System.out.println("1. Concierto");
        System.out.println("2. Conferencia");
        System.out.println("3. Teatro");
        System.out.println("0. Cancelar");
        System.out.print("Seleccione el tipo: ");
        try {
            int tipo = Integer.parseInt(leerLinea());
            if (tipo < 0 || tipo > 3) {
                System.out.println("Tipo inválido.");
                return 0;
            }
            return tipo;
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Debe ingresar un número.");
            return 0;
        }
    }

    public String pedirIdEvento() {
        System.out.print("Ingrese el ID del evento (ej: E001): ");
        return leerLinea();
    }

    public String pedirNombreEvento() {
        System.out.print("Ingrese el nombre del evento: ");
        return leerLinea();
    }

    public String pedirUbicacion() {
        System.out.print("Ingrese la ubicación: ");
        return leerLinea();
    }

    public int pedirCapacidad() {
        while (true) {
            System.out.print("Ingrese la capacidad: ");
            try {
                int capacidad = Integer.parseInt(leerLinea());
                if (capacidad <= 0) {
                    System.out.println("⚠  La capacidad debe ser mayor a 0. Intente de nuevo.");
                } else {
                    return capacidad;
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠  Entrada inválida. Debe ingresar un número entero.");
            }
        }
    }

    public String pedirDatoEspecifico(int tipo) {
        switch (tipo) {
            case 1: System.out.print("Ingrese la banda principal: ");  break;
            case 2: System.out.print("Ingrese el nombre del orador: "); break;
            case 3: System.out.print("Ingrese el nombre de la obra: "); break;
            default: throw new IllegalArgumentException("Tipo de evento no válido: " + tipo);
        }
        return leerLinea();
    }

    public String pedirIdBoleto() {
        System.out.print("Ingrese el ID del boleto (ej: B001): ");
        return leerLinea();
    }

    public String pedirIdEventoBoleto() {
        System.out.print("Ingrese el ID del evento: ");
        return leerLinea();
    }

    public String pedirNombreAsistente() {
        System.out.print("Ingrese el nombre del asistente: ");
        return leerLinea();
    }

    public double pedirPrecio() {
        System.out.print("Ingrese el precio del evento: $");
        try {
            return Double.parseDouble(leerLinea());
        } catch (NumberFormatException e) {
            System.out.println("Precio inválido. Se asignará $0.00");
            return 0.0;
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void cerrar() {
        sc.close();
    }
}
