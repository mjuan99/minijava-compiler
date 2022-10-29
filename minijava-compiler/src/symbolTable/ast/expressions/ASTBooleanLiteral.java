package symbolTable.ast.expressions;

import codeGenerator.CodeGenerator;
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

    @Override
    public void generateCode() {
        if(tkLiteral.getLexeme().equals("true"))
            CodeGenerator.generateCode("PUSH 1");
        else
            CodeGenerator.generateCode("PUSH 0");
    }
}
