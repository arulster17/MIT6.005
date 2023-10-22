package expressivo;
import java.util.Map;

/**
 * 
 *  @author mathurs
 * An immutable implementation of Expression that represents a sum of expressions.
 * AF: Represents an expression that is the sum of expressions in values 
 * RI: values is not empty
 * Safety agreement: all types are private, we are not currently returning values
 *
 **/
/*
class Sum implements Expression {
    private final List<Expression> values;
    
    public Sum(List<Expression> values) {
        this.values = values;
        checkRep();
    }
    private void checkRep() {assert values.size() != 0;}
    
    @Override 
    public String toString() {
        String result = values.get(0).toString();
        for(int i = 1; i < values.size(); i++) {
            result += "+" + values.get(i).toString();
        }
        return result;
        //return "(" + value1.toString() + " + " + value2.toString() + ")";
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (! (thatObject instanceof Sum)) {
            return false;
        }
        Sum thatSum = (Sum) thatObject;
        
        if (thatSum.values.size() != values.size()) return false;
        
        for(int i = 0; i < values.size(); i++) {
            if (! thatSum.values.get(i).equals(values.get(i))) return false;
        }
        
        return true;
    }
    
    @Override
    public int hashCode() { 
        int sum = 0;
        for (Expression value : values) {sum += value.hashCode();}
        return sum;
    }
}
*/


/**
 * 
 * @author mathurs
 * An immutable implementation of Expression that represents a sum of two expressions.
 * AF: Represents an expression that is the sum of two expressions 
 * RI: value1 and value2 are valid Expressions
 * Safety agreement: all types are private, we are not currently returning value1 or value2
 *
 **/

class Sum implements Expression {
    private final Expression value1;
    private final Expression value2;
    
    public Sum(Expression value1, Expression value2) {
        this.value1 = value1;
        this.value2 = value2;
        checkRep();
    }
    private void checkRep() {}
    
    @Override 
    public String toString() {
        return "(" + value1.toString() + "+" + value2.toString() + ")";
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (! (thatObject instanceof Sum)) {
            return false;
        }
        Sum thatSum = (Sum) thatObject;
        return (thatSum.value1.equals(value1) && thatSum.value2.equals(value2));
    }
    
    @Override
    public int hashCode() { 
        return value1.hashCode() + value2.hashCode();
    }
    
    @Override
    public Expression differentiate(String V) {
        return new Sum(value1.differentiate(V), value2.differentiate(V));
    }
    
    @Override
    public Expression simplify(Map<String, Double> environment) {
        Expression e = new Sum(value1.simplify(environment), value2.simplify(environment));
        Expression newNum = new Number(e.sumOfNumbers());
        Expression newVar = e.sumOfVars();
        
        if(newNum.equals(new Number(0)) && newVar.equals(new Number(0))) {
            return new Number(0);
        }
        if(newNum.equals(new Number(0))) {
            return newVar;
        }
        if(newVar.equals(new Number(0))) {
            return newNum;
        }
        
        
        return new Sum(new Number(e.sumOfNumbers()), e.sumOfVars());
    }
    
    
    @Override
    public double sumOfNumbers() {
        return value1.sumOfNumbers() + value2.sumOfNumbers();
    }
    @Override
    public double productOfNumbers() {
        return 1;
    }
    @Override
    public Expression sumOfVars() {
        if(value1.sumOfVars().equals(new Number(0)) && value2.sumOfVars().equals(new Number(0))) {
            return new Number(0);
        }
        if(value1.sumOfVars().equals(new Number(0))) {
            return value2.sumOfVars();
        }
        if(value2.sumOfVars().equals(new Number(0))) {
            return value1.sumOfVars();
        }
        return new Sum(value1.sumOfVars(), value2.sumOfVars());
    }
    @Override
    public Expression productOfVars() {
        return this;
    }

}
