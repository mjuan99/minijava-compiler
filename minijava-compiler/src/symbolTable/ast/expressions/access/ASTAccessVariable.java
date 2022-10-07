package symbolTable.ast.expressions.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.types.STType;

public class ASTAccessVariable implements ASTAccess{
    private final Token tkVariable;
    private ASTChaining astChaining;
    private boolean endsWithVariable;

    public ASTAccessVariable(Token tkVariable, ASTChaining astChaining) {
        this.tkVariable = tkVariable;
        this.astChaining = astChaining;
        endsWithVariable = true;
    }

    public void print() {
        System.out.print(tkVariable.getLexeme());
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public Token getToken() {
        return null;//TODO implementar
    }

    @Override
    public STType check() throws SemanticException {
        STType variableType;
        variableType = ST.symbolTable.getCurrentASTBlock().getVariableType(tkVariable.getLexeme());
        if(variableType == null){
            variableType = ST.symbolTable.getCurrentSTMethod().getArgumentType(tkVariable.getLexeme());
            if(variableType == null)
                variableType = ST.symbolTable.getCurrentSTClass().getAttributeType(tkVariable.getLexeme());
        }
        if(variableType == null)
            throw new SemanticException(new SemanticError(tkVariable, "la variable " + tkVariable.getLexeme() + " no fue delcarada"));
        if(astChaining == null)
            return variableType;
        else{
            STType chainingType = astChaining.check(variableType);
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
