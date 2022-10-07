package symbolTable.ast.expressions.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.entities.STClass;
import symbolTable.types.STType;

import java.util.LinkedList;

public class ASTAccessStaticMethod implements ASTAccess{
    private final Token tkClassName;
    private final Token tkMethod;
    private final LinkedList<ASTExpression> arguments;
    private ASTChaining astChaining;
    private boolean endsWithVariable;

    public ASTAccessStaticMethod(Token tkClassName, Token tkMethod, LinkedList<ASTExpression> arguments, ASTChaining astChaining) {
        this.tkClassName = tkClassName;
        this.tkMethod = tkMethod;
        this.arguments = arguments;
        this.astChaining = astChaining;
        endsWithVariable = false;
    }

    public void print() {
        System.out.print(tkClassName.getLexeme() + "." + tkMethod.getLexeme() + "(");
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
        STClass stClass = ST.symbolTable.getSTClass(tkClassName.getLexeme());
        if(stClass == null)
            throw new SemanticException(new SemanticError(tkClassName, "la clase " + tkClassName.getLexeme() + " no fue declarada"));
        STType returnType = stClass.getStaticMethodReturnType(tkMethod.getLexeme());
        if(returnType == null)
            throw new SemanticException(new SemanticError(tkMethod, "el metodo estatico " + tkMethod.getLexeme() + " no fue declarado en la clase " + stClass.getTKName().getLexeme()));
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
