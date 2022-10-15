package symbolTable.ast.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.types.STType;

public class ASTAccessVariable implements ASTAccess{
    private final Token tkVariable;
    private ASTChaining astChaining;

    public ASTAccessVariable(Token tkVariable, ASTChaining astChaining) {
        this.tkVariable = tkVariable;
        this.astChaining = astChaining;
    }

    public void print() {
        System.out.print(tkVariable.getLexeme());
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public Token getToken() {
        if(astChaining == null)
            return tkVariable;
        else
            return astChaining.getToken();
    }

    @Override
    public STType check() throws SemanticException {
        STType variableType;
        variableType = ST.symbolTable.getCurrentASTBlock().getVariableType(tkVariable.getLexeme());
        if(variableType == null){
            variableType = ST.symbolTable.getCurrentSTMethod().getArgumentType(tkVariable.getLexeme());
            if(variableType == null) {
                variableType = ST.symbolTable.getCurrentSTClass().getAttributeType(tkVariable.getLexeme());
                if (variableType != null && ST.symbolTable.getCurrentSTMethod().isStatic())
                    throw new SemanticException(new SemanticError(tkVariable, "acceso a atributo de instancia en metodo estatico"));
            }
        }
        if(variableType == null)
            throw new SemanticException(new SemanticError(tkVariable, "la variable " + tkVariable.getLexeme() + " no fue delcarada"));
        if(astChaining == null)
            return variableType;
        else
            return astChaining.check(variableType);
    }

    public void setASTChaining(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }

    @Override
    public boolean isValidCall() {
        if(astChaining == null)
            return false;
        else
            return astChaining.isValidCall();
    }

    @Override
    public boolean isValidVariable() {
        if(astChaining == null)
            return true;
        else
            return astChaining.isValidVariable();
    }
}
