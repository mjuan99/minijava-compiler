package symbolTable.ast.access;

import errors.SemanticException;
import symbolTable.ast.expressions.ASTOperand;
import symbolTable.types.STType;

public abstract class ASTAccess implements ASTOperand {
    protected ASTChaining astChaining;
    protected boolean isLeftSideOfAssignment = false;

    public void setASTChaining(ASTChaining astChaining){
        this.astChaining = astChaining;
    }

    public STType checkChaining(STType accessType) throws SemanticException {
        if(astChaining == null)
            return accessType;
        else
            return astChaining.check(accessType);
    }

    public void setLeftSideOfAssignment(){
        isLeftSideOfAssignment = true;
    }

    public boolean isValidCall(){
        if(astChaining == null)
            return isValidCallWithoutChaining();
        else
            return astChaining.isValidCall();
    }

    protected abstract boolean isValidCallWithoutChaining();

    public boolean isValidVariable(){
        if(astChaining == null)
            return isValidVariableWithoutChaining();
        else
            return astChaining.isValidVariable();
    }

    protected abstract boolean isValidVariableWithoutChaining();

    public boolean isNotVoid() {
        if(astChaining == null)
            return isNotVoidWithoutChaining();
        else
            return astChaining.isNotVoid();
    }

    protected abstract boolean isNotVoidWithoutChaining();
}
