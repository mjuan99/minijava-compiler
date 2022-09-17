import Errors.SyntacticException;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.sourceFileManager.SourceFileManager;
import syntacticAnalyzer.SyntacticAnalyzer;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public final static int ERR_ARGUMENTO_FALTANTE = 1;

    public static void main(String[] args){
        if(args.length == 0) {
            System.out.println("Argumento faltante");
            System.exit(ERR_ARGUMENTO_FALTANTE);
        }
        //analyzeAllErrorFiles();
        analyze(args[0]);
    }

    private static void analyzeAllErrorFiles() {
        for(int i = 1; i <= 63; i++){
            System.out.println("\n" + getNumber(i) + "###########################################################################################################\n\n");
            String path = "./resources/conErrores/sintError" + getNumber(i) + ".java";
            analyze(path);
        }
    }

    private static String getNumber(int i){
        return i > 9 ? "" + i : "0" + i;
    }

    private static void analyze(String sourceFilePath) {
        try {
            SourceFileManager sourceFileManager = new SourceFileManager(sourceFilePath);
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceFileManager);
            new SyntacticAnalyzer(lexicalAnalyzer);
            System.out.println("[SinErrores]");
        } catch (FileNotFoundException exception) {
            System.out.println("Archivo no encontrado");
        } catch (IOException exception){
            System.out.println("Error de IO");
        } catch (SyntacticException e) {
            System.out.println(e.getMessage());
        }
    }
}
