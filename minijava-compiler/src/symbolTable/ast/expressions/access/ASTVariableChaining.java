package symbolTable.ast.expressions.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.types.STType;

public class ASTVariableChaining implements ASTChaining{
    private final Token tkVariable;
    private final ASTChaining astChaining;
    private boolean endsWithVariable;

    @Override
    public boolean endsWithVariable() {
        return endsWithVariable;
    }

    @Override
    public STType check(STType previousType) throws SemanticException {
        if(!previousType.isTypeReference())
            throw new SemanticException(new SemanticError(tkVariable, "no se puede aplicar encadenado a un tipo primitivo o void"));
        STType stType = ST.symbolTable.getSTClass(previousType.toString()).getAttributeType(tkVariable.getLexeme());
        if(stType == null)
            throw new SemanticException(new SemanticError(tkVariable, "el tipo " + previousType + " no tiene un atributo llamado " + tkVariable.getLexeme()));
        if(astChaining == null)
            return stType;
        else{
            STType chainingType = astChaining.check(stType);
            endsWithVariable = astChaining.endsWithVariable();
            return chainingType;
        }
    }

    public ASTVariableChaining(Token tkVariable, ASTChaining astChaining) {
        this.tkVariable = tkVariable;
        this.astChaining = astChaining;
        endsWithVariable = true;
    }

    public void print(){
        System.out.print("." + tkVariable.getLexeme());
        if(astChaining != null)
            astChaining.print();
    }
}
