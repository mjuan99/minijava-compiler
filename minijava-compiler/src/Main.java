import errors.SemanticException;
import errors.SyntacticException;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.sourceFileManager.SourceFileManager;
import semanticAnalyzer.SemanticAnalyzer;
import symbolTable.ST;
import syntacticAnalyzer.SyntacticAnalyzer;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public final static int ERR_ARGUMENTO_FALTANTE = 1;
    public final static boolean DEBUG = true;

    public static void main(String[] args){
        if(args.length == 0) {
            System.out.println("Argumento faltante");
            System.exit(ERR_ARGUMENTO_FALTANTE);
        }
        analyze(args[0]);
    }

    private static void analyze(String sourceFilePath) {
        try {
            SourceFileManager sourceFileManager = new SourceFileManager(sourceFilePath);
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceFileManager);
            SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer);
            new SemanticAnalyzer(syntacticAnalyzer);
            if(DEBUG)
                ST.symbolTable.print();
            System.out.println("[SinErrores]");
        } catch (FileNotFoundException exception) {
            System.out.println("Archivo no encontrado");
        } catch (IOException exception){
            System.out.println("Error de IO");
        } catch (SyntacticException | SemanticException e) {
            if(DEBUG)
                ST.symbolTable.print();
            System.out.println(e.getMessage());
        }
    }
}
