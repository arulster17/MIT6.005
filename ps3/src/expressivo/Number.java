package expressivo;

import java.util.Map;

/**
 * 
 * @author mathurs
 * An immutable implementation of Expression that represents a number.
 * AF: A number with value value
 * RI: value >= 0
 * Safety agreement: all types are immutable
 *
 **/
class Number implements Expression {
    
    private final double value;
    
    public Number(double value) {
        this.value = value;
        checkRep();
    }
   
    private void checkRep() {
        assert value >= 0;
    }
    
    @Override
    public String toString() {
        if(value % 1 == 0) {
            return Integer.toString((int) value);
        }
        return Double.toString(value);
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (! (thatObject instanceof Number)) {
            return false;
        }
        Number thatNumber = (Number) thatObject;
        return (thatNumber.value == value);
    }
    
    @Override
    public int hashCode() { 
        return Double.hashCode(value);
    }
    
    @Override
    public Expression differentiate(String V) {
        return new Number(0);
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        
        return this;
    }

    @Override
    public double sumOfNumbers() {
        return value;
    }

    @Override
    public double productOfNumbers() {
        return value;
    }

    @Override
    public Expression sumOfVars() {
        return new Number(0);
    }

    @Override
    public Expression productOfVars() {
        return new Number(1);
    }
    
}