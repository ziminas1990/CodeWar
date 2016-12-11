package ru.codewar.protocol.module;


public interface ModuleController {
    /**
     * check if controller is able to parse and execute command or request
     */
    boolean checkIfSupported(String message);

    /**
     * Parse a command and pass it to module
     */
    void onCommand(String command);

    /**
     * Parse request message and pass it to module. If module is able to answer
     * immediatelly, function will return response as ready-to-send string, otherwise return null
     * If controller receive response later, it will call an "onResponse" function of ModuleOperator
     */
    String onRequest(Integer transactionId, String request);
}
