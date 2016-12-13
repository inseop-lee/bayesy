package io.bayesy.network.observable;

import org.eclipse.recommenders.jayes.BayesNode;

abstract public class Observable {

    protected BayesNode node;
    protected String value;

    Observable(BayesNode node) {
	this.node = node;
    }

    public String getNodeName() {
	return node.getName();
    }

    public BayesNode getNode() {
	return node;
    }

    final public void setValue(String value) {
	this.value = value;
    }

    abstract public String findConfluentState();
}
