package symbolTable.ast.expressions;

import codeGenerator.CodeGenerator;
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
        CodeGenerator.generateCode("RMEM 1 ;lugar de retorno");
        CodeGenerator.generateCode("PUSH " + (tkLiteral.getLexeme().length()) + " ;celdas a reservar");
        CodeGenerator.generateCode("PUSH " + CodeGenerator.tagMalloc + " ;cargar la etiqueta de la rutina malloc");
        CodeGenerator.generateCode("CALL ;llamada a malloc");

        for(int i = 1; i < tkLiteral.getLexeme().length() - 1; i++){
            CodeGenerator.generateCode("DUP");
            CodeGenerator.generateCode("PUSH " + "'" + tkLiteral.getLexeme().charAt(i) + "'");
            CodeGenerator.generateCode("STOREREF " + i);
        }

        CodeGenerator.generateCode("DUP");
        CodeGenerator.generateCode("PUSH 0 ;terminador de string");
        CodeGenerator.generateCode("STOREREF " + (tkLiteral.getLexeme().length() - 1));
    }
}
