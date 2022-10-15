package symbolTable.ast.expressions;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;
import symbolTable.types.STTypeBoolean;

public class ASTBooleanLiteral extends ASTLiteral{
    public ASTBooleanLiteral(Token tkLiteral){
        super(tkLiteral);
    }

    @Override
    public STType check() {
        return new STTypeBoolean();
    }
}
