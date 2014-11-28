/** KernelCRF.java
 * Created on Jun 28, 2008
 * 
 * @author Sunita Sarawagi
 * @since 1.3
 * @version 1.3
 */
package org.melodi.learning.iitb.KernelCRF;

import java.io.Serializable;
import java.util.Properties;
import java.util.Vector;

import org.melodi.learning.iitb.CRF.CRF;
import org.melodi.learning.iitb.CRF.DataIter;
import org.melodi.learning.iitb.CRF.DataSequence;
import org.melodi.learning.iitb.CRF.Evaluator;
import org.melodi.learning.iitb.CRF.FeatureGenerator;
import org.melodi.learning.iitb.CRF.Viterbi;

public class KernelCRF extends CRF {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5041774715605661933L;
	public static class SupportVector implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = -5611055066177234585L;
		DataSequence dataSeq;
        YSequence yseq;
        double alpha;
        public SupportVector(DataSequence dataSeq, YSequence yseq, double alpha) {
            this.dataSeq = dataSeq;
            this.yseq = yseq;
            this.alpha = alpha;
        }
    }
    Vector<SupportVector> svecs;
    SequenceKernel kernel;
    public KernelCRF(int numLabels, FeatureGenerator fgen, Properties configOptions) {
        super(numLabels, fgen, configOptions);
    }
    public KernelCRF(int numLabels, FeatureGenerator fgen, String arg) {
        super(numLabels, fgen, arg);
    }
    @Override
    public double[] train(DataIter trainData, Evaluator evaluator, float instanceWts[]) {
        return train(trainData,evaluator,instanceWts,null);
    }
    @Override
    public double[] train(DataIter trainData, Evaluator evaluator, float instanceWts[], float misClassCosts[][]) {
        KernelTrainer trainer = (KernelTrainer) getTrainer();
        trainer.train(this, trainData, null, evaluator, instanceWts, misClassCosts);
        svecs = trainer.getSupportVectors();
        kernel=trainer.getKernel();
        return new double[getFeatureGenerator().numFeatures()]; // this is just to not break code downstream.
    }
    @Override
    public Viterbi getViterbi(int beamsize) {
        return new KernelViterbi(this,beamsize);
    }
}
