package codeGenerator;

import symbolTable.ST;

import java.util.Scanner;

public class CodeGenerator {
    public static String code = "";
    public static String tagMain;
    public static String tagMalloc;
    private static boolean newLine = true;

    public String generateCode(boolean formatCode){
        String tagHeapInit = TagManager.getTag("heapInit");
        tagMalloc = TagManager.getTag("malloc");
        tagMain = TagManager.getTag("main");
        generateCode(".code");
        generateCode("PUSH " + tagHeapInit);
        generateCode("CALL ;llamada a heapInit");
        generateCode("PUSH " + tagMain);
        generateCode("CALL ;llamada a main");
        generateCode("HALT ;finalizacion del programa");

        generateCode(tagHeapInit + ": RET 0");

        generateCode(tagMalloc + ": LOADFP ;Inicialización unidad");
        generateCode("LOADSP");
        generateCode("STOREFP ;Finaliza inicialización del RA");
        generateCode("LOADHL ;hl");
        generateCode("DUP ;hl");
        generateCode("PUSH 1 ;1");
        generateCode("ADD ;hl+1");
        generateCode("STORE 4 ;Guarda el resultado (un puntero a la primer celda de la región de memoria)");
        generateCode("LOAD 3 ;Carga la cantidad de celdas a alojar (parámetro que debe ser positivo)");
        generateCode("ADD");
        generateCode("STOREHL ;Mueve el heap limit (hl). Expande el heap");
        generateCode("STOREFP");
        generateCode("RET 1 ;Retorna eliminando el parámetro");

        ST.symbolTable.generateCode();
        if(formatCode)
            return format(code);
        else
            return code;
    }

    public static void generateCode(String instruction){
        if(newLine)
            code += instruction;
        else
            code += "\n" + instruction;
        newLine = false;
    }

    public static void setNextInstructionTag(String tag){
        if(newLine)
            code += tag + ": ";
        else
            code += "\n" + tag + ": ";
        newLine = true;
    }

    public static void setComment(String comment){
        code += " ;" + comment;
        newLine = false;
    }

    private static String format(String code){
        return formatComments(formatTags(code));
    }

    private static String formatTags(String code){
        StringBuilder formattedCode = new StringBuilder();
        int maxTagIndex = 0;
        Scanner scanner = new Scanner(code);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            int tagIndex = getCharacterIndex(line, ':');
            if(tagIndex != -1)
                maxTagIndex = Math.max(tagIndex, maxTagIndex);
        }
        scanner = new Scanner(code);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String formattedLine;
            int tagIndex = getCharacterIndex(line, ':');
            if(tagIndex != -1){
                formattedLine = line.substring(0, tagIndex + 1) + getSpaces(maxTagIndex - tagIndex) + line.substring(tagIndex + 1);
            }
            else
                formattedLine = getSpaces(maxTagIndex + 2) + line;
            formattedCode.append(formattedLine).append("\n");
        }
        return formattedCode.toString();
    }

    private static String formatComments(String code){
        StringBuilder formattedCode = new StringBuilder();
        int maxCommentIndex = 0;
        Scanner scanner = new Scanner(code);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            int commentIndex = getCharacterIndex(line, ';');
            if(commentIndex != -1)
                maxCommentIndex = Math.max(commentIndex, maxCommentIndex);
        }
        scanner = new Scanner(code);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String formattedLine;
            int commentIndex = getCharacterIndex(line, ';');
            if(commentIndex != -1){
                formattedLine = line.substring(0, commentIndex) + getSpaces(maxCommentIndex - commentIndex + 10) + line.substring(commentIndex);
            }
            else
                formattedLine = line;
            formattedCode.append(formattedLine).append("\n");
        }
        return formattedCode.toString();
    }

    private static int getCharacterIndex(String line, char character){
        char lastSpecialCharacter = '\0';
        for(int i = 0; i < line.length(); i++){
            if(lastSpecialCharacter == '\0') {
                if(line.charAt(i) == character)
                    return i;
                else if(line.charAt(i) == '\'')
                    lastSpecialCharacter = '\'';
                else if(line.charAt(i) == '"')
                    lastSpecialCharacter = '"';
            }
            else if(lastSpecialCharacter == '\'' && line.charAt(i) == '\'')
                lastSpecialCharacter = '\0';
            else if(lastSpecialCharacter == '"' && line.charAt(i) == '"')
                lastSpecialCharacter = '\0';
        }
        return -1;
    }

    private static String getSpaces(int n){
        return " ".repeat(n);
    }
}
