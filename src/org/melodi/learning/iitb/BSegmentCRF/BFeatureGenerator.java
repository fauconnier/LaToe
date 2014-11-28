/** BFeatureGenerator.java
 * Created on Apr 2, 2005
 * 
 * @author Sunita Sarawagi
 * @since 1.2
 * @version 1.3
 *
 */
package org.melodi.learning.iitb.BSegmentCRF;

import org.melodi.learning.iitb.CRF.DataSequence;
import org.melodi.learning.iitb.CRF.FeatureGeneratorNested;

public interface BFeatureGenerator extends FeatureGeneratorNested {
    /**
     * @return: the maximum gap between start and end boundary of features
     */
    int maxBoundaryGap();
    void startScanFeaturesAt(DataSequence data);
    BFeature nextFeature();
};
