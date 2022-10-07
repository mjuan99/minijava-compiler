package symbolTable.ast;

import lexicalAnalyzer.Token;

public class ASTVariableChaining implements ASTChaining{
    private final Token tkVariable;
    private final ASTChaining astChaining;

    public ASTVariableChaining(Token tkVariable, ASTChaining astChaining) {
        this.tkVariable = tkVariable;
        this.astChaining = astChaining;
    }

    public void print(){
        System.out.print("." + tkVariable.getLexeme());
        if(astChaining != null)
            astChaining.print();
    }
}
