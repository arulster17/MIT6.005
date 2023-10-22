/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    
    /* Testing strategy
    Numbers: 0, 1, n
    Variables: 0, 1, n
    Unique Variables: 0, 1, n
    Sum: 0, 1, n
    Product: 0, 1, n
    Parentheses: none, do not impact PEMDAS, impact PEMDAS
    
    T1: 0N 0V 0U 0S 0P X
    T1: 1N 1V 1U 1S 0P N
    T1: 1N 2V 2U 2S 1P Y
    T1: 0N 3V 3U 0S 2P X
     
    */
    @Test
    public void testExpressions() {

        Expression e1 =  Expression.parse("3");
        Expression e2 =  Expression.parse("(x+1)");
        Expression e3 =  Expression.parse("1+(x+Y)*3");
        Expression e4 =  Expression.parse("wPls*wdj*wdh");
        
        Expression e5 =  Expression.parse("x+1");
        Expression e6 =  Expression.parse("x + 1");
        Expression e7 =  Expression.parse("(x) +    (1)");
        Expression e8 =  Expression.parse("(1+x)");
        
        Expression e9 =  Expression.parse("3+4+5");
        Expression e10 =  Expression.parse("(3+4+5)");
        
        assertTrue("toString failed", e1.toString().equals("3"));
        assertTrue("toString failed", e2.toString().equals("(x+1)"));
        assertTrue("toString failed", e3.toString().equals("(1+((x+Y)*3))"));
        assertTrue("toString failed", e4.toString().equals("((wPls*wdj)*wdh)"));
        
        assertTrue("equals failed", e2.equals(e5));
        assertTrue("equals failed", e2.equals(e6));
        assertTrue("equals failed", e2.equals(e7));
        assertFalse("equals failed", e2.equals(e8));
        assertTrue("equals failed", e9.equals(e10));
        
        
        assertTrue("hashcode failed", e2.hashCode() == e5.hashCode());
        assertTrue("hashcode failed", e2.hashCode() == e6.hashCode());
        assertTrue("hashcode failed", e2.hashCode() == e7.hashCode());
        assertTrue("hashcode failed", e9.hashCode() == e10.hashCode());
    }
        
     
    /* Testing strategy
    Numbers: 0, 1, n
    Variables: 0, 1, n
    Unique Variables: 0, 1, n
    Sum: 0, 1, n
    Product: 0, 1, n
    Parentheses: none, do not impact PEMDAS, impact PEMDAS
    Differentiation: variable present w/others , not present w/ others, present w/o others, not present w/o others
    
    T1: 0N 0V 0U 0S 0P X
    T1: 1N 1V 1U 1S 0P N
    T1: 1N 2V 2U 2S 1P Y
    T1: 0N 3V 3U 0S 2P X
     
    */
    @Test
    public void testDifferentiation() {
        //d/dx all of these
        Expression e1 =  Expression.parse("3");
        Expression e2 =  Expression.parse("(x+1)");
        Expression e3 =  Expression.parse("1+(x+Y)*3");
        Expression e4 =  Expression.parse("wPls*wdj*wdh");
        
        assertEquals("diff failed", "0", Commands.differentiate(e1.toString(), "0"));
        //manual check the rest are correct
        System.out.println(Commands.differentiate(e2.toString(), "x")); //√
        System.out.println(Commands.differentiate(e3.toString(), "x")); //√
        System.out.println(Commands.differentiate(e4.toString(), "x")); //√
    }
        
        
        

        
    
    
    
    // TODO tests for Expression
    
}
