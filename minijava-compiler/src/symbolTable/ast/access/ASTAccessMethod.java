package symbolTable.ast.access;

import codeGenerator.CodeGenerator;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.entities.STMethod;
import symbolTable.types.STType;
import symbolTable.types.STTypeVoid;

import java.util.LinkedList;

public class ASTAccessMethod extends ASTAccess{
    private final Token tkMethod;
    private final LinkedList<ASTExpression> arguments;
    private STMethod stMethod;

    public ASTAccessMethod(Token tkMethod, LinkedList<ASTExpression> arguments) {
        this.tkMethod = tkMethod;
        this.arguments = arguments;
    }

    public void print() {
        System.out.print(tkMethod.getLexeme() + "(");
        for(ASTExpression argument : arguments){
            argument.print();
            System.out.print(", ");
        }
        System.out.print(")");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public STType check() throws SemanticException {
        stMethod = ST.symbolTable.getCurrentSTClass().getMethod(tkMethod.getLexeme());
        if(stMethod == null)
            throw new SemanticException(new SemanticError(tkMethod, "el metodo " + tkMethod.getLexeme() + " no fue declarado en la clase " + ST.symbolTable.getCurrentSTClass().getTKName().getLexeme()));
        if(ST.symbolTable.getCurrentSTMethod().isStatic() && !stMethod.isStatic())
            throw new SemanticException(new SemanticError(tkMethod, "acceso a metodo dinamico en metodo estatico"));
        if(arguments.size() != stMethod.getArguments().size())
            throw new SemanticException(new SemanticError(tkMethod, "llamada al metodo " + tkMethod.getLexeme() + " con " + arguments.size() + " argumentos cuando se esperaban " + stMethod.getArguments().size() + " argumentos"));
        for(int i = 0; i < arguments.size(); i++){
            STType foundType = arguments.get(i).check();
            STType expectedType = stMethod.getArguments().get(i).getType();
            if(!foundType.conformsWith(expectedType))
                throw new SemanticException(new SemanticError(tkMethod, "el tipo del " + (i + 1) + "° argumento actual (" + foundType + ") del método " + stMethod.getTKName().getLexeme() + " no conforma con el tipo del argumento formal (" + expectedType + ")"));
        }
        return checkChaining(stMethod.getSTReturnType());
    }

    @Override
    public void generateCode() {
        if(!stMethod.isStatic())
            CodeGenerator.generateCode("LOAD 3 ;this");
        if(!stMethod.getSTReturnType().equals(new STTypeVoid())) {
            CodeGenerator.generateCode("RMEM 1 ;lugar de retorno");
            CodeGenerator.generateCode("SWAP");
        }
        for(int i = 0; i <arguments.size(); i++){
            ASTExpression argument = arguments.get(i);
            argument.generateCode();
            CodeGenerator.setComment("argumento " + i + " del metodo " + stMethod.getSTClass().getTKName().getLexeme() + "." + stMethod.getTKName().getLexeme());
            if(!stMethod.isStatic())
                CodeGenerator.generateCode("SWAP");
        }
        if(!stMethod.isStatic()) {
            CodeGenerator.generateCode("DUP");
            CodeGenerator.generateCode("LOADREF 0");
            CodeGenerator.generateCode("LOADREF " + stMethod.getOffset());
        }
        else
            CodeGenerator.generateCode("PUSH " + stMethod.getMethodTag());
        CodeGenerator.generateCode("CALL ;llamada a metodo " + stMethod.getTKName().getLexeme());
        if(astChaining != null)
            astChaining.generateCode();
        //TODO implementar
    }

    @Override
    public boolean isValidCallWithoutChaining() {
        return true;
    }

    @Override
    public boolean isValidVariableWithoutChaining() {
        return false;
    }

    @Override
    protected boolean isNotVoidWithoutChaining() {
        return !stMethod.getSTReturnType().equals(new STTypeVoid());
    }
}
