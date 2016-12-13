package network;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.recommenders.jayes.BayesNet;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import io.bayesy.network.handler.NetworkHandler;
import io.bayesy.network.handler.NetworkHandlerBuilder;

public class NetworkHandlerTest {

    NetworkHandler netHandler;

    @Before
    public void init() {
	BayesNet net = NetExamples.testNet1();
	netHandler = new NetworkHandlerBuilder()
		.setNetwork(net)
		.setTargetNode("d")
		.addObservable("a")
		.addObservable("b")
		.build();
    }

    @Test
    public void testAddAllEvidences() {
	HashMap<String, String> map = Maps.newHashMap();
	map.put("a", "false");
	map.put("b", "la");
	netHandler.addAllEvidences(map);
	assertEquals(map, netHandler.getEvidences());
    }

    @Test(expected = NullPointerException.class)
    public void testObservableHasNotEvidence() {
	netHandler.computeProbabilities();
    }

    @Test(expected = NullPointerException.class)
    public void testNullEvidence() {
	netHandler.addEvidence("x", "10");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnmatchedState() {
	netHandler.addEvidence("a", "true");
	netHandler.addEvidence("b", "xxx");
	netHandler.computeProbabilities();
    }

    @Test
    public void testComputeProbabilities() {
	netHandler.addEvidence("a", "false");
	netHandler.addEvidence("b", "la");
	Map<String, Double> prob = netHandler.computeProbabilities();
	assertEquals(.26f, prob.get("true").floatValue(), 0.0001);
	assertEquals(.74f, prob.get("false").floatValue(), 0.0001);
    }

    @Test
    public void testComputeProbabilitiesWithoutEvidences() {
	BayesNet net = NetExamples.testNet1();
	NetworkHandler netHandler2 = new NetworkHandlerBuilder()
		.setNetwork(net)
		.setTargetNode("d")
		.build();
	Map<String, Double> prob = netHandler2.computeProbabilities();
	assertEquals(.2804f, prob.get("true").floatValue(), 0.0001);
	assertEquals(.7196f, prob.get("false").floatValue(), 0.0001);
    }

    @Test
    public void testComputeProbabilitiesWithTargetNode() {
	netHandler.addEvidence("a", "false");
	netHandler.addEvidence("b", "la");
	Map<String, Double> prob = netHandler.computeProbabilitiesByNodeName("c");
	assertEquals(.20f, prob.get("true").floatValue(), 0.0001);
	assertEquals(.80f, prob.get("false").floatValue(), 0.0001);
    }
}
