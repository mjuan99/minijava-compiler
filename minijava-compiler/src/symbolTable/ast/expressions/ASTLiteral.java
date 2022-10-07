package symbolTable.ast.expressions;

import lexicalAnalyzer.Token;
import symbolTable.types.*;

import java.util.Objects;

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
        return tkLiteral;
    }

    @Override
    public STType check() {
        if(Objects.equals(tkLiteral.getTokenType(), "intLiteral"))
            return new STTypeInt();
        else if(Objects.equals(tkLiteral.getTokenType(), "charLiteral"))
            return new STTypeChar();
        else if(Objects.equals(tkLiteral.getTokenType(), "StringLiteral"))
            return new STTypeReference(new Token("idClase", "String", 0));
        else if(Objects.equals(tkLiteral.getLexeme(), "true") || Objects.equals(tkLiteral.getLexeme(), "false"))
            return new STTypeBoolean();
        else
            return new STTypeNull();
    }
}
