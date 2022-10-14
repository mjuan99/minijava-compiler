package symbolTable.ast.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.entities.STClass;
import symbolTable.types.STType;

public class ASTVariableChaining implements ASTChaining{
    private final Token tkVariable;
    private final ASTChaining astChaining;

    @Override
    public STType check(STType previousType) throws SemanticException {
        STType stType;
        if(!previousType.isTypeReference())
            throw new SemanticException(new SemanticError(tkVariable, "no se puede aplicar encadenado a un tipo primitivo o void"));
        STClass previousTypeClass = ST.symbolTable.getSTClass(previousType.toString());
        if(previousTypeClass != null) {
            stType = previousTypeClass.getAttributeType(tkVariable.getLexeme());
            if (stType == null)
                throw new SemanticException(new SemanticError(tkVariable, "el tipo " + previousType + " no tiene un atributo llamado " + tkVariable.getLexeme()));
        }else
            throw new SemanticException(new SemanticError(tkVariable, "intento de acceso a atributos de la interfaz " + previousType));
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
