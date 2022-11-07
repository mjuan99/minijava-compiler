package symbolTable.ast.expressions;

import codeGenerator.CodeGenerator;
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

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("PUSH " + tkLiteral.getLexeme() + " ;cargar literal char en la pila");
    }
}
