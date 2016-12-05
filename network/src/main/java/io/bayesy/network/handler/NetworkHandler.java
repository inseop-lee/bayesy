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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.bayesy.network.observable.DiscreteObservable;
import io.bayesy.network.observable.Observable;
import io.bayesy.preprocessing.PreprocessingContext;

/**
 * @author inseop.lee
 *
 * TODO : Evidence 별도 클래스로 분리??
 */

public class NetworkHandler {
    private BayesNet net;
    private JunctionTreeAlgorithm inferer;
    private BayesNode targetNode;
    private List<Observable> observables;
    private PreprocessingContext preprocessingContext;
    private Map<BayesNode, Object> evidences;
    
    NetworkHandler(BayesNet net, BayesNode targetNode) {
	this.net = net;
	this.targetNode = targetNode;
	this.observables = Lists.newArrayList();
	inferer = new JunctionTreeAlgorithm();

	inferer.getFactory().setFloatingPointType(float.class);
	inferer.setNetwork(net);
    }

    public HashMap<String, Double> getMarginal() {
	return getMarginal(targetNode);
    }

    public HashMap<String, Double> getMarginalByNodeName(String nodeName) {
	return getMarginal(net.getNode(nodeName));
    }

    public void addObservable(String nodeName) {
	BayesNode node = net.getNode(nodeName);
	observables.add(new DiscreteObservable(node));
    }

    void setPreprocessingContext(PreprocessingContext pCtx) {
	preprocessingContext = pCtx;
    }
    

    private HashMap<String, Double> getMarginal(BayesNode node) {
	propagate();
	
	double[] marginals = inferer.getBeliefs(node);
	HashMap<String, Double> result = new HashMap<String, Double>();

	for (int i = 0; i < node.getOutcomeCount(); i++) {
	    result.put(node.getOutcomeName(i), marginals[i]);
	}

	return result;
    }
    
    private void propagate() {
	if (!Objects.isNull(preprocessingContext)) {
	    addAllEvidences(preprocessingContext.preprocessingData(getEvidences()));
	}
	
	for(Observable observable : observables) {
	    try {
		String obNodeName = observable.getNodeName();
		observable.setValue(evidences.get(obNodeName));
		inferer.addEvidence(observable.getNode(), observable.getConfluentState());
	    } catch (NullPointerException e) {
		//TODO : invalid preprocessing key 
	    }
	}
    }

    public void addAllEvidences(Map<String, Object> map) {
	for(String key : map.keySet()) {
	    addEvidence(key,map.get(key));
	}
    }
    
    public void addEvidence(String key, Object value) {
	try {
	    evidences.put(net.getNode(key), value);
	} catch (NullPointerException e) {
	    //TODO : invalid evidence Key
	}
    }

    private Map<String, Object> getEvidences() {
	Map<String, Object> evidencesMap = Maps.newHashMap();
	for(BayesNode node : evidences.keySet()) {
	    evidencesMap.put(node.getName(), evidences.get(node));
	}
	return evidencesMap;
    }
}
