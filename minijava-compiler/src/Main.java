import codeGenerator.CodeGenerator;
import errors.SemanticException;
import errors.SyntacticException;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.sourceFileManager.SourceFileManager;
import semanticAnalyzer.SemanticAnalyzer;
import syntacticAnalyzer.SyntacticAnalyzer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public final static int ERR_ARGUMENTO_FALTANTE = 1;
    public final static boolean GENERATE_CODE = true;
    public final static boolean RUN_VM = true;
    public final static boolean FORMAT_CODE = true;

    public static void main(String[] args){
        if(args.length == 0) {
            System.out.println("Argumento faltante");
            System.exit(ERR_ARGUMENTO_FALTANTE);
        }
        String code = generateCode(args[0]);
        if(!code.equals("")) {
            Path path;
            if (args.length == 1)
                path = Path.of("default.txt");
            else
                path = Path.of(args[1]);
            try {
                if (path.getParent() != null)
                    Files.createDirectories(path.getParent());
                Files.writeString(path, code);
                if (RUN_VM)
                    RunVM.main(new String[]{path.toString()});
            } catch (IOException e) {
                System.out.println("Error de IO");
            }
        }
    }

    private static String generateCode(String sourceFilePath) {
        try {
            SourceFileManager sourceFileManager = new SourceFileManager(sourceFilePath);
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceFileManager);
            SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer);
            new SemanticAnalyzer(syntacticAnalyzer);
            System.out.println("[SinErrores]");
            if(GENERATE_CODE)
                return new CodeGenerator().generateCode(FORMAT_CODE);
            else
                System.exit(0);
        } catch (FileNotFoundException exception) {
            System.out.println("Archivo no encontrado");
        } catch (IOException exception){
            System.out.println("Error de IO");
        } catch (SyntacticException | SemanticException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
