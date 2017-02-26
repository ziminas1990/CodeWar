package ru.codewar.protocol.module;


import ru.codewar.networking.Message;

public interface ModuleController {
    /**
     * check if controller is able to parse and execute command or request
     */
    static boolean checkIfSupported(String message)
    {
        return false;
    }

    /**
     * Parse a command and pass it to module
     */
    void onCommand(String command);

    /**
     * Parse request message and pass it to module. If module is able to answer
     * immediately, function will return response as ready-to-send string, otherwise return null
     * If controller receive response later, it will call an "onResponse" function of ModuleOperator
     */
    Message onRequest(Integer transactionId, String request);
}
