package symbolTable.ast.expressions;

import lexicalAnalyzer.Token;
import symbolTable.ast.expressions.access.ASTChaining;
import symbolTable.types.STType;

public class ASTBinaryExpression implements ASTExpression{
    private final ASTExpression leftSide;
    private final Token tkBinaryOperator;
    private final ASTUnaryExpression rightSide;
    private ASTChaining astChaining;

    public ASTBinaryExpression(ASTExpression leftSide, Token tkBinaryOperator, ASTUnaryExpression astUnaryExpRightSide, ASTChaining astChaining) {
        this.leftSide = leftSide;
        this.tkBinaryOperator = tkBinaryOperator;
        this.rightSide = astUnaryExpRightSide;
        this.astChaining = astChaining;
    }

    @Override
    public void print() {
        System.out.print("(");
        leftSide.print();
        System.out.print(" " + tkBinaryOperator.getLexeme() + " ");
        rightSide.print();
        System.out.print(")");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public Token getToken() {
        return null;//TODO implementar
    }

    @Override
    public STType check() {
        return null;//TODO implementar
    }

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }

    @Override
    public boolean isValidCall() {
        if(astChaining == null)
            return false;
        else
            return astChaining.isValidCall();
    }

    @Override
    public boolean isValidVariable() {
        if(astChaining == null)
            return false;
        else
            return astChaining.isValidVariable();
    }
}
