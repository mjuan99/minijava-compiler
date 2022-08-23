import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.LexicalException;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.sourceFileManager.SourceFileManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    public final static int ERR_ARGUMENTO_FALTANTE = 1;
    public final static int ERR_ARCHIVO_NO_ENCONTRADO = 2;
    public final static int ERR_IO = 3;

    public static void main(String[] args){
        if(args.length == 0)
            System.exit(ERR_ARGUMENTO_FALTANTE);
        LexicalAnalyzer lexicalAnalyzer = null;
        SourceFileManager sourceFileManager = null;
        try {
            sourceFileManager = new SourceFileManager(args[0]);
            lexicalAnalyzer = new LexicalAnalyzer(sourceFileManager);
        } catch (FileNotFoundException exception) {
            System.out.println("Archivo no encontrado");
            System.exit(ERR_ARCHIVO_NO_ENCONTRADO);
        } catch (IOException exception){
            System.out.println("Error de IO");
            System.exit(ERR_IO);
        }

        boolean eof = false;
        boolean error = false;

        while (!eof) {
            try{
                try {
                    Token token = lexicalAnalyzer.getNextToken();
                    if (Objects.equals(token.getTokenType(), "EOF")) {
                        eof = true;
                        if (!error)
                            System.out.println("\n[SinErrores]");
                    } else {
                        if (!error)
                            System.out.println(token);
                    }
                } catch (LexicalException exception) {
                    error = true;
                    System.out.println("Error Léxico en línea " + exception.getLineNumber() +
                            " columna " + exception.getColumnNumber() + ": " + exception.getMessage() +
                            " --> " + exception.getLexeme());
                    System.out.println("Detalle: " + sourceFileManager.getLine(exception.getLineNumber()));
                    System.out.println(getSpaces(exception.getColumnNumber() + 8) + '^');
                    System.out.println("\n[Error:" + exception.getLexeme() + '|' + exception.getLineNumber() + "]\n");
                }
            }
            catch(IOException exception){
                System.out.println("Error de IO");
                System.exit(ERR_IO);
            }
        }
    }

    private static String getSpaces(int spaces){
        char[] array = new char[spaces];
        Arrays.fill(array, ' ');
        return new String(array);
    }
}
