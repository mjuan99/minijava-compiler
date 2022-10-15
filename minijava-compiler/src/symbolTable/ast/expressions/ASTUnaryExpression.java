package symbolTable.ast.expressions;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.types.STType;
import symbolTable.types.STTypeBoolean;
import symbolTable.types.STTypeInt;

import java.util.Objects;

public class ASTUnaryExpression implements ASTExpression{
    private final Token tkUnaryOperator;
    private final ASTOperand astOperand;

    public ASTUnaryExpression(Token tkUnaryOperator, ASTOperand astOperand) {
        this.tkUnaryOperator = tkUnaryOperator;
        this.astOperand = astOperand;
    }

    @Override
    public void print() {
        System.out.print((tkUnaryOperator != null ? tkUnaryOperator.getLexeme() : ""));
        astOperand.print();
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
        return stType;
    }
}
