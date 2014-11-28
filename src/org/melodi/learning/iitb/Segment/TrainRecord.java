package org.melodi.learning.iitb.Segment;
import org.melodi.learning.iitb.CRF.*;
/**
 *
 * @author Sunita Sarawagi
 *
 */ 



public interface TrainRecord extends SegmentDataSequence {
    int numSegments(); // number of segments in the record
    int[] labels(); // labels of each segment
    String[] tokens(int segmentNum); // array of tokens in this segment  
    int numSegments(int label); // number of segments of given label
    String[] tokens(int label, int i); // i-th segment of given label
};

