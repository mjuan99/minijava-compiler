package symbolTable.ast.sentences;

public class ASTEmptySentence extends ASTSentence{
    public void print(){
        System.out.println(";");
    }

    @Override
    public void checkSentences() {}
}
