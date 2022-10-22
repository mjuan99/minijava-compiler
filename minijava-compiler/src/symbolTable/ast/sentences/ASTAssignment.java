package symbolTable.ast.sentences;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.ast.access.ASTAccess;
import symbolTable.types.STType;
import symbolTable.types.STTypeInt;

import java.util.Objects;

public class ASTAssignment extends ASTSentence{
    private final ASTAccess astAccess;
    private final Token tkAssignment;
    private final ASTExpression value;

    public ASTAssignment(ASTAccess astAccess, Token tkAssignment, ASTExpression value) {
        this.astAccess = astAccess;
        this.tkAssignment = tkAssignment;
        this.value = value;
    }

    public void print() {
        astAccess.print();
        System.out.print(" " + tkAssignment.getLexeme() + " ");
        value.print();
        System.out.println(";");
    }

    @Override
    public void checkSentences() throws SemanticException {
        STType accessType = astAccess.check();
        if(!astAccess.isValidVariable())
            throw new SemanticException(new SemanticError(tkAssignment, "sentencia incorrecta, asignacion a un acceso no asignable"));
        STType valueType = value.check();
        if(Objects.equals(tkAssignment.getLexeme(), "=")) {
            if (!valueType.conformsWith(accessType))
                throw new SemanticException(new SemanticError(tkAssignment, "el tipo de la expresion " + valueType + " no coincide con el tipo de la variable asignada " + accessType));
        }else{
            if(!accessType.conformsWith(new STTypeInt()))
                throw new SemanticException(new SemanticError(tkAssignment, "la asignacion " + tkAssignment.getLexeme() + " esperaba una variable tipo int pero se encontro una variable tipo " + accessType));
            if(!valueType.conformsWith(new STTypeInt()))
                throw new SemanticException(new SemanticError(tkAssignment, "la asignacion " + tkAssignment.getLexeme() + " esperaba una expresion tipo int pero se encontro una expresion tipo " + valueType));
        }
    }
}
