package ru.codewar.protocol.module;

public interface ModuleOperator {
    void onResponse(int transactionId, String response);

    void onIndication(String indication);
}
