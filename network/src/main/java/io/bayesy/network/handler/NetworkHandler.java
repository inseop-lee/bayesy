/**
 * 
 */

package io.bayesy.network.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.recommenders.jayes.BayesNet;
import org.eclipse.recommenders.jayes.BayesNode;
import org.eclipse.recommenders.jayes.inference.jtree.JunctionTreeAlgorithm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.bayesy.network.observable.DiscreteObservable;
import io.bayesy.network.observable.Observable;
import io.bayesy.preprocessing.PreprocessingContext;

/**
 * @author inseop.lee
 *
 *         TODO : Evidence 별도 클래스로 분리??
 */

public class NetworkHandler {
    private BayesNet net;
    private JunctionTreeAlgorithm inferer;
    private BayesNode targetNode;
    private List<Observable> observables;
    private PreprocessingContext preprocessingContext;
    private Map<BayesNode, String> evidences;

    NetworkHandler(BayesNet net, BayesNode targetNode) {
	this.net = net;
	this.targetNode = targetNode;
	this.observables = Lists.newArrayList();
	evidences = Maps.newHashMap();
	inferer = new JunctionTreeAlgorithm();

	inferer.getFactory().setFloatingPointType(float.class);
	inferer.setNetwork(net);
    }

    void setPreprocessingContext(PreprocessingContext pCtx) {
	preprocessingContext = pCtx;
    }

    public HashMap<String, Double> computeProbabilities() {
	return computeProbabilities(targetNode);
    }

    public HashMap<String, Double> computeProbabilitiesByNodeName(String nodeName) {
	return computeProbabilities(net.getNode(nodeName));
    }

    void addObservable(String nodeName) {
	BayesNode node = net.getNode(nodeName);
	try {
	    observables.add(new DiscreteObservable(Preconditions.checkNotNull(node)));
	} catch (NullPointerException e) {
	    throw new NullPointerException(String.format("There is no Observable in the Network. : %s", nodeName));
	}
    }

    private HashMap<String, Double> computeProbabilities(BayesNode node) {
	propagate();

	double[] marginals = inferer.getBeliefs(node);
	HashMap<String, Double> result = new HashMap<String, Double>();

	for (int i = 0; i < node.getOutcomeCount(); i++) {
	    result.put(node.getOutcomeName(i), marginals[i]);
	}

	return result;
    }

    private void propagate() {

	addAllEvidences(getEvidences());

	if (!Objects.isNull(preprocessingContext)) {
	    addAllEvidences(preprocessingContext.preprocessingData(getEvidences()));
	}

	for (Observable observable : observables) {
	    try {
		observable.setValue(Preconditions.checkNotNull(evidences.get(observable.getNode())));
		inferer.addEvidence(observable.getNode(), observable.findConfluentState());
	    } catch (NullPointerException e) {
		throw new NullPointerException(
			String.format("Some observable have not any evidence. : %s", observable.getNodeName()));
	    } catch (IllegalArgumentException e) {
		throw new IllegalArgumentException(String.format("The evidence is not matched with the node. : %s-%s",
			observable.getNodeName(), observable.findConfluentState()));
	    }
	}
    }

    public void addAllEvidences(Map<String, String> map) {
	for (String key : map.keySet()) {
	    addEvidence(key, map.get(key));
	}
    }

    public void addEvidence(String nodeName, String value) {
	try {
	    BayesNode node = net.getNode(nodeName);
	    Preconditions.checkNotNull(node);
	    evidences.put(node, value);
	} catch (NullPointerException e) {
	    throw new NullPointerException(String.format("The node is not in the Network. : %s", nodeName));
	}
    }

    public Map<String, String> getEvidences() {
	Map<String, String> evidencesMap = Maps.newHashMap();
	for (BayesNode node : evidences.keySet()) {
	    evidencesMap.put(node.getName(), evidences.get(node));
	}
	return evidencesMap;
    }
}
