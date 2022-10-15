package symbolTable.ast.expressions;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;
import symbolTable.types.STTypeChar;

public class ASTCharLiteral extends ASTLiteral{
    public ASTCharLiteral(Token tkLiteral){
        super(tkLiteral);
    }

    @Override
    public STType check() {
        return new STTypeChar();
    }
}
