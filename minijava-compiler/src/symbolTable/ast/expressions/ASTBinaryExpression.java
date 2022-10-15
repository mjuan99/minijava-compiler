package symbolTable.ast.expressions;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.types.STType;
import symbolTable.types.STTypeBoolean;
import symbolTable.types.STTypeInt;

import java.util.Objects;

public class ASTBinaryExpression implements ASTExpression{
    private final ASTExpression leftSide;
    private final Token tkBinaryOperator;
    private final ASTUnaryExpression rightSide;

    public ASTBinaryExpression(ASTExpression leftSide, Token tkBinaryOperator, ASTUnaryExpression astUnaryExpRightSide) {
        this.leftSide = leftSide;
        this.tkBinaryOperator = tkBinaryOperator;
        this.rightSide = astUnaryExpRightSide;
    }

    @Override
    public void print() {
        System.out.print("(");
        leftSide.print();
        System.out.print(" " + tkBinaryOperator.getLexeme() + " ");
        rightSide.print();
        System.out.print(")");
    }

    @Override
    public STType check() throws SemanticException {
        STType leftSideType = leftSide.check();
        STType rightSideType = rightSide.check();
        STType expressionType;
        if(Objects.equals(tkBinaryOperator.getLexeme(), "+") || Objects.equals(tkBinaryOperator.getLexeme(), "-") ||
                Objects.equals(tkBinaryOperator.getLexeme(), "*") || Objects.equals(tkBinaryOperator.getLexeme(), "/") ||
                Objects.equals(tkBinaryOperator.getLexeme(), "%")){
            if(!leftSideType.conformsWith(new STTypeInt()))
                throw new SemanticException(new SemanticError(tkBinaryOperator, "Expresion de tipo " + leftSideType + " cuando el operador " + tkBinaryOperator.getLexeme() + " esperaba int"));
            if(!rightSideType.conformsWith(new STTypeInt()))
                throw new SemanticException(new SemanticError(tkBinaryOperator, "Expresion de tipo " + rightSideType + " cuando el operador " + tkBinaryOperator.getLexeme() + " esperaba int"));
            expressionType = new STTypeInt();
        }else if(Objects.equals(tkBinaryOperator.getLexeme(), "&&") || Objects.equals(tkBinaryOperator.getLexeme(), "||")){
            if(!leftSideType.conformsWith(new STTypeBoolean()))
                throw new SemanticException(new SemanticError(tkBinaryOperator, "Expresion de tipo " + leftSideType + " cuando el operador " + tkBinaryOperator.getLexeme() + " esperaba boolean"));
            if(!rightSideType.conformsWith(new STTypeBoolean()))
                throw new SemanticException(new SemanticError(tkBinaryOperator, "Expresion de tipo " + rightSideType + " cuando el operador " + tkBinaryOperator.getLexeme() + " esperaba boolean"));
            expressionType = new STTypeBoolean();
        }else if(Objects.equals(tkBinaryOperator.getLexeme(), "==") || Objects.equals(tkBinaryOperator.getLexeme(), "!=")){
            if(!leftSideType.conformsWith(rightSideType) && !rightSideType.conformsWith(leftSideType))
                throw new SemanticException(new SemanticError(tkBinaryOperator, "Comparación de tipos no conformantes (" + leftSideType + " y " + rightSideType + ")"));
            expressionType = new STTypeBoolean();
        }else{
            if(!leftSideType.conformsWith(new STTypeInt()))
                throw new SemanticException(new SemanticError(tkBinaryOperator, "Expresion de tipo " + leftSideType + " cuando el operador " + tkBinaryOperator.getLexeme() + " esperaba int"));
            if(!rightSideType.conformsWith(new STTypeInt()))
                throw new SemanticException(new SemanticError(tkBinaryOperator, "Expresion de tipo " + rightSideType + " cuando el operador " + tkBinaryOperator.getLexeme() + " esperaba int"));
            expressionType = new STTypeBoolean();
        }
        return expressionType;
    }
}
