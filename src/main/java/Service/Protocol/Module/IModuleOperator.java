package Service.Protocol.Module;

public interface IModuleOperator {
    void onResponse(int transactionId, String response);
    void onIndication(String indication);
}
