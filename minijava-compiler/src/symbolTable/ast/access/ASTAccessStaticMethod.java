package symbolTable.ast.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.entities.STClass;
import symbolTable.entities.STInterface;
import symbolTable.entities.STMethod;
import symbolTable.types.STType;

import java.util.LinkedList;

public class ASTAccessStaticMethod extends ASTAccess{
    private final Token tkClassName;
    private final Token tkMethod;
    private final LinkedList<ASTExpression> arguments;

    public ASTAccessStaticMethod(Token tkClassName, Token tkMethod, LinkedList<ASTExpression> arguments, ASTChaining astChaining) {
        this.tkClassName = tkClassName;
        this.tkMethod = tkMethod;
        this.arguments = arguments;
        setASTChaining(astChaining);
    }

    public void print() {
        System.out.print(tkClassName.getLexeme() + "." + tkMethod.getLexeme() + "(");
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
        STClass stClass = ST.symbolTable.getSTClass(tkClassName.getLexeme());
        if(stClass == null) {
            STInterface stInterface = ST.symbolTable.getSTInterface(tkClassName.getLexeme());
            if(stInterface == null)
                throw new SemanticException(new SemanticError(tkClassName, "la clase " + tkClassName.getLexeme() + " no fue declarada"));
            else
                throw new SemanticException(new SemanticError(tkMethod, "intento de acceso a metodo estatico de interfaz " + tkClassName.getLexeme()));
        }
        STMethod stMethod = stClass.getMethod(tkMethod.getLexeme());
        if(stMethod == null || !stMethod.isStatic())
            throw new SemanticException(new SemanticError(tkMethod, "el metodo estatico " + tkMethod.getLexeme() + " no fue declarado en la clase " + stClass.getTKName().getLexeme()));
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
    public boolean isValidCallWithoutChaining() {
        return true;
    }

    @Override
    public boolean isValidVariableWithoutChaining() {
        return false;
    }
}
