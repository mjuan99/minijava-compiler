package symbolTable.ast;

import lexicalAnalyzer.Token;

public class ASTUnaryExpression implements ASTExpression{
    private final Token tkUnaryOperator;
    private final ASTOperand astOperand;
    private final ASTChaining astChaining;

    public ASTUnaryExpression(Token tkUnaryOperator, ASTOperand astOperand, ASTChaining astChaining) {
        this.tkUnaryOperator = tkUnaryOperator;
        this.astOperand = astOperand;
        this.astChaining = astChaining;
    }

    @Override
    public void print() {
        System.out.print((tkUnaryOperator != null ? tkUnaryOperator.getLexeme() : ""));
        astOperand.print();
    }
}
