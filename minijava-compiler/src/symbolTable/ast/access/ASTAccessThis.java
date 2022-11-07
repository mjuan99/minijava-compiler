package symbolTable.ast.access;

import codeGenerator.CodeGenerator;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.types.STType;
import symbolTable.types.STTypeReference;

public class ASTAccessThis extends ASTAccess{
    private final Token tkThis;

    public ASTAccessThis(Token tkThis) {
        this.tkThis = tkThis;
    }

    @Override
    public boolean isValidCallWithoutChaining() {
        return false;
    }

    @Override
    public boolean isValidVariableWithoutChaining() {
        return false;
    }

    @Override
    protected boolean isNotVoidWithoutChaining() {
        return false;
    }

    public void print() {
        System.out.print("this");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public STType check() throws SemanticException {
        if(ST.symbolTable.getCurrentSTMethod().isStatic())
            throw new SemanticException(new SemanticError(tkThis, "acceso a this en un metodo estatico"));
        return checkChaining(new STTypeReference(ST.symbolTable.getCurrentSTClass().getTKName()));
    }

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("LOAD 3 ;cargar this");
        if(astChaining != null)
            astChaining.generateCode();
    }
}
