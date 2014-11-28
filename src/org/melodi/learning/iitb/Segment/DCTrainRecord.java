package org.melodi.learning.iitb.Segment;

import org.melodi.learning.iitb.CRF.Segmentation;

class DCTrainRecord implements TrainRecord, Segmentation {

	private static final long serialVersionUID = -3412644222368304767L;
	int[] ls;
    String[][] _tokens;

    int[] labelsPerToken;
    int[] snum, spos;

    DCTrainRecord(int[] ts, String[][] toks) {
        ls = ts;
        _tokens = toks;
        spos = new int[ls.length];
        int len = 0;
        for (int i = 0; i < numSegments(); i++) {
            spos[i] = len;
            len+= _tokens[i].length;
        }
        labelsPerToken = new int[len];
        snum = new int[len];
        int pos = 0;
        for (int i = 0; i < ls.length; i++) {
            for (int p = 0; p < _tokens[i].length; p++) {
                snum[pos] = i;
                labelsPerToken[pos++] = ls[i];
            }
        }
    }
    public int[] labels() {
        return ls;
    }
    public void set_y(int i, int l) {labelsPerToken[i] = l;} // not applicable for training data.
    public int length() {return labelsPerToken.length;}
    public Object x(int i) {return _tokens[snum[i]][i - spos[snum[i]]];}
    public int y(int i) {return  labelsPerToken[i];}

    public int numSegments() {
        return ls.length;
    }
    public int numSegments(int l) {
        int sz = 0;
        for (int i = 0; i < ls.length; i++)
            if (ls[i] == l) sz++;
        return sz;
    }
    public String[] tokens(int snum) {
        return _tokens[snum];
    }
    public String[] tokens(int l, int p) {
        int pos = 0;
        for (int i = 0; i < ls.length; i++)
            if (ls[i] == l) {
                if (pos == p)
                    return _tokens[i];
                pos++;
            }
        return null;
    }
    /* (non-Javadoc)
     * @see iitb.CRF.SegmentDataSequence#getSegmentEnd(int)
     */
    public int getSegmentEnd(int segmentStart) {
        for (int i = segmentStart+1; i < length(); i++) {
            if (y(i)!= y(segmentStart))
                return i-1;
        }
        return length()-1;
    }
    /* (non-Javadoc)
     * @see iitb.CRF.SegmentDataSequence#setSegment(int, int, int)
     */
    public void setSegment(int segmentStart, int segmentEnd, int y) {
        for (int i = segmentStart; i <= segmentEnd; i++)
            set_y(i,y);
        assert(false);
    }
    public int getSegmentId(int offset) {
        return snum[offset];
    }
    public int segmentEnd(int segmentNum) {
        return segmentStart(segmentNum) + _tokens[segmentNum].length-1;
    }
    public int segmentLabel(int segmentNum) {
        return ls[segmentNum];
    }
    public int segmentStart(int segmentNum) {
        return spos[segmentNum];
    }
};