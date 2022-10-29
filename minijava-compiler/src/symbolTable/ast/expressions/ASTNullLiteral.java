package symbolTable.ast.expressions;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;
import symbolTable.types.STTypeNull;

public class ASTNullLiteral extends ASTLiteral{

    public ASTNullLiteral(Token tkLiteral){
        super(tkLiteral);
    }

    @Override
    public STType check() {
        return new STTypeNull();
    }

    @Override
    public void generateCode() {
        //TODO implementar
    }
}
