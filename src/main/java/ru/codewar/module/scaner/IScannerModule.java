package ru.codewar.module.scaner;

import ru.codewar.module.IPlatformedModule;

/**
 * IScannerModule allows player to get information about huge objects of solar system
 * (star, planets, moons)
 * Main scanner parameter - resolution. Resolution is signature-to-distance ratio, that define
 * a signature of object, that could be detected on specified distance.
 * For example, if scanner can detect object with 1 km signature, that 1 million m away, it means that
 * scanner has resolution = 1 / 1000000 or 10^-6
 *
 * Extends:
 *   - IPlatformedModule because scanner could be installed on ship, drone etc...
 */
public interface IScannerModule extends IPlatformedModule {

    void attachToOperator(ScannerOperator operator);

    /**
     * Start scanning procedure
     * @param transactionId transaction identifier, that will be passed to operator with scanning results
     * @param distance max. distance of scanning (in meters)
     * @param minSignature minimal signature of object
     * @param maxSignature max signature of object
     * @return returns true if scanning begin, otherwise returns false
     */
    boolean scanning(int transactionId, double distance, double minSignature, double maxSignature);
}
