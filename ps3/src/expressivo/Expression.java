/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import expressivo.parser.*;
//import org.antlr.v4.runtime.CharStreams;
import expressivo.parser.ExpressionParser.ExpressionContext;
import expressivo.parser.ExpressionParser.OpContext;
import expressivo.parser.ExpressionParser.PrimitiveContext;
//import expressivo.parser.ExpressionParser.ProductContext;
import expressivo.parser.ExpressionParser.RootContext;
//import expressivo.parser.ExpressionParser.SumContext;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS3 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    /* Datatype definition
       Expression = Number (a: int)
                   + Variable (name: String)
                   + Sum(a: Expression, b: Expression)
                   + Product(a: Expression, b: Expression)
        
    */
    
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    
    public static Expression parse(String input) {
        CharStream stream = new ANTLRInputStream(input);
        ExpressionLexer lexer = new ExpressionLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokens);
        ParseTree tree = parser.root();
        
        ParseTreeWalker walker = new ParseTreeWalker();
        MakeExpression listener = new MakeExpression();
        walker.walk(listener, tree);
        return listener.getExpression();
    }
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS3 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    public Expression differentiate(String V);
    
    public Expression simplify(Map<String,Double> environment);
    
    /**
     * 
     * @return sum of numbers in expression
     */
    public double sumOfNumbers();
    /**
     * 
     * @return product of numbers in expression
     */
    public double productOfNumbers();
    /**
     * 
     * @return sum of variables in expression
     */
    public Expression sumOfVars();
    /**
     * 
     * @return product of numbers in expression
     */
    public Expression productOfVars();
    
}

class MakeExpression implements ExpressionListener {
    private Stack<Expression> stack = new Stack<>();
    
    public Expression getExpression() {
        
        return stack.get(0);
    }
    
    public void enterEveryRule(ParserRuleContext arg0) {}
    public void exitEveryRule(ParserRuleContext arg0) {}
    public void visitErrorNode(ErrorNode arg0) {}
    public void visitTerminal(TerminalNode arg0) { }

    public void enterRoot(RootContext ctx) { }
    public void enterPrimitive(PrimitiveContext ctx) {}
    public void enterExpression(ExpressionContext ctx) {}
    public void enterOp(OpContext ctx) {}

    @Override
    public void exitRoot(RootContext ctx) {
        
        
    }

    @Override
    public void exitPrimitive(PrimitiveContext ctx) {
        if(ctx.NUMBER() != null) {
            double d = Double.valueOf(ctx.NUMBER().getText());
            Expression e = new Number(d);
            stack.push(e);
            
        }
        else if(ctx.VARIABLE() != null) {
            String s = ctx.VARIABLE().getText();
            Expression e = new Variable(s);
            stack.push(e);
        }
        else {
            
        }

       
        
    }

    @Override
    public void exitExpression(ExpressionContext ctx) {
        
        //expression : primitive (ADD primitive | MULT primitive)*;
        //1+(x+y)*3
        //result = some expression of sum
        
        List<Expression> expressions = new ArrayList<>();
        //Expression firstExpression = 
        assert stack.size() >= ctx.op().size();
        //assert ctx.op().size() > 0;
        for(int i = 0; i <= ctx.op().size(); i++) {
            expressions.add(stack.pop());
        }
        
        Collections.reverse(expressions);
        //expressions = <1, (x+y), 3>
        
        List<Boolean> isMult = new ArrayList<>();
        for(int i = 0; i < ctx.op().size(); i++) {
            if(ctx.op(i).MULT() != null) {
                isMult.add(true);
            }
            else if (ctx.op(i).ADD() != null){
                isMult.add(false);
            }
            else {
                assert false;
            }
        }
        
        
        //isMult = <false, true>
        
        int i = 0;
        while(i < isMult.size())  {
            if(isMult.get(i)) {
                //product between i and i+1
                //<+, *>
                //<1, (x+y), 3>
                expressions.set(i+1, (new Product(expressions.get(i), expressions.get(i+1))));
                //<1, (x+y), (x+y)*3>
                expressions.remove(i);
                isMult.remove(i);
                i--;
                //<+>
                // <1, (x+y)*3>  
                
            }
            i++;
        }   
        Expression sum = expressions.get(0);
        for(int j = 1; j < expressions.size(); j++) {
            sum = new Sum(sum, expressions.get(j));
        }
        stack.push(sum);
        
    }
    
    
    @Override
    public void exitOp(OpContext ctx) {
    }


    
}
