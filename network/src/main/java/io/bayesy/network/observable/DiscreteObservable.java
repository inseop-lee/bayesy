package io.bayesy.network.observable;

import org.eclipse.recommenders.jayes.BayesNode;

public class DiscreteObservable extends Observable {

    public DiscreteObservable(BayesNode node) {
	super(node);
    }

    @Override
    public String getConfluentState() {
    	return (String) value;
    }
}
