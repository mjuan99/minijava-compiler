import Errors.CompilerException;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.sourceFileManager.SourceFileManager;
import syntacticAnalyzer.SyntacticAnalyzer;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public final static int ERR_ARGUMENTO_FALTANTE = 1;
    public final static int ERR_ARCHIVO_NO_ENCONTRADO = 2;
    public final static int ERR_IO = 3;

    public static void main(String[] args){
        if(args.length == 0) {
            System.out.println("Argumento faltante");
            System.exit(ERR_ARGUMENTO_FALTANTE);
        }
        //analyzeAllErrorFiles();
        String sourceFilePath = args[0];
        analyze(sourceFilePath);
    }

    private static void analyzeAllErrorFiles() {
        for(int i = 1; i <= 55; i++){
            System.out.println(getNumber(i) + "--------------------------------------------------------------------\n");
            String path = "./resources/conErrores/sintError" + getNumber(i) + ".java";
            analyze(path);
            System.out.println("----------------------------------------------------------------------\n");
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
            System.exit(ERR_ARCHIVO_NO_ENCONTRADO);
        } catch (IOException exception){
            System.out.println("Error de IO");
            System.exit(ERR_IO);
        } catch (CompilerException e) {
            System.out.println(e.getMessage());
        }
    }
}
