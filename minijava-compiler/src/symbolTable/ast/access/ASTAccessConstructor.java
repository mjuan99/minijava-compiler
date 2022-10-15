package symbolTable.ast.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.entities.STClass;
import symbolTable.entities.STInterface;
import symbolTable.types.STType;
import symbolTable.types.STTypeReference;

public class ASTAccessConstructor extends ASTAccess{
    private final Token tkClassName;
    private final boolean hasArguments;

    public ASTAccessConstructor(Token tkClassName, boolean hasArguments) {
        this.tkClassName = tkClassName;
        this.hasArguments = hasArguments;
    }

    @Override
    protected boolean isValidCallWithoutChaining() {
        return true;
    }

    @Override
    public boolean isValidVariableWithoutChaining() {
        return false;
    }

    public void print() {
        System.out.print("new " + tkClassName.getLexeme() + "()");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public STType check() throws SemanticException {
        STClass stClass = ST.symbolTable.getSTClass(tkClassName.getLexeme());
        if(stClass == null) {
            STInterface stInterface = ST.symbolTable.getSTInterface(tkClassName.getLexeme());
            if(stInterface == null)
                throw new SemanticException(new SemanticError(tkClassName, "la clase " + tkClassName.getLexeme() + " no fue declarada"));
            else
                throw new SemanticException(new SemanticError(tkClassName, "intento de acceso a constructor de interfaz " + tkClassName.getLexeme()));
        }
        if(hasArguments)
            throw new SemanticException(new SemanticError(tkClassName, "constructor de clase no deberia tener argumentos"));
        return checkChaining(new STTypeReference(tkClassName));
    }
}
