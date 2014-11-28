package org.latoe.layoutanalysis.pdf.labelisation;

import java.util.Comparator;
import java.util.Map;

public class ValueComparatorIntInt implements Comparator<Integer> {

    Map<Integer, Integer> base;
    public ValueComparatorIntInt(Map<Integer, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(Integer a, Integer b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }

}
