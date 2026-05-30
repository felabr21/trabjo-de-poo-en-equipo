public class EntradaInterrumpidaException extends RuntimeException {
    public EntradaInterrumpidaException() {
        super("Entrada interrumpida por el usuario (Ctrl+Z / Ctrl+D).");
    }
}
