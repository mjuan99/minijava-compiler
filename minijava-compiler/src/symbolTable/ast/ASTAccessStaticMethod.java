package symbolTable.ast;

import lexicalAnalyzer.Token;

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

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }
}
