/*
 * Created on Jun 28, 2008
 * @author sunita
 */
package org.melodi.learning.iitb.KernelCRF;

import java.util.Vector;

import org.melodi.learning.iitb.CRF.CrfParams;
import org.melodi.learning.iitb.CRF.Trainer;
import org.melodi.learning.iitb.KernelCRF.KernelCRF.SupportVector;

public abstract class KernelTrainer extends Trainer {

    public KernelTrainer(CrfParams p) {
        super(p);
    }
    public abstract Vector<SupportVector> getSupportVectors();
    public abstract SequenceKernel getKernel();
}
