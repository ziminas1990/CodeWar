package ru.codewar.module.scaner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.module.BaseModuleController;
import ru.codewar.networking.Message;
import ru.codewar.util.ArgumentsReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScannerController extends BaseModuleController {

    private Logger logger = LoggerFactory.getLogger(ScannerController.class);
    private String prefix;

    private final static Pattern checkPattern = Pattern.compile("(scan\\s+.+)");
    private final static Pattern scanningPattern = Pattern.compile("scan\\s+(?<ARGS>.+)");

    IScannerModule scanner;
    ScannerOperator operator;

    public static boolean checkIfSupported(String message)
    {
        return BaseModuleController.checkIfSupported(message) ||
                checkPattern.matcher(message).matches();
    }

    public void attachToOperator(ScannerOperator operator)
    {
        this.operator = operator;
    }

    public void attachToModule(IScannerModule scanner) {
        super.attachToModule(scanner);
        this.scanner = scanner;
        prefix = "for " + scanner.getModuleAddress() + " ";
    }

    @Override // ModuleController
    public void onCommand(String command) {
        if(BaseModuleController.checkIfSupported(command)) {
            super.onCommand(command);
            return;
        }
        if(scanner == null) {
            operator.onCommandFailed("controller NOT attached to module");
            return;
        }
    }

    @Override // ModuleController
    public Message onRequest(Integer transactionId, String request) {
        if(BaseModuleController.checkIfSupported(request)) {
            return super.onRequest(transactionId, request);
        }
        if(scanner == null) {
            operator.onRequestFailed(transactionId, "controller NOT attached to module");
            return null;
        }
        Matcher matcher = scanningPattern.matcher(request);
        if(matcher.matches()) {
            scanningRequested(transactionId, new ArgumentsReader(matcher.group("ARGS")));
        }
        return null;
    }


    private void scanningRequested(int transactionId, ArgumentsReader args) {
        Double distance = args.readDouble();
        if(distance == null) {
            operator.onRequestFailed(transactionId, "invalid distance");
            return;
        }
        Double minSignature = args.readDouble();
        if(minSignature == null) {
            operator.onRequestFailed(transactionId, "invalid minSignature");
            return;
        }
        Double maxSignature = args.readDouble();
        if(maxSignature == null) {
            operator.onRequestFailed(transactionId, "invalid mxnSignature");
            return;
        }
        if(!scanner.scanning(transactionId, distance, minSignature, maxSignature)) {
            operator.onRequestFailed(transactionId, "scanning failed to start");
        }
    }
}

