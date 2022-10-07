package symbolTable.ast.expressions.access;

import lexicalAnalyzer.Token;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.types.STType;

import java.util.LinkedList;

public class ASTAccessStaticMethod implements ASTAccess{
    private final Token tkClassName;
    private final Token tkMethod;
    private final LinkedList<ASTExpression> arguments;
    private ASTChaining astChaining;

    public ASTAccessStaticMethod(Token tkClassName, Token tkMethod, LinkedList<ASTExpression> arguments, ASTChaining astChaining) {
        this.tkClassName = tkClassName;
        this.tkMethod = tkMethod;
        this.arguments = arguments;
        this.astChaining = astChaining;
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
        return null;//TODO implementar
    }

    @Override
    public STType check() {
        //TODO implementar
        return null;
    }

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }

    @Override
    public boolean endsWithVariable() {
        return false;//TODO implementar
    }
}
