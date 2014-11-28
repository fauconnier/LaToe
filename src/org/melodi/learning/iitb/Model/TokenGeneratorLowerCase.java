/*
 * Created on May 4, 2008
 * @author sunita
 */
package org.melodi.learning.iitb.Model;

/**
 * 
 * @author Sunita Sarawagi
 * @since 1.3
 * @version 1.3
 */
public class TokenGeneratorLowerCase extends TokenGenerator {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5104463806229821948L;

	@Override
    public Object getKey(Object xArg) {
        return xArg.toString().toLowerCase();
    }
}
