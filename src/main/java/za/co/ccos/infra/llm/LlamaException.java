package za.co.ccos.infra.llm;

/**
 * Exception thrown when LLAMA operations fail
 */
public class LlamaException extends Exception {
    public LlamaException(String message) {
        super(message);
    }

    public LlamaException(String message, Throwable cause) {
        super(message, cause);
    }
}
