package io.bayesy.network.observable;

import org.eclipse.recommenders.jayes.BayesNode;

/**
 * @author inseop.lee
 *
 *         TODO : Evidence 별도 클래스로 분리??
 */

public class DiscreteObservable extends Observable {

    public DiscreteObservable(BayesNode node) {
	super(node);
    }

    @Override
    public String findConfluentState() {
	return value.toString();
    }
}
