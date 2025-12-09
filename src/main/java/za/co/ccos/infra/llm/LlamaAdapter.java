package za.co.ccos.infra.llm;

/**
 * Interface for LLAMA API adapter
 */
public interface LlamaAdapter {
    /**
     * Execute a prompt against the LLAMA model
     * @param prompt the prompt to execute
     * @param options configuration options
     * @return the response from the model
     * @throws LlamaException if the operation fails
     */
    LlamaResponse runPrompt(String prompt, LlamaOptions options) throws LlamaException;
}
