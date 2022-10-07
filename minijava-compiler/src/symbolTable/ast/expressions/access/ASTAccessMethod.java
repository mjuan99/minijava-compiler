package symbolTable.ast.expressions.access;

import lexicalAnalyzer.Token;
import symbolTable.ast.expressions.ASTExpression;
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
        return null;//TODO implementar
    }

    @Override
    public STType check() {
        return null;//TODO implementar
    }

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }

    @Override
    public boolean endsWithVariable() {
        return false;//TODO implementar
    }
}
