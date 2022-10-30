package symbolTable.ast.sentences;

import codeGenerator.CodeGenerator;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.entities.STMethod;
import symbolTable.types.STType;
import symbolTable.types.STTypeVoid;

public class ASTReturn extends ASTSentence{
    private final Token tkReturn;
    private final ASTExpression returnExpression;

    public ASTReturn(Token tkReturn, ASTExpression returnExpression){
        this.tkReturn = tkReturn;
        this.returnExpression = returnExpression;
        alwaysReturns = true;
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
                throw new SemanticException(new SemanticError(tkReturn, "retorno vacio en un metodo no void"));
        }else{
            STType returnType = returnExpression.check();
            if(!returnType.conformsWith(ST.symbolTable.getCurrentSTMethod().getSTReturnType()))
                throw new SemanticException(new SemanticError(tkReturn, "el tipo retornado " + returnType + " no conforma con el tipo de retorno del método (" + ST.symbolTable.getCurrentSTMethod().getSTReturnType().toString() + ")"));
        }
    }

    @Override
    public void generateCode() {
        STMethod currentMethod = ST.symbolTable.getCurrentSTMethod();
        ASTBlock currentBlock = ST.symbolTable.getCurrentASTBlock();
        if(returnExpression != null){
            returnExpression.generateCode();
            CodeGenerator.generateCode("STORE " + (currentMethod.getArguments().size() + (currentMethod.isStatic() ? 3 : 4)));
        }
        CodeGenerator.generateCode("FMEM " + currentBlock.getCurrentVariableOffset());
        CodeGenerator.generateCode("STOREFP ;actualizar registro de activación (desapilar)");
        CodeGenerator.generateCode("RET " + (currentMethod.getArguments().size() + (currentMethod.isStatic() ? 0 : 1)) + " ;retorno del metodo");
    }
}
