package symbolTable.ast.expressions.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.types.STType;

import java.util.LinkedList;

public class ASTAccessMethod implements ASTAccess{
    private final Token tkMethod;
    private final LinkedList<ASTExpression> arguments;
    private ASTChaining astChaining;
    private boolean endsWithVariable;

    public ASTAccessMethod(Token tkMethod, LinkedList<ASTExpression> arguments, ASTChaining astChaining) {
        this.tkMethod = tkMethod;
        this.arguments = arguments;
        this.astChaining = astChaining;
        endsWithVariable = false;
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
        STType returnType = ST.symbolTable.getCurrentSTClass().getMethodReturnType(tkMethod.getLexeme());
        if(returnType == null)
            throw new SemanticException(new SemanticError(tkMethod, "el metodo " + tkMethod.getLexeme() + " no fue declarado en la clase " + ST.symbolTable.getCurrentSTClass().getTKName().getLexeme()));
        if(astChaining == null)
            return returnType;
        else{
            STType chainingType = astChaining.check(returnType);
            endsWithVariable = astChaining.endsWithVariable();
            return chainingType;
        }
    }

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }

    @Override
    public boolean endsWithVariable() {
        return endsWithVariable;
    }
}
