package symbolTable.ast.access;

import codeGenerator.CodeGenerator;
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
    private STClass stClass;

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

    @Override
    protected boolean isNotVoidWithoutChaining() {
        return false;
    }

    public void print() {
        System.out.print("new " + tkClassName.getLexeme() + "()");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public STType check() throws SemanticException {
        stClass = ST.symbolTable.getSTClass(tkClassName.getLexeme());
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

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("RMEM 1 ;lugar de retorno");
        CodeGenerator.generateCode("PUSH " + (stClass.getAttributesSize() + 1) + " ;celdas a reservar");
        CodeGenerator.generateCode("PUSH " + CodeGenerator.tagMalloc);
        CodeGenerator.generateCode("CALL ;llamada a malloc");

        CodeGenerator.generateCode("DUP");
        CodeGenerator.generateCode("PUSH " + stClass.getVTableTag());
        CodeGenerator.generateCode("STOREREF 0 ;set vtable");

        if(astChaining != null)
            astChaining.generateCode();
    }
}
