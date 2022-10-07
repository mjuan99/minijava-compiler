package symbolTable.ast.expressions.access;

import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.types.STType;

import java.util.LinkedList;

public class ASTMethodChaining implements ASTChaining{
    private final Token tkMethod;
    private final LinkedList<ASTExpression> arguments;
    private final ASTChaining astChaining;
    private boolean endsWithVariable;

    public ASTMethodChaining(Token tkMethod, LinkedList<ASTExpression> arguments, ASTChaining astChaining) {
        this.tkMethod = tkMethod;
        this.arguments = arguments;
        this.astChaining = astChaining;
        endsWithVariable = false;
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

    public boolean endsWithVariable() {
        return endsWithVariable;
    }

    @Override
    public STType check(STType previousType) throws SemanticException {
        return null;//TODO implementar
    }
}
