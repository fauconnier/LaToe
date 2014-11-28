package org.melodi.learning.iitb.CRF;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * CRF (conditional random fields) This class provides support for
 * training and applying a conditional random field for sequence
 * labeling problems.   
 *
 * @author Sunita Sarawagi
 *
 */ 


public class CRF implements java.io.Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 14L;
    double lambda[];
    protected int numY;
    transient Trainer trainer;
    FeatureGenerator featureGenerator;
    EdgeGenerator edgeGen;
    HistoryManager histMgr;
    public CrfParams params;
    transient Viterbi viterbi;
    
    /**
     * @param numLabels is the number of distinct class labels or y-labels
     * @param fgen is the class that is responsible for providing 
     * the features for a particular position on the sequence.
     * @param arg is a string that can be used to control various 
     * parameters of the CRF, these are space separated name-value pairs 
     * described in 
     * @see org.melodi.learning.iitb.CRF.CrfParams 
     */
    public CRF(int numLabels, FeatureGenerator fgen, String arg) {
        this(numLabels, fgen, CrfParams.stringToOptions(arg));
    }
    
    public CRF(int numLabels, FeatureGenerator fgen, java.util.Properties configOptions) {
        this(numLabels,1,fgen,configOptions);
    }
    
    public CRF(int numLabels, int histsize, FeatureGenerator fgen, java.util.Properties configOptions) {
        histMgr = new HistoryManager(histsize,numLabels);
        featureGenerator = histMgr.getFeatureGen(fgen);
        numY = histMgr.numY;
        params = new CrfParams(configOptions);
        edgeGen = histMgr.getEdgeGenerator();
        viterbi = getViterbi(1);
    }
    public FeatureGenerator getFeatureGenerator() {return featureGenerator;}
    /*
     * useful for resetting Viterbi options after loading a saved model.
     */
    public void reinitOptions(java.util.Properties configOptions) {
	params = new CrfParams(configOptions);
	viterbi = null;
    }
    /**
     * write the trained parameters of the CRF to the file
     */
    public void write(String fileName)  throws IOException {
        PrintWriter out=new PrintWriter(new FileOutputStream(fileName));
        out.println("******* Features/Weights (rounded) ************");
        out.println(lambda.length);
        
		HashMap<String, Double> nameFeatures_lambda= new HashMap<String, Double>();
		ArrayList<String> nameFeatures = new ArrayList<String>();
		
        for (int i = 0; i < lambda.length; i++){
        	nameFeatures.add(featureGenerator.featureName(i)+"  ");
        	nameFeatures_lambda.put(featureGenerator.featureName(i)+"  ",Math.round( lambda[i] * 1000.0 ) / 1000.0 );
//        	double newvalue = Math.round( lambda[i] * 100.0 ) / 100.0;
//        	out.println(newvalue);
        }
        
		Collections.sort(nameFeatures);
		for(String nameF : nameFeatures){
			out.print(nameF);
			out.print("  ");
			out.println(nameFeatures_lambda.get(nameF)+"");
			
		}
        
        out.close();
    }
    /**
     * read the parameters of the CRF from a file
     */
    public void read(String fileName) throws IOException {
        BufferedReader in=new BufferedReader(new FileReader(fileName));
        int numF = Integer.parseInt(in.readLine());
        lambda = new double[numF];
        int pos = 0;
        String line;
        while((line=in.readLine())!=null) {
            lambda[pos++] = Double.parseDouble(line);
        }
    }
    protected Trainer dynamicallyLoadedTrainer() {
        if (params.trainerType.startsWith("load=")) {
            try {
                Class c =  Class.forName(params.trainerType.substring(5));
                Constructor constr = c.getConstructor( new Class[] { CrfParams.class } );
                return (Trainer)constr.newInstance( new Object[] { params } );
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
    protected Trainer getTrainer() {
        Trainer thisTrainer = dynamicallyLoadedTrainer();
        if (thisTrainer != null)
            return thisTrainer;
        if (params.trainerType.startsWith("Collins"))
            return new CollinsTrainer(params);
        if (params.trainerType.startsWith("Piecewise"))
            return new PiecewiseTrainer(params);
        
        return new Trainer(params);
    }
    public Viterbi getViterbi(int beamsize) {
        return params.miscOptions.getProperty("segmentViterbi", "false").equals("true")?
                new SegmentViterbi(this,beamsize):new Viterbi(this, beamsize);
    }
    /**
     * Trains the model given the data
     * @return the learnt parameter value as an array
     */
    public double[] train(DataIter trainData) {
        return train(trainData,null,null);
    }
    public void setInitTrainWeights(double initLambda[]) {
        lambda = new double[featureGenerator.numFeatures()];
        params.miscOptions.setProperty("initValuesUseExisting", "true");
        for (int i = 0; i < initLambda.length; lambda[i] = initLambda[i], i++);
    }
    /**
     * Trains the model given the data
     * @return the learnt parameter value as an array
     */
    public double[] train(DataIter trainData, Evaluator evaluator) {
        return train(trainData,evaluator,null);
    }
    /**
     * Trains the model given the data with weighted instances.
     * @return the learnt parameter value as an array
     */
    public double[] train(DataIter trainData, Evaluator evaluator, float instanceWts[]) {
        if (lambda == null) lambda = new double[featureGenerator.numFeatures()];	
        trainer = getTrainer();
        trainer.train(this, histMgr.mapTrainData(trainData), lambda, evaluator, instanceWts);
        return lambda;
    }
    /** 
     * Same as above except that there is a misclassification cost associated with label pairs.
     */
    public double[] train(DataIter trainData, Evaluator evaluator, float instanceWts[], float misClassCosts[][]) {
        if (lambda == null) lambda = new double[featureGenerator.numFeatures()];    
        trainer = getTrainer();
        trainer.train(this, histMgr.mapTrainData(trainData), lambda, evaluator, instanceWts, misClassCosts);
        return lambda;
    }
    public double[] learntWeights() {
        return lambda;
    }
    public double apply(DataSequence dataSeq) {
//    	System.out.println("DEBUG:appli");
        if (viterbi==null)
            viterbi = getViterbi(1);
        if (params.debugLvl > 1) 
            Util.printDbg("CRF: Applying on " + dataSeq);
        double score = viterbi.bestLabelSequence(dataSeq,lambda);
        if (histMgr != null) {
            for(int i = dataSeq.length()-1; i >= 0; i--) {
                histMgr.set_y(dataSeq, i, dataSeq.y(i));
            }
        }
        return score;
    }
    public double applyAndScore(DataSequence dataSeq) {
        double score = apply(dataSeq);
        double lZx = getLogZx(dataSeq);
        return Math.exp(score-lZx);
    }
    public LabelSequence[] topKLabelSequences(DataSequence dataSeq, int numLabelSeqs, boolean getScores) {
	     if ((viterbi==null) || (viterbi.beamsize < numLabelSeqs))
		        viterbi = getViterbi(numLabelSeqs);
	     return viterbi.topKLabelSequences(dataSeq,lambda,numLabelSeqs,getScores);
	}
    public double score(DataSequence dataSeq) {
	    if (viterbi==null)
	       viterbi = getViterbi(1);
		return viterbi.viterbiSearch(dataSeq,lambda,true);
	}
    public void expectedFeatureValues(DataIter data, double expFVals[], FeatureGenerator fgen) {
        if (trainer==null) {
            trainer = getTrainer();
            trainer.init(this,data,lambda);
        }
        trainer.computeFeatureExpectedValue(data,fgen,lambda,expFVals);
    }
    public double getLogZx(DataSequence dataSequence) {
        if (trainer==null) {
            trainer = getTrainer();
            trainer.init(this,null,lambda);
        } else {
            trainer.reInit();
        }
        return -1*trainer.sumProduct(dataSequence,featureGenerator,lambda,null,null,false, -1, null);
    }

    public void score(DataSequence dataSeq, double[] featureVec) {
        if (trainer==null) {
            trainer = getTrainer();
            trainer.init(this,null,lambda);
        }
        trainer.addFeatureVector(dataSeq,featureVec); 
    }
    public int getNumY() {return numY;}
};
