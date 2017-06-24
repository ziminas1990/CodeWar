package ru.codewar.module.multiplexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.module.*;
import ru.codewar.networking.Channel;
import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleController;
import ru.codewar.protocol.module.ModuleOperator;
import ru.codewar.util.IdPool;

import java.util.*;

public class MultiplexerLogic {

    private Logger logger = LoggerFactory.getLogger(MultiplexerLogic.class);
    private IdPool idPool = new IdPool();
    private Channel channel;
    private Map<String, IBaseModule> modules = new HashMap<>();
    private Set<String> modulesInUse = new HashSet<>();
    private Map<Integer, VirtualChannel> virtualChannels = new HashMap<>();
    private IModulesFactory factory;

    public MultiplexerLogic(IModulesFactory factory)
    {
        this.factory = factory;
    }

    public void attachToChannel(Channel channel) {
        this.channel = channel;
    }

    public void addModule(IBaseModule module) {
        modules.put(module.getModuleAddress(), module);
    }

    public Map<String, IBaseModule> getAllModules() {
        return modules;
    }

    // Create new virtual channel to device with address moduleAddress
    // Return created virtual channel id on success
    public int openVirtualChannel(String moduleAddress) {

        logger.info("Open virtual channel to {} requested", moduleAddress);
        if(modulesInUse.contains(moduleAddress)) {
            logger.warn("Module {} is already in use!", moduleAddress);
            throw new IllegalArgumentException("Module " + moduleAddress + " is already in use");
        }

        IBaseModule module = modules.get(moduleAddress);
        if(module == null) {
            logger.warn("Module {} not found!", moduleAddress);
            throw new IllegalArgumentException("Element " + moduleAddress + " NOT found");
        }


        ModuleTerminal moduleTerminal = factory.makeTerminal(module);
        if(moduleTerminal == null) {
            logger.warn("Can't create terminal for {} module {}", module.getModuleType(), module.getModuleAddress());
            throw new IllegalArgumentException("Failed to create terminal for module " + moduleAddress);
        }

        int virtualChannelId = idPool.getNextId();

        VirtualChannel virtualChannel = new VirtualChannel(Integer.toString(virtualChannelId));
        virtualChannel.attachToChannel(channel);
        virtualChannel.attachToLogic(moduleTerminal);

        // Registering created virtual channel and returning channel id
        virtualChannels.put(virtualChannelId, virtualChannel);
        modulesInUse.add(moduleAddress);
        logger.debug("Virtual channel created with id: {}", virtualChannelId);
        return virtualChannelId;
    }

    public void closeVirtualChannel(int virtualChannelId)  {
        logger.info("Close virtual channel {} requested", virtualChannelId);
        VirtualChannel virtualChannel = virtualChannels.get(virtualChannelId);
        if(virtualChannel == null) {
            logger.warn("Can't close virtual channel {}! No such channel!", virtualChannelId);
            throw new IllegalArgumentException("Virtual channel " + virtualChannelId + " doesn't exist");
        } else {
            String moduleAddress = virtualChannel.getTerminal().getModule().getModuleAddress();
            logger.debug("Virtual channel {} connected to module {}", virtualChannelId, moduleAddress);
            if(!modulesInUse.remove(moduleAddress)) {
                logger.warn("Module {} wasn't marked as used!", moduleAddress);
            }
            virtualChannels.remove(virtualChannelId);
        }
    }

    public void forwardingMessage(int virtualChannelId, Message message) {
        VirtualChannel virtualChannel = virtualChannels.get(virtualChannelId);
        if(virtualChannel != null) {
            virtualChannel.onMessageReceived(message);
        }
    }
}
