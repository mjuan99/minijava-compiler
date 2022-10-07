package symbolTable.ast.sentences;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.types.STType;
import symbolTable.types.STTypeVoid;

public class ASTReturn implements ASTSentence{
    private final ASTExpression returnExpression;

    public ASTReturn(ASTExpression returnExpression){
        this.returnExpression = returnExpression;
    }

    @Override
    public void print() {
        System.out.print("return ");
        if(returnExpression != null)
            returnExpression.print();
        System.out.println(";");
    }

    @Override
    public void checkSentences() throws SemanticException {
        if(returnExpression == null) {
            if (!ST.symbolTable.getCurrentSTMethod().getSTReturnType().equals(new STTypeVoid()))
                //TODO ver de donde saco el token
                throw new SemanticException(new SemanticError(new Token("pr_return", "return", 0), "retorno vacio en un metodo no void"));
        }else{
            STType returnType = returnExpression.check();
            if(!returnType.conformsWith(ST.symbolTable.getCurrentSTMethod().getSTReturnType()))
                throw new SemanticException(new SemanticError(returnExpression.getToken(), "el tipo retornado " + returnType + " no conforma con el tipo de retorno del método (" + ST.symbolTable.getCurrentSTMethod().getSTReturnType().toString() + ")"));
        }
    }
}
