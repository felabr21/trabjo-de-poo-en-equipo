import java.io.*;
public class GestorArchivosBoletos {
    // escribir
    public void escribirLinea(String nombreArchivo, String linea) {
        try (FileWriter fw = new FileWriter(nombreArchivo, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(linea);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("ERROR al escribir: " + e.getMessage());
        }
    }
    // leer
    public void leerArchivo(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            System.out.println("El archivo " + nombreArchivo + " no existe aún.");
            return;
        }
        try (FileReader fr = new FileReader(archivo);
             BufferedReader br = new BufferedReader(fr)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("ERROR al leer: " + e.getMessage());
        }
    }
    // id de evento o boleto
    public String[] buscarPorId(String nombreArchivo, String idBuscado) {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            return null;
        }
        try (FileReader fr = new FileReader(archivo);
             BufferedReader br = new BufferedReader(fr)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos[0].equals(idBuscado)) {
                    return datos; 
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR al buscar: " + e.getMessage());
        }
        return null; // No encontré nada
    }
    //cambia la cantidad de boletos que hay en existencia
    public void actualizarDisponibilidad(String idEvento, int nuevaDisponibilidad) {
    File archivoOriginal = new File("eventos.txt");
    File archivoTemporal = new File("eventos_temp.txt");
    try (FileReader fr = new FileReader(archivoOriginal);
         BufferedReader br = new BufferedReader(fr);
         FileWriter fw = new FileWriter(archivoTemporal);
         BufferedWriter bw = new BufferedWriter(fw)) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(";");
            //checa si es o no el evento
            if (datos[0].equals(idEvento)) {
                datos[4] = String.valueOf(nuevaDisponibilidad); // Cambia el dato
                linea = String.join(";", datos);                // Reconstruye la línea
            }     
            bw.write(linea);
            bw.newLine();
        }
    } catch (IOException e) {
        System.out.println("ERROR: " + e.getMessage());
        return;
    }
    // Reemplaza el archivo original con el temporal
    archivoOriginal.delete();
    archivoTemporal.renameTo(archivoOriginal);
}
//al cancelar
public void eliminarBoleto(String idBoleto) {
    File archivoOriginal = new File("boletos.txt");
    File archivoTemporal = new File("boletos_temp.txt");
    try (FileReader fr = new FileReader(archivoOriginal);
         BufferedReader br = new BufferedReader(fr);
         FileWriter fw = new FileWriter(archivoTemporal);
         BufferedWriter bw = new BufferedWriter(fw)) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(";");
            // Si NO es el boleto a eliminar, lo escribo
            if (!datos[0].equals(idBoleto)) {
                bw.write(linea);
                bw.newLine();
            }
        }
    } catch (IOException e) {
        System.out.println("ERROR: " + e.getMessage());
        return;
    }
    archivoOriginal.delete();
    archivoTemporal.renameTo(archivoOriginal);
}
}
