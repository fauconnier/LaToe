package org.melodi.tools.fuzzymatcher.net.java.frej;


class ResultSet {
    
    private Result[] set;
    
    public static final ResultSet empty;
    
    
    Result best() {
        throw new UnsupportedOperationException("Not implemented");
    } // best
    
    
    static {
        empty = new ResultSet();
        empty.set = new Result[]{ new Result() };
    } // static
    
    
} // class ResultSet
