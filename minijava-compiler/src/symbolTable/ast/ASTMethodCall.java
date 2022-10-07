package symbolTable.ast;

public class ASTMethodCall implements ASTSentence{
    private final ASTAccess astAccess;

    public ASTMethodCall(ASTAccess astAccess) {
        this.astAccess = astAccess;
    }

    public void print(){
        astAccess.print();
        System.out.println(";");
    }
}
