package symbolTable.ast.sentences;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ast.access.ASTAccess;

public class ASTMethodCall extends ASTSentence{
    private final ASTAccess astAccess;
    private final Token tkSemicolon;

    public ASTMethodCall(ASTAccess astAccess, Token tkSemicolon) {
        this.astAccess = astAccess;
        this.tkSemicolon = tkSemicolon;
    }

    public void print(){
        astAccess.print();
        System.out.println(";");
    }

    @Override
    public void checkSentences() throws SemanticException{
        astAccess.check();
        if(!astAccess.isValidCall())
            throw new SemanticException(new SemanticError(tkSemicolon, "sentencia incorrecta, no es una llamada valida"));
    }
}
