package symbolTable.ast.sentences;

public class ASTEmptySentence implements ASTSentence{
    public void print(){
        System.out.println(";");
    }

    @Override
    public void checkSentences() {}
}