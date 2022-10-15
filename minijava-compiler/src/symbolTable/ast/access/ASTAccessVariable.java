package symbolTable.ast.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.types.STType;

public class ASTAccessVariable extends ASTAccess{
    private final Token tkVariable;

    public ASTAccessVariable(Token tkVariable) {
        this.tkVariable = tkVariable;
    }

    public void print() {
        System.out.print(tkVariable.getLexeme());
        if(astChaining != null)
            astChaining.print();
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
        return checkChaining(variableType);
    }

    @Override
    public boolean isValidCallWithoutChaining() {
        return false;
    }

    @Override
    public boolean isValidVariableWithoutChaining() {
        return true;
    }
}
