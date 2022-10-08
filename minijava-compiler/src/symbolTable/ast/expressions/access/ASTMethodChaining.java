package symbolTable.ast.expressions.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.types.STType;

import java.util.LinkedList;

public class ASTMethodChaining implements ASTChaining{
    private final Token tkMethod;
    private final LinkedList<ASTExpression> arguments;
    private final ASTChaining astChaining;

    public ASTMethodChaining(Token tkMethod, LinkedList<ASTExpression> arguments, ASTChaining astChaining) {
        this.tkMethod = tkMethod;
        this.arguments = arguments;
        this.astChaining = astChaining;
    }

    public void print(){
        System.out.print("." + tkMethod.getLexeme() + "(");
        for(ASTExpression argument : arguments){
            argument.print();
            System.out.print(", ");
        }
        System.out.print(")");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public STType check(STType previousType) throws SemanticException {
        if(!previousType.isTypeReference())
            throw new SemanticException(new SemanticError(tkMethod, "no se puede aplicar encadenado a un tipo primitivo o void"));
        STType stType = ST.symbolTable.getSTClass(previousType.toString()).getMethodReturnType(tkMethod.getLexeme());
        if(stType == null)
            throw new SemanticException(new SemanticError(tkMethod, "el tipo " + previousType + " no tiene un metodo llamado " + tkMethod.getLexeme()));
        if(astChaining == null)
            return stType;
        else{
            return astChaining.check(stType);
        }
    }

    @Override
    public Token getToken() {
        if(astChaining == null)
            return tkMethod;
        else
            return astChaining.getToken();
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
