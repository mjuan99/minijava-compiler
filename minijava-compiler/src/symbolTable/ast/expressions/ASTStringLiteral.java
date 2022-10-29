package symbolTable.ast.expressions;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;
import symbolTable.types.STTypeReference;

public class ASTStringLiteral extends ASTLiteral{
    public ASTStringLiteral(Token tkLiteral){
        super(tkLiteral);
    }

    @Override
    public STType check() {
            return new STTypeReference(new Token("idClase", "String", 0));
    }

    @Override
    public void generateCode() {
        //TODO implementar
    }
}
