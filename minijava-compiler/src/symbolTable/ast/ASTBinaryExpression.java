package symbolTable.ast;

import lexicalAnalyzer.Token;

public class ASTBinaryExpression implements ASTExpression{
    private final ASTExpression astExpressionLeftSide;
    private final Token tkBinaryOperator;
    private final ASTUnaryExpression astUnaryExpRightSide;
    private final ASTChaining astChaining;

    public ASTBinaryExpression(ASTExpression astExpressionLeftSide, Token tkBinaryOperator, ASTUnaryExpression astUnaryExpRightSide, ASTChaining astChaining) {
        this.astExpressionLeftSide = astExpressionLeftSide;
        this.tkBinaryOperator = tkBinaryOperator;
        this.astUnaryExpRightSide = astUnaryExpRightSide;
        this.astChaining = astChaining;
    }

    @Override
    public void print() {
        astExpressionLeftSide.print();
        System.out.print(tkBinaryOperator.getLexeme());
        astUnaryExpRightSide.print();
    }
}
