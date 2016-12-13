/*******************************************************************************
 * Copyright (c) 2013 Michael Kutschke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Michael Kutschke - initial API and implementation
 ******************************************************************************/
package network;

import java.util.Arrays;

import org.eclipse.recommenders.jayes.transformation.util.ArrayFlatten;

import com.google.common.collect.Lists;

public final class ArrayUtils {

    private ArrayUtils() {
        // Not meant to be instantiated
    }

    public static double[] flatten(double[][][] array) {
        return ArrayFlatten.flatten(Lists.transform(Arrays.asList(array), new ArrayFlatten()).toArray(new double[0][]));
    }
}
