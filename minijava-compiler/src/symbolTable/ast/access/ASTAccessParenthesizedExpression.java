package symbolTable.ast.access;

import errors.SemanticException;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.types.STType;

public class ASTAccessParenthesizedExpression extends ASTAccess{

    private final ASTExpression astExpression;

    public ASTAccessParenthesizedExpression(ASTExpression astExpression) {
        this.astExpression = astExpression;
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
    public void print() {
        astExpression.print();
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public STType check() throws SemanticException {
        STType expressionType = astExpression.check();
        return checkChaining(expressionType);
    }
}
