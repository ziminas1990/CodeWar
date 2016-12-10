package ServiceImpl.Protocol.Module;

import Service.Networking.IChannel;
import Service.Networking.INetworkTerminal;
import Service.Protocol.Module.IModuleController;
import Service.Protocol.Module.IModuleOperator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModuleOperator implements IModuleOperator, INetworkTerminal {

    private IModuleController module;
    private IChannel channel;

    private Pattern requestPattern = Pattern.compile("REQ\\s+(\\w+)\\s+(.*)");
    private Pattern commandPattern = Pattern.compile("CMD\\s(.*)");

    public void attachToModuleController(IModuleController module)
    {
        this.module = module;
    }

    public void attachToChannel(Service.Networking.IChannel channel)
    {
        this.channel = channel;
    }

    public void onMessageReceived(String message)
    {
        // Received a message, we should read it header and call an
        // appropriate function of the module
        Matcher result = requestPattern.matcher(message);
        if(result.matches()) {
            Integer transactionId = Integer.valueOf(result.group(1));
            String response = module.onRequest(transactionId, result.group(2));
            if(response != null) {
                // Got response immediately - sending it to client
                onResponse(transactionId, response);
            }
            return;
        }
        result = commandPattern.matcher(message);
        if(result.matches()) {
            module.onCommand(result.group(1));
        }
    }

    public void onResponse(int transactionId, String response)
    {
        // Adding header to response and send it via channel
        channel.sendMessage("RESP " + transactionId + " " + response);
    }

    public void onIndication(String indication)
    {
        // Adding header to response and send it via channel
        channel.sendMessage("IND " + indication);
    }

}
