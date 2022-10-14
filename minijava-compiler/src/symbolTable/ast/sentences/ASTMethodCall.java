package symbolTable.ast.sentences;

import errors.SemanticError;
import errors.SemanticException;
import symbolTable.ast.access.ASTAccess;

public class ASTMethodCall implements ASTSentence{
    private final ASTAccess astAccess;

    public ASTMethodCall(ASTAccess astAccess) {
        this.astAccess = astAccess;
    }

    public void print(){
        astAccess.print();
        System.out.println(";");
    }

    @Override
    public void checkSentences() throws SemanticException{
        astAccess.check();
        if(!astAccess.isValidCall())
            throw new SemanticException(new SemanticError(astAccess.getToken(), "sentencia incorrecta, acceso a variable"));
    }
}
