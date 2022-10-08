package symbolTable.ast.expressions.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.entities.STMethod;
import symbolTable.types.STType;

import java.util.LinkedList;

public class ASTAccessMethod implements ASTAccess{
    private final Token tkMethod;
    private final LinkedList<ASTExpression> arguments;
    private ASTChaining astChaining;

    public ASTAccessMethod(Token tkMethod, LinkedList<ASTExpression> arguments, ASTChaining astChaining) {
        this.tkMethod = tkMethod;
        this.arguments = arguments;
        this.astChaining = astChaining;
    }

    public void print() {
        System.out.print(tkMethod.getLexeme() + "(");
        for(ASTExpression argument : arguments){
            argument.print();
            System.out.print(", ");
        }
        System.out.print(")");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public Token getToken() {
        if(astChaining == null)
            return tkMethod;
        else
            return astChaining.getToken();
    }

    @Override
    public STType check() throws SemanticException {
        STMethod stMethod = ST.symbolTable.getCurrentSTClass().getMethod(tkMethod.getLexeme());
        if(stMethod == null)
            throw new SemanticException(new SemanticError(tkMethod, "el metodo " + tkMethod.getLexeme() + " no fue declarado en la clase " + ST.symbolTable.getCurrentSTClass().getTKName().getLexeme()));
        if(ST.symbolTable.getCurrentSTMethod().isStatic() && !stMethod.isStatic())
            throw new SemanticException(new SemanticError(tkMethod, "acceso a metodo dinamico en metodo estatico"));
        if(astChaining == null)
            return stMethod.getSTReturnType();
        else
            return astChaining.check(stMethod.getSTReturnType());
    }

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }

    @Override
    public boolean isValidCall() {
        if(astChaining == null)
            return true;
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
}
