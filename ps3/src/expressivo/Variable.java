package expressivo;

import java.util.Map;

/**
 * 
 * @author mathurs
 * An immutable implementation of Expression that represents a variable.
 * AF: A variable named varName
 * RI: varName != "", every character in varName is a letter
 * Safety agreement: all types are immutable
 *
 **/
class Variable implements Expression {
    
    private final String varName;
    
    public Variable(String varName) {
        this.varName = varName;
        checkRep();
    }
    
    private void checkRep() {
        assert !varName.isEmpty();
        assert !varName.equals(null);
        char ch;
        for(int i = 0; i < varName.length(); i++) {
            ch = varName.charAt(i);
            assert (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
        }
    }
    
    @Override 
    public String toString() {
        return varName;
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (! (thatObject instanceof Variable)) {
            return false;
        }
        Variable thatVariable = (Variable) thatObject;
        return (thatVariable.varName.equals(varName));
    }
    
    @Override
    public int hashCode() { 
        return varName.hashCode();
    }
    
    @Override
    public Expression differentiate(String V) {
        if(V.equals(varName)) {
            return new Number(1);
        }
        return new Number(0);
    }
    
    @Override
    public Expression simplify(Map<String, Double> environment) {
        
        if(environment.containsKey(varName)) {
            return new Number(environment.get(varName));
        }
        return this;
    }

    @Override
    public double sumOfNumbers() {
        return 0;
    }

    @Override
    public double productOfNumbers() {
        return 1;
    }

    @Override
    public Expression sumOfVars() {
        return this;
    }

    @Override
    public Expression productOfVars() {
        return this;
    }

}
