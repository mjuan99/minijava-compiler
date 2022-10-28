import codeGenerator.CodeGenerator;
import errors.SemanticException;
import errors.SyntacticException;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.sourceFileManager.SourceFileManager;
import semanticAnalyzer.SemanticAnalyzer;
import symbolTable.ST;
import syntacticAnalyzer.SyntacticAnalyzer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public final static int ERR_ARGUMENTO_FALTANTE = 1;
    public final static boolean DEBUG = true;

    public static void main(String[] args){
        if(args.length == 0) {
            System.out.println("Argumento faltante");
            System.exit(ERR_ARGUMENTO_FALTANTE);
        }
        String code = generateCode(args[0]);
        Path path;
        if(args.length == 1)
            path = Path.of("default.txt");
        else
            path = Path.of(args[1]);
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, code);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error de IO");
        }
        if(DEBUG)
            ExecuteVM.main(new String[]{path.toString()});
    }

    private static String generateCode(String sourceFilePath) {
        try {
            SourceFileManager sourceFileManager = new SourceFileManager(sourceFilePath);
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceFileManager);
            SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer);
            new SemanticAnalyzer(syntacticAnalyzer);
            if(DEBUG)
                ST.symbolTable.print();
            System.out.println("[SinErrores]");
            return new CodeGenerator().generateCode();
        } catch (FileNotFoundException exception) {
            System.out.println("Archivo no encontrado");
        } catch (IOException exception){
            System.out.println("Error de IO");
        } catch (SyntacticException | SemanticException e) {
            if(DEBUG)
                ST.symbolTable.print();
            System.out.println(e.getMessage());
        }
        return "";
    }
}
