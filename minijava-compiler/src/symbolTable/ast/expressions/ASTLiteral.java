package symbolTable.ast.expressions;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public class ASTLiteral implements ASTOperand{
    private final Token tkLiteral;

    public ASTLiteral(Token tkLiteral) {
        this.tkLiteral = tkLiteral;
    }

    public void print() {
        System.out.print(tkLiteral.getLexeme());
    }

    @Override
    public Token getToken() {
        return null;//TODO implementar
    }

    @Override
    public STType check() {
        return null;//TODO implementar
    }
}
