package symbolTable.ast;

public class ASTEmptySentence implements ASTSentence{
    public void print(){
        System.out.println(";");
    }

    @Override
    public void checkSentences() {}
}
