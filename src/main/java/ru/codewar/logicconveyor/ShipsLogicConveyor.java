package ru.codewar.logicconveyor;

import ru.codewar.logicconveyor.concept.ConveyorLogic;
import ru.codewar.module.ship.BaseShip;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ShipsLogicConveyor implements ConveyorLogic {

    private ArrayList<BaseShip> ships = new ArrayList<>();
    private AtomicInteger nextShipId = new AtomicInteger(0);

    public void addShip(BaseShip ship) { ships.add(ship); }
    public void removeShip(BaseShip ship) { ships.remove(ship); }

    public int stagesCount() { return 1; }
    public boolean prepareStage(int stageId) {
        nextShipId.set(0);
        return true;
    }
    public void proceedStage(int stageId, int threadId, int totalThreads) {
        for(int shipId = nextShipId.getAndIncrement(); shipId < ships.size(); shipId = nextShipId.getAndIncrement())
            ships.get(shipId).proceed();
    }

}
