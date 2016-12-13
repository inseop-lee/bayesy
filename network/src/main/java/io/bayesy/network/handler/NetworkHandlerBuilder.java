/**
 * 
 */
package io.bayesy.network.handler;

import java.util.ArrayList;

import org.eclipse.recommenders.jayes.BayesNet;
import org.eclipse.recommenders.jayes.BayesNode;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import io.bayesy.preprocessing.PreprocessingContext;

/**
 * @author inseop.lee
 *
 */
public class NetworkHandlerBuilder {

    private PreprocessingContext pContext;
    private BayesNet net;
    private BayesNode targetNode;
    private ArrayList<String> observables;

    public NetworkHandlerBuilder() {
	net = new BayesNet();
	observables = Lists.newArrayList();
    }

    public BayesNet getNet() {
	return net;
    }

    public BayesNode getTargetNode() {
	return targetNode;
    }

    public NetworkHandlerBuilder setTargetNode(String nodeName) {
	try {
	    targetNode = net.getNode(nodeName);
	    Preconditions.checkNotNull(targetNode);
	} catch (NullPointerException e) {
	    throw new NullPointerException("Target Node is not founded.");
	}
	return this;
    }

    public NetworkHandlerBuilder setNetwork(BayesNet net) {
	this.net = net;
	return this;
    }
    
    public NetworkHandlerBuilder addObservable(String nodeName) {
	observables.add(nodeName);
	return this;
    }

    public NetworkHandlerBuilder setPreprocessingContext(PreprocessingContext pCtx) {
	this.pContext = pCtx;
	return this;
    }

    public NetworkHandler build() {
	NetworkHandler netHandler = new NetworkHandler(net, targetNode);
	netHandler.setPreprocessingContext(pContext);
	for(String observable : observables) {
	    netHandler.addObservable(observable);
	}

	return netHandler;
    }
}
