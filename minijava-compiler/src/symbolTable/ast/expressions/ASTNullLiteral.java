package symbolTable.ast.expressions;

import codeGenerator.CodeGenerator;
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
        CodeGenerator.generateCode("PUSH 0 ;cargar literal null en la pila");
    }
}
