package symbolTable.ast;

import lexicalAnalyzer.Token;

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

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }
}
