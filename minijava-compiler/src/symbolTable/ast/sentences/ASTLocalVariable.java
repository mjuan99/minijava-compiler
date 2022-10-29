package symbolTable.ast.sentences;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.entities.STVariable;
import symbolTable.types.STType;
import symbolTable.types.STTypeNull;
import symbolTable.types.STTypeVoid;

public class ASTLocalVariable extends ASTSentence{
    private final Token tkVariable;
    private final Token tkAssignment;
    private final ASTExpression value;

    public ASTLocalVariable(Token tkVariable, Token tkAssignment, ASTExpression value) {
        this.tkVariable = tkVariable;
        this.tkAssignment = tkAssignment;
        this.value = value;
    }

    public void print(){
        System.out.print("var " + tkVariable.getLexeme() + " = ");
        value.print();
        System.out.println(";");
    }

    @Override
    public void checkSentences() throws SemanticException {
        if(ST.symbolTable.getCurrentSTMethod().getArgumentType(tkVariable.getLexeme()) != null)
            throw new SemanticException(new SemanticError(tkVariable, "el nombre de la variable " + tkVariable.getLexeme() + " coincide con el nombre de un argumento"));
        else if(ST.symbolTable.getCurrentASTBlock().getVariableType(tkVariable.getLexeme()) != null)
            throw new SemanticException(new SemanticError(tkVariable, "la variable " + tkVariable.getLexeme() + " ya fue declarada"));
        STType variableType = value.check();
        if(variableType.equals(new STTypeNull()))
            throw new SemanticException(new SemanticError(tkAssignment, "declaracion de variable con valor null"));
        if(variableType.equals(new STTypeVoid()))
            throw new SemanticException(new SemanticError(tkAssignment, "declaracion de variable con valor void"));
        ST.symbolTable.getCurrentASTBlock().insertVariable(tkVariable.getLexeme(), new STVariable(tkVariable, variableType));
    }

    @Override
    public void generateCode() {
        //TODO implementar
    }
}
