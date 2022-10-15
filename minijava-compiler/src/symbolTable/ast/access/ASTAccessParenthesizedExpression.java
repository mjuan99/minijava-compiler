package symbolTable.ast.access;

import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.types.STType;

public class ASTAccessParenthesizedExpression implements ASTAccess{

    private final ASTExpression astExpression;
    private ASTChaining astChaining;

    public ASTAccessParenthesizedExpression(ASTExpression astExpression) {
        this.astExpression = astExpression;
    }

    @Override
    public void setASTChaining(ASTChaining astChaining) {
        this.astChaining = astChaining;
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
            return false;
        else
            return astChaining.isValidVariable();
    }

    @Override
    public void print() {

    }

    @Override
    public Token getToken() {
        return null;
    }

    @Override
    public STType check() throws SemanticException {
        STType expressionType = astExpression.check();
        if(astChaining == null)
            return expressionType;
        else
            return astChaining.check(expressionType);
    }
}
