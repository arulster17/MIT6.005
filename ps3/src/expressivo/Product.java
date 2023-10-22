package expressivo;
import java.util.Map;

/**
 * 
 * @author mathurs
 * An immutable implementation of Expression that represents a product of expressions.
 * AF: Represents an expression that is the product of the expressions in values
 * RI: values is nonempty
 * Safety agreement: all types are private, we are not currently returning values
 *
 **/
/*
class Product implements Expression {
    private final List<Expression> values;
    public Product(List<Expression> values) {
        this.values = values;
        checkRep();
    }
    private void checkRep() {assert values.size() != 0;}
    
    @Override 
    public String toString() {
        String result = values.get(0).toString();
        for(int i = 1; i < values.size(); i++) {
            result += "*" + values.get(i).toString();
        }
        return result;
        //return "(" + value1.toString() + " * " + value2.toString() + ")";
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (! (thatObject instanceof Product)) {
            return false;
        }
        Product thatProduct = (Product) thatObject;
        
        if (thatProduct.values.size() != values.size()) return false;
        
        for(int i = 0; i < values.size(); i++) {
            if (! thatProduct.values.get(i).equals(values.get(i))) return false;
        }
        
        return true;
    }
    
    @Override
    public int hashCode() { 
        int product = 1;
        for (Expression value : values) {product *= value.hashCode();}
        return product;
    }
}

*/

/**
 * 
 * @author mathurs
 * An immutable implementation of Expression that represents a product of two expressions.
 * AF: Represents an expression that is the product of two expressions 
 * RI: value1 and value2 are valid Expressions
 * Safety agreement: all types are private, we are not currently returning value1 or value2
 *
 **/
 class Product implements Expression {
    private final Expression value1;
    private final Expression value2;
    public Product(Expression value1, Expression value2) {
        this.value1 = value1;
        this.value2 = value2;
        checkRep();
    }
    private void checkRep() {}
    
    @Override 
    public String toString() {
        return "(" + value1.toString() + "*" + value2.toString() + ")";
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (! (thatObject instanceof Product)) {
            return false;
        }
        Product thatProduct = (Product) thatObject;
        return (thatProduct.value1.equals(value1) && thatProduct.value2.equals(value2));
    }
    
    @Override
    public int hashCode() { 
        return value1.hashCode() * value2.hashCode();
    }
    
    @Override
    public Expression differentiate(String V) {
        Expression fg1 = new Product(value1, value2.differentiate(V));
        Expression f1g = new Product(value1.differentiate(V), value2);
        return new Sum(fg1, f1g);
    }
    
    @Override
    public Expression simplify(Map<String, Double> environment) {
        Expression e = new Product(value1.simplify(environment), value2.simplify(environment));
        
        Expression newNum = new Number(e.productOfNumbers());
        Expression newVar = e.productOfVars();
        if(newNum.equals(new Number(1)) && newVar.equals(new Number(1))) {
            return new Number(1);
        }
        if(newNum.equals(new Number(1))) {
            return newVar;
        }
        if(newVar.equals(new Number(1))) {
            return newNum;
        }
        if(newNum.equals(new Number(0))) {
            return new Number(0);        
        }
        if(newVar.equals(new Number(0))) {
            return new Number(0);       
        }
        
        return new Product(newNum, newVar);
        
    }
    @Override
    public double sumOfNumbers() {
        return 0;
    }
    @Override
    public double productOfNumbers() {
        return value1.productOfNumbers()*value2.productOfNumbers();
    }
    @Override
    public Expression sumOfVars() {
        return this;
    }
    @Override
    public Expression productOfVars() {
        if(value1.productOfVars().equals(new Number(1)) && value2.productOfVars().equals(new Number(1))) {
            return new Number(1);
        }
        if(value1.productOfVars().equals(new Number(1))) {
            return value2.productOfVars();
        }
        if(value2.productOfVars().equals(new Number(1))) {
            return value1.productOfVars();
        }
        if(value1.productOfVars().equals(new Number(0))) {
            return new Number(0);        
        }
        if(value2.productOfVars().equals(new Number(0))) {
            return new Number(0);       
        }
        return new Product(value1.productOfVars(), value2.productOfVars());
    }

}


