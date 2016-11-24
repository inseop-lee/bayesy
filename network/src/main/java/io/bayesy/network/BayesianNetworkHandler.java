package io.bayesy.network;

import org.eclipse.recommenders.jayes.BayesNet;
import org.eclipse.recommenders.jayes.BayesNode;
import org.eclipse.recommenders.jayes.inference.jtree.JunctionTreeAlgorithm;

import io.bayesy.preprocessor.PreprocessedData;


public class BayesianNetworkHandler {
    BayesNet net;
    JunctionTreeAlgorithm alg;
    
    public BayesianNetworkHandler(BayesNet net, boolean normalize) {
	this.net = net;
	alg = new JunctionTreeAlgorithm();
	
	alg.getFactory().setFloatingPointType(float.class);
	alg.setNetwork(net);
    }
    
    public void setEvidence(PreprocessedData pData) {
	
    }
    
}
