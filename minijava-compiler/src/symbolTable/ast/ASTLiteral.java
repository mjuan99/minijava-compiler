package symbolTable.ast;

import lexicalAnalyzer.Token;

public class ASTLiteral implements ASTOperand{
    private final Token tkLiteral;

    public ASTLiteral(Token tkLiteral) {
        this.tkLiteral = tkLiteral;
    }

    public void print() {
        System.out.print(tkLiteral.getLexeme());
    }
}
