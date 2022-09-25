package errors;

import lexicalAnalyzer.Token;

import java.util.Objects;

public class SyntacticError implements CompilerError{
    private final Token found;
    private final String expected;

    public SyntacticError(Token found, String expected){
        this.found = found;
        this.expected = expected;
    }

    public String getMessage(){
        String message = "Error sintactico en linea " + found.getLineNumber() + ": se esperaba " + expected +
                " pero se encontro " + (Objects.equals(found.getLexeme(), "") ? "EOF" : found.getLexeme()) +
                "\n[Error:" + found.getLexeme() + "|" + found.getLineNumber() + "]\n";
        message = message.replaceAll("idMetVar", "identificador de metodo o variable");
        message = message.replaceAll("idClase", "identificador de clase");
        return message;
    }

}
