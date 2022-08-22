import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.LexicalException;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.sourceFileManager.SourceFileManager;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public final static int ERR_ARGUMENTO_FALTANTE = 1;
    public final static int ERR_ARCHIVO_NO_ENCONTRADO = 2;
    public final static int ERR_IO = 3;

    public static void main(String[] args){
        if(args.length == 0)
            System.exit(ERR_ARGUMENTO_FALTANTE);
        LexicalAnalyzer lexicalAnalyzer = null;
        try {
            SourceFileManager sourceFileManager = new SourceFileManager(args[0]);
            lexicalAnalyzer = new LexicalAnalyzer(sourceFileManager);
        } catch (FileNotFoundException exception) {
            System.out.println("Archivo no encontrado");
            System.exit(ERR_ARCHIVO_NO_ENCONTRADO);
        } catch (IOException exception){
            System.out.println("Error de IO");
            System.exit(ERR_IO);
        }

        boolean eof = false;

        while (!eof) {
            try {
                Token token = lexicalAnalyzer.getNextToken();
                if(token.getTokenType() == "EOF") {
                    eof = true;
                    System.out.println("\n[SinErrores]");
                }else
                    System.out.println(token);
            }
            catch (LexicalException exception){
                System.out.println("Error Léxico en línea " + exception.getLineNumber() + ": " + exception.getMessage());
                System.out.println("\n[Error:" + exception.getLexeme() + '|' + exception.getLineNumber() + "]");
                break;
            }
            catch(IOException exception){
                System.out.println("Error de IO");
                System.exit(ERR_IO);
            }
        }
    }
}
