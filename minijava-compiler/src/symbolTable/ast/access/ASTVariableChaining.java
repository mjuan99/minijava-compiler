package symbolTable.ast.access;

import codeGenerator.CodeGenerator;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.entities.STAttribute;
import symbolTable.entities.STClass;
import symbolTable.types.STType;

public class ASTVariableChaining implements ASTChaining{
    private final Token tkVariable;
    private final ASTChaining astChaining;
    private STAttribute stAttribute;
    private boolean isLeftSideOfAssignment = false;

    @Override
    public STType check(STType previousType) throws SemanticException {
        if(!previousType.isTypeReference())
            throw new SemanticException(new SemanticError(tkVariable, "no se puede aplicar encadenado a un tipo primitivo o void"));
        STClass previousTypeClass = ST.symbolTable.getSTClass(previousType.toString());
        if(previousTypeClass != null) {
            stAttribute = previousTypeClass.getAttribute(tkVariable.getLexeme());
            if (stAttribute == null)
                throw new SemanticException(new SemanticError(tkVariable, "el tipo " + previousType + " no tiene un atributo llamado " + tkVariable.getLexeme()));
            if(previousTypeClass != ST.symbolTable.getCurrentSTClass() && !stAttribute.isPublic())
                throw new SemanticException(new SemanticError(tkVariable, "intento de acceso a atributo privado " + tkVariable.getLexeme() + " de la clase " + previousType));
        }else
            throw new SemanticException(new SemanticError(tkVariable, "intento de acceso a atributos de la interfaz " + previousType));
        if(astChaining == null)
            return stAttribute.getSTType();
        else
            return astChaining.check(stAttribute.getSTType());
    }

    @Override
    public void generateCode() {
        if(!isLeftSideOfAssignment || astChaining != null)
            CodeGenerator.generateCode("LOADREF " + stAttribute.getOffset() + " ;carga valor del atributo en la pila");
        else{
            CodeGenerator.generateCode("SWAP");
            CodeGenerator.generateCode("STOREREF " + stAttribute.getOffset() + " ;almacena el tope de la pila en el atributo");
        }
        if(astChaining != null)
            astChaining.generateCode();
    }

    @Override
    public boolean isNotVoid() {
        return false;
    }

    @Override
    public void setLeftSideOfAssignment() {
        isLeftSideOfAssignment = true;
        if(astChaining != null)
            astChaining.setLeftSideOfAssignment();
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
