package symbolTable.ast.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.types.STType;
import symbolTable.types.STTypeReference;

public class ASTAccessThis implements ASTAccess{
    private final Token tkThis;
    private ASTChaining astChaining;

    public ASTAccessThis(Token tkThis, ASTChaining astChaining) {
        this.astChaining = astChaining;
        this.tkThis = tkThis;
    }

    @Override
    public boolean isValidCall() {
        if(astChaining == null)
            return false;
        else
            return astChaining.isValidCall();
    }

    @Override
    public boolean isValidVariable() {
        if(astChaining == null)
            return false;
        else
            return astChaining.isValidVariable();
    }

    public void print() {
        System.out.print("this");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public Token getToken() {
        if(astChaining == null)
            //TODO ver de donde saco el token
            return new Token("pr_this", "this", 0);
        else
            return astChaining.getToken();
    }

    @Override
    public STType check() throws SemanticException {
        if(ST.symbolTable.getCurrentSTMethod().isStatic())
            throw new SemanticException(new SemanticError(tkThis, "acceso a this en un metodo estatico"));
        if(astChaining == null)
            return new STTypeReference(ST.symbolTable.getCurrentSTClass().getTKName());
        else
            return astChaining.check(new STTypeReference(ST.symbolTable.getCurrentSTClass().getTKName()));
    }

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }
}
