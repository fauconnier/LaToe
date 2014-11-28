package org.melodi.learning.iitb.Segment;

import java.util.ArrayList;

import org.melodi.learning.iitb.CRF.DataSequence;

class DCTrainData implements TrainData {
    ArrayList<DCTrainRecord> trainRecs;
    int pos;
    DCTrainData(ArrayList<DCTrainRecord> trs) {
        trainRecs = trs;
    }
    public int size() {
        return trainRecs.size();
    }
    public void startScan() {
        pos = 0;
    }
    public TrainRecord nextRecord() {
        return (TrainRecord)trainRecs.get(pos++);
    }
    public boolean hasMoreRecords() {
        return (pos < size());
    }
    public boolean hasNext() {
        return hasMoreRecords();
    }
    public DataSequence next() {
        return nextRecord();
    }
};