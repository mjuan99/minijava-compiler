package symbolTable.ast.expressions;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ast.access.ASTChaining;
import symbolTable.types.STType;
import symbolTable.types.STTypeBoolean;
import symbolTable.types.STTypeInt;

import java.util.Objects;

public class ASTUnaryExpression implements ASTExpression{
    private final Token tkUnaryOperator;
    private final ASTOperand astOperand;
    private ASTChaining astChaining;

    public ASTUnaryExpression(Token tkUnaryOperator, ASTOperand astOperand, ASTChaining astChaining) {
        this.tkUnaryOperator = tkUnaryOperator;
        this.astOperand = astOperand;
        this.astChaining = astChaining;
    }

    @Override
    public void print() {
        System.out.print((tkUnaryOperator != null ? tkUnaryOperator.getLexeme() : ""));
        astOperand.print();
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public Token getToken() {
        return astOperand.getToken();
    }

    @Override
    public STType check() throws SemanticException {
        //TODO implementar
        STType stType = astOperand.check();
        if(tkUnaryOperator != null) {
            if (Objects.equals(tkUnaryOperator.getLexeme(), "+") || Objects.equals(tkUnaryOperator.getLexeme(), "-"))
                if (!stType.conformsWith(new STTypeInt()))
                    throw new SemanticException(new SemanticError(tkUnaryOperator, "no se puede aplicar el operador " + tkUnaryOperator.getLexeme() + " al tipo " + stType));
            if(Objects.equals(tkUnaryOperator.getLexeme(), "!"))
                if(!stType.conformsWith(new STTypeBoolean()))
                    throw new SemanticException(new SemanticError(tkUnaryOperator, "no se puede aplicar el operador " + tkUnaryOperator.getLexeme() + " al tipo " + stType));
        }
        if(astChaining != null)
            return astChaining.check(stType);
        else
            return stType;
    }

    public void setASTChainng(ASTChaining astChaining) {
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
}
