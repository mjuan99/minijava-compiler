package symbolTable.ast.expressions;

import lexicalAnalyzer.Token;

public abstract class ASTLiteral implements ASTOperand{
    private final Token tkLiteral;

    public ASTLiteral(Token tkLiteral) {
        this.tkLiteral = tkLiteral;
    }

    public void print() {
        System.out.print(tkLiteral.getLexeme());
    }
}
