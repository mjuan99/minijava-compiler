package symbolTable.ast.sentences;

import codeGenerator.CodeGenerator;

public class ASTEmptySentence extends ASTSentence{
    public void print(){
        System.out.println(";");
    }

    @Override
    public void checkSentences() {}

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("NOP ;sentencia vacia");
    }
}
