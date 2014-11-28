/*
 * Created on May 4, 2005
 *
 */
package org.melodi.learning.iitb.BSegment;

import org.melodi.learning.iitb.CRF.DataSequence;

/**
 * @author sunita
 *
 */
public interface BoundaryFeatureFunctions {
   // public void assignBoundary(BFeatureImpl feature, int pos);
    public int maxBoundaryGap();
    public void next(BFeatureImpl feature);
    public boolean startScanFeaturesAt(DataSequence data, int pos);
}
