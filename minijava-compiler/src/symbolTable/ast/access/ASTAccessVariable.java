package symbolTable.ast.access;

import codeGenerator.CodeGenerator;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.entities.STArgument;
import symbolTable.entities.STAttribute;
import symbolTable.entities.STVariable;
import symbolTable.types.STType;

public class ASTAccessVariable extends ASTAccess{
    private final Token tkVariable;
    private STArgument stArgument;
    private STAttribute stAttribute;
    private STVariable stVariable;

    public ASTAccessVariable(Token tkVariable) {
        this.tkVariable = tkVariable;
        stVariable = null;
        stArgument = null;
        stAttribute = null;
    }

    @Override
    protected boolean isNotVoidWithoutChaining() {
        return false;
    }

    public void print() {
        System.out.print(tkVariable.getLexeme());
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public STType check() throws SemanticException {
        STType variableType = null;
        stVariable = ST.symbolTable.getCurrentASTBlock().getVariable(tkVariable.getLexeme());
        if(stVariable != null) variableType = stVariable.getVariableType();
        if(variableType == null){
            stArgument = ST.symbolTable.getCurrentSTMethod().getArgument(tkVariable.getLexeme());
            if(stArgument != null) variableType = stArgument.getType();
            if(variableType == null) {
                stAttribute = ST.symbolTable.getCurrentSTClass().getAttribute(tkVariable.getLexeme());
                if(stAttribute != null) variableType = stAttribute.getSTType();
                if (stVariable != null && ST.symbolTable.getCurrentSTMethod().isStatic())
                    throw new SemanticException(new SemanticError(tkVariable, "acceso a atributo de instancia en metodo estatico"));
            }
        }
        if(variableType == null)
            throw new SemanticException(new SemanticError(tkVariable, "la variable " + tkVariable.getLexeme() + " no fue delcarada"));
        return checkChaining(variableType);
    }

    @Override
    public void generateCode() {
        if(stAttribute != null) {
            CodeGenerator.generateCode("LOAD 3 ;carga this");
            if(!isLeftSideOfAssignment || astChaining != null)
                CodeGenerator.generateCode("LOADREF " + stAttribute.getOffset() + " ;carga valor del atributo en la pila");
            else{
                CodeGenerator.generateCode("SWAP ;swap el valor de la asignacion con this");
                CodeGenerator.generateCode("STOREREF " + stAttribute.getOffset() + " ;almacena el tope de la pila en el atributo");
            }
        }
        else if(stArgument != null){
            if(!isLeftSideOfAssignment || astChaining != null)
                CodeGenerator.generateCode("LOAD " + stArgument.getOffset() + " ;carga valor del argumento en la pila");
            else
                CodeGenerator.generateCode("STORE " + stArgument.getOffset() + " ;almacena el tope de la pila en el argumento");
        }else if(stVariable != null){
            if(!isLeftSideOfAssignment || astChaining != null)
                CodeGenerator.generateCode("LOAD " + stVariable.getOffset() + " ;carga valor de la variable en la pila");
            else
                CodeGenerator.generateCode("STORE " + stVariable.getOffset() + " ;almacena el tope de la pila en la variable");
        }
        if(astChaining != null)
            astChaining.generateCode();
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
