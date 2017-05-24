package ru.codewar.module.scaner;

import org.junit.Test;
import static org.junit.Assert.*;

import org.w3c.dom.Node;
import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;
import ru.codewar.module.SimpleModulesPlatform;
import ru.codewar.util.ArgumentsReader;
import ru.codewar.world.CelestialBody;
import ru.codewar.world.ISolarSystem;
import ru.codewar.world.SolarSystem;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.util.*;
import java.util.function.Predicate;

public class BaseScannerTests {

    @Test
    public void scanningPlanetsAndSol() {
        RunTestCases("ScanningPlanetsAndSolCases.svg");
    }

    @Test
    public void scanningAsteroidsCases() { RunTestCases("ScanningAsteroidsCases.svg"); }

    @Test
    public void scanningResolutionCases() { RunTestCases("ScanningResolutionCases.svg"); }

    public void RunTestCases(String testCaseSvgFile) {
        Element root = readXmlDocument(testCaseSvgFile);
        assertNotEquals(null, root);

        NodesByTypeMap allNodes = NodesByTypeMap.make(root, BaseScannerTests::isEllipse);
        SolarSystem system = buildSolarSystem(allNodes);

        ScannersFactory scannerFactory = new ScannersFactory(allNodes.get("SCANNER"), system);

        ArrayList<TestCase> testCases = loadTestCases(root, scannerFactory);

        for(int testNumber = 0; testNumber < testCases.size(); testNumber++) {
            String testName = "Test #" + testNumber;
            TestCase testCase = testCases.get(testNumber);
            MockedScannerOperator mockedOperator = new MockedScannerOperator();

            testCase.scanner.attachToOperator(mockedOperator);
            testCase.scanner.scanning(testCase.scanningRadius, testCase.minSignature, testCase.maxSignature);
            testCase.scanner.alive(testCase.alive);

            assertEquals(testName, testCase.coveredBodiesName.size(), mockedOperator.scannedBodies.size());
            for (String expectedBodyName : testCase.coveredBodiesName)
                assertTrue(
                        testName + ": body " + expectedBodyName,
                        mockedOperator.hasBodyWithName(expectedBodyName));
        }
    }

    private Element readXmlDocument(String document) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = dbFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.parse(getClass().getResourceAsStream(document));
            Element root = xmlDocument.getDocumentElement();
            root.normalize();
            return root;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    static ArrayList<Node> asArray(NodeList nodesList) {
        ArrayList<Node> nodesArray = new ArrayList<>();
        for(int i = 0; i < nodesList.getLength(); i++)
            nodesArray.add(nodesList.item(i));
        return nodesArray;
    }

    private static class NodesByTypeMap extends HashMap<String, NodeByNameMap>
    {
        private static NodesByTypeMap make(Element root, Predicate<Node> predicate) {
            NodesByTypeMap map = new NodesByTypeMap();
            BaseScannerTests.asArray(root.getChildNodes()).stream()
                    .filter(predicate)
                    .forEach(map::addNode);
            return map;
        }

        private NodesByTypeMap() {}

        boolean addNode(Node node) {
            String id = node.getAttributes().getNamedItem("id").getNodeValue();
            if(id.isEmpty())
                return false;
            String type = id.substring(0, id.indexOf(":"));
            String name = id.substring(id.indexOf(":") + 1);
            if(type.isEmpty() || name.isEmpty())
                return false;
            getOrCreateNodeByNameMap(type).put(name, node);
            return true;
        }

        Node getNode(String type, String name) {
            NodeByNameMap nodesByName = get(type);
            if(type == null)
                return null;
            return nodesByName.get(name);
        }

        private NodeByNameMap getOrCreateNodeByNameMap(String type) {
            NodeByNameMap value = get(type);
            if(value == null) {
                value = new NodeByNameMap();
                put(type, value);
            }
            return value;
        }
    }

    private static class NodeByNameMap extends HashMap<String, Node>
    {}

    private static boolean isEllipse(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("ellipse");
    }

    private static SolarSystem buildSolarSystem(NodesByTypeMap nodes) {
        SolarSystem system = new SolarSystem(null);
        for(CelestialBody.BodyType type : CelestialBody.BodyType.values()) {
            NodeByNameMap namedNodesMap = nodes.get(type.name());
            if(namedNodesMap == null)
                continue;
            for(Map.Entry<String, Node> namedNode : namedNodesMap.entrySet())
                system.addCelestialBody(buildCelestialBody(type, namedNode.getKey(), namedNode.getValue()));
        }
        return system;
    }

    private static CelestialBody buildCelestialBody(CelestialBody.BodyType type, String name, Node node) {
        double signature = Double.valueOf(node.getAttributes().getNamedItem("rx").getNodeValue());
        double cx = Double.valueOf(node.getAttributes().getNamedItem("cx").getNodeValue());
        double cy = Double.valueOf(node.getAttributes().getNamedItem("cy").getNodeValue());
        return new CelestialBody(type, name, 0, signature, new Point(cx, cy), new Vector());
    }

    private static class ScannersFactory {
        NodeByNameMap scannersNodes;
        ISolarSystem system;

        ScannersFactory(NodeByNameMap scannersNodes, ISolarSystem system) {
            this.scannersNodes = scannersNodes;
            this.system = system;
        }

        BaseScanner make(String scannerName) {
            if(scannersNodes == null || !scannersNodes.containsKey(scannerName))
                return null;
            NamedNodeMap attributes = scannersNodes.get(scannerName).getAttributes();
            SimpleModulesPlatform platform = new SimpleModulesPlatform();
            platform.getPosition().setPosition(
                    Double.valueOf(attributes.getNamedItem("cx").getNodeValue()),
                    Double.valueOf(attributes.getNamedItem("cy").getNodeValue())
            );
            BaseScanner scanner = new BaseScanner(platform, "scanner", system);
            scanner.setSqrResolution(Double.valueOf(attributes.getNamedItem("resolution").getNodeValue()));
            return scanner;
        }

    }

    ArrayList<TestCase> loadTestCases(Element root, ScannersFactory scannerFactory) {
        ArrayList<Node> testCaseNodes = new ArrayList<>();
        asArray(root.getChildNodes()).stream()
                .filter(node -> node.getNodeName().equals("TestCase"))
                .forEach(testCaseNodes::add);

        ArrayList<TestCase> testCases = new ArrayList<>();
        for(Node testCaseNode : testCaseNodes) {
            testCases.add(TestCase.Read(testCaseNode.getAttributes(), scannerFactory));
        }
        return testCases;
    }

    private static class TestCase {
        private TestCase() {}

        static TestCase Read(NamedNodeMap attrs, ScannersFactory scannerFactory) {
            TestCase testCase = new TestCase();

            String scannerName = attrs.getNamedItem("scannerId").getNodeValue();
            testCase.scanner = scannerFactory.make(scannerName);

            String scannedBodiesArray = attrs.getNamedItem("scanned").getNodeValue();
            testCase.coveredBodiesName = new ArgumentsReader(scannedBodiesArray).readArray();

            testCase.scanningRadius = Double.valueOf(attrs.getNamedItem("radius").getNodeValue());
            testCase.minSignature = Double.valueOf(attrs.getNamedItem("minSignature").getNodeValue());
            testCase.maxSignature = Double.valueOf(attrs.getNamedItem("maxSignature").getNodeValue());
            testCase.alive = Double.valueOf(attrs.getNamedItem("alive").getNodeValue());
            return testCase;
        }

        BaseScanner scanner;
        ArrayList<String> coveredBodiesName;
        double scanningRadius;
        double minSignature;
        double maxSignature;
        double alive;
    }

}

