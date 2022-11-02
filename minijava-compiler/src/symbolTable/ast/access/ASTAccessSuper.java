package symbolTable.ast.access;

import codeGenerator.CodeGenerator;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.types.STType;
import symbolTable.types.STTypeReference;

public class ASTAccessSuper extends ASTAccess{
    private final Token tkSuper;

    public ASTAccessSuper(Token tkSuper) {
        this.tkSuper = tkSuper;
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
        System.out.print("super");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public STType check() throws SemanticException {
        if(ST.symbolTable.getCurrentSTMethod().isStatic())
            throw new SemanticException(new SemanticError(tkSuper, "acceso a super en un metodo estatico"));
        if(astChaining == null)
            throw new SemanticException(new SemanticError(tkSuper, "acceso a super sin encadenado"));
        return astChaining.check(new STTypeReference(ST.symbolTable.getCurrentSTClass().getTKClassItExtends()));
    }

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("LOAD 3");
        astChaining.generateCode();
    }


}
