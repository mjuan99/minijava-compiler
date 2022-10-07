package symbolTable.ast;

import lexicalAnalyzer.Token;

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

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }
}
