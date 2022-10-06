package symbolTable.ast;

import lexicalAnalyzer.Token;

public class ASTLiteral implements ASTOperand{
    private final Token tkValue;

    public ASTLiteral(Token tkValue) {
        this.tkValue = tkValue;
    }

    public void print() {
        System.out.print(tkValue.getLexeme());
    }
}
