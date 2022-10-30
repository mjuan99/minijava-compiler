package symbolTable.ast.access;

import codeGenerator.CodeGenerator;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.entities.STAbstractMethod;
import symbolTable.entities.STClass;
import symbolTable.entities.STInterface;
import symbolTable.types.STType;
import symbolTable.types.STTypeVoid;

import java.util.LinkedList;

public class ASTMethodChaining implements ASTChaining{
    private final Token tkMethod;
    private final LinkedList<ASTExpression> arguments;
    private final ASTChaining astChaining;
    private STAbstractMethod stMethod;

    public ASTMethodChaining(Token tkMethod, LinkedList<ASTExpression> arguments, ASTChaining astChaining) {
        this.tkMethod = tkMethod;
        this.arguments = arguments;
        this.astChaining = astChaining;
    }

    public void print(){
        System.out.print("." + tkMethod.getLexeme() + "(");
        for(ASTExpression argument : arguments){
            argument.print();
            System.out.print(", ");
        }
        System.out.print(")");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public STType check(STType previousType) throws SemanticException {
        if(!previousType.isTypeReference())
            throw new SemanticException(new SemanticError(tkMethod, "no se puede aplicar encadenado a un tipo primitivo o void"));
        STClass stClass = ST.symbolTable.getSTClass(previousType.toString());
        if(stClass != null)
            stMethod = stClass.getMethod(tkMethod.getLexeme());
        else {
            STInterface stInterface = ST.symbolTable.getSTInterface(previousType.toString());
            stMethod = stInterface.getMethod(tkMethod.getLexeme());
        }
        if(stMethod == null)
            throw new SemanticException(new SemanticError(tkMethod, "el tipo " + previousType + " no tiene un metodo llamado " + tkMethod.getLexeme()));
        if(arguments.size() != stMethod.getArguments().size())
            throw new SemanticException(new SemanticError(tkMethod, "llamada al metodo " + tkMethod.getLexeme() + " con " + arguments.size() + " argumentos cuando se esperaban " + stMethod.getArguments().size() + " argumentos"));
        for(int i = 0; i < arguments.size(); i++){
            STType foundType = arguments.get(i).check();
            STType expectedType = stMethod.getArguments().get(i).getType();
            if(!foundType.conformsWith(expectedType))
                throw new SemanticException(new SemanticError(tkMethod, "el tipo del " + (i + 1) + "° argumento actual (" + foundType + ") del método " + stMethod.getTKName().getLexeme() + " no conforma con el tipo del argumento formal (" + expectedType + ")"));
        }
        if(astChaining == null)
            return stMethod.getSTReturnType();
        else{
            return astChaining.check(stMethod.getSTReturnType());
        }
    }

    @Override
    public void generateCode() {
        if(stMethod.isStatic())
            CodeGenerator.generateCode("FMEM 1 ;libero espacio del this en llamada a metodo estatico");
        if(!stMethod.getSTReturnType().equals(new STTypeVoid())) {
            CodeGenerator.generateCode("RMEM 1 ;lugar de retorno");
            if(!stMethod.isStatic())
                CodeGenerator.generateCode("SWAP");
        }
        for(int i = 0; i <arguments.size(); i++){
            ASTExpression argument = arguments.get(i);
            argument.generateCode();
            CodeGenerator.setComment("argumento " + i + " del metodo " + stMethod.getTKName().getLexeme());
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
    public boolean isNotVoid() {
        if(astChaining == null)
            return !stMethod.getSTReturnType().equals(new STTypeVoid());
        else
            return astChaining.isNotVoid();
    }

    @Override
    public boolean isValidCall() {
        if(astChaining == null)
            return true;
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
