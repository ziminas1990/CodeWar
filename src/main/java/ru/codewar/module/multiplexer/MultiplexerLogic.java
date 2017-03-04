package ru.codewar.module.multiplexer;

import ru.codewar.networking.Channel;
import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleOperator;
import ru.codewar.util.IdPool;

import java.util.*;

public class MultiplexerLogic {

    private IdPool idPool = new IdPool();
    private Channel channel;
    private Map<String, ModuleOperator> modules = new HashMap<>();
    private Set<String> modulesInUse = new HashSet<>();
    private Map<Integer, VirtualChannel> virtualChannels = new HashMap<>();

    public void attachToChannel(Channel channel) {
        this.channel = channel;
    }

    public void addModule(ModuleOperator module) {
        modules.put(module.getAddress(), module);
    }

    public Map<String, ModuleOperator> getAllModules() {
        return modules;
    }

    // Create new virtual channel to device with address moduleAddress
    // Return created virtual channel id on success
    public int openVirtualChannel(String moduleAddress) {

        if(modulesInUse.contains(moduleAddress)) {
            throw new IllegalArgumentException("Module " + moduleAddress + " is already in use");
        }

        ModuleOperator operator = modules.get(moduleAddress);
        if(operator == null) {
            throw new IllegalArgumentException("Element " + moduleAddress + " NOT found");
        }

        int virtualChannelId = idPool.getNextId();
        VirtualChannel virtualChannel = new VirtualChannel(Integer.toString(virtualChannelId), moduleAddress);
        virtualChannel.attachToChannel(channel);
        virtualChannel.attachToLogic(operator);
        operator.attachToChannel(virtualChannel);

        // Registering created virtual channel and returning channel id
        virtualChannels.put(virtualChannelId, virtualChannel);
        modulesInUse.add(moduleAddress);
        return virtualChannelId;
    }

    public void closeVirtualChannel(int virtualChannelId)  {
        VirtualChannel virtualChannel = virtualChannels.get(virtualChannelId);
        if(virtualChannel == null) {
            throw new IllegalArgumentException("Virtual channel " + virtualChannelId + " doesn't exist");
        }
        modulesInUse.remove(virtualChannel.getEndPointAddress());
        virtualChannels.remove(virtualChannelId);
    }

    public void forwardingMessage(int virtualChannelId, Message message) {
        VirtualChannel virtualChannel = virtualChannels.get(virtualChannelId);
        if(virtualChannel != null) {
            virtualChannel.onMessageReceived(message);
        }
    }
}
