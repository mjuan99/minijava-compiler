package symbolTable.ast.expressions.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.types.STType;

public class ASTVariableChaining implements ASTChaining{
    private final Token tkVariable;
    private final ASTChaining astChaining;

    @Override
    public STType check(STType previousType) throws SemanticException {
        if(!previousType.isTypeReference())
            throw new SemanticException(new SemanticError(tkVariable, "no se puede aplicar encadenado a un tipo primitivo o void"));
        STType stType = ST.symbolTable.getSTClass(previousType.toString()).getAttributeType(tkVariable.getLexeme());
        if(stType == null)
            throw new SemanticException(new SemanticError(tkVariable, "el tipo " + previousType + " no tiene un atributo llamado " + tkVariable.getLexeme()));
        if(astChaining == null)
            return stType;
        else
            return astChaining.check(stType);
    }

    public ASTVariableChaining(Token tkVariable, ASTChaining astChaining) {
        this.tkVariable = tkVariable;
        this.astChaining = astChaining;
    }

    public void print(){
        System.out.print("." + tkVariable.getLexeme());
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
