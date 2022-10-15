package symbolTable.ast.expressions;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;
import symbolTable.types.STTypeInt;

public class ASTIntLiteral extends ASTLiteral{
    public ASTIntLiteral(Token tkLiteral){
        super(tkLiteral);
    }

    @Override
    public STType check() {
        return new STTypeInt();
    }
}
