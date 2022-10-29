package codeGenerator;

import symbolTable.ST;

import java.util.Scanner;

public class CodeGenerator {
    public static String code = "";
    public static String tagMain;
    private static boolean newLine = true;

    public String generateCode(boolean formatCode){
        String tagHeapInit = TagManager.getTag("heapInit");
        tagMain = TagManager.getTag("main");
        generateCode(".code");
        generateCode("PUSH " + tagHeapInit);
        generateCode("CALL");
        generateCode("PUSH " + tagMain);
        generateCode("CALL");
        generateCode("HALT");

        generateCode(tagHeapInit + ": RET 0");
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
        int maxTag = 0;
        Scanner scanner = new Scanner(code);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.contains(":"))
                maxTag = Math.max(line.indexOf(':'), maxTag);
        }
        scanner = new Scanner(code);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String formattedLine;
            if(line.contains(":")){
                int tag = line.indexOf(':');
                formattedLine = line.substring(0, tag+1) + getSpaces(maxTag - tag) + line.substring(tag+1);
            }
            else
                formattedLine = getSpaces(maxTag + 2) + line;
            formattedCode.append(formattedLine).append("\n");
        }
        return formattedCode.toString();
    }

    private static String formatComments(String code){
        StringBuilder formattedCode = new StringBuilder();
        int maxComment = 0;
        Scanner scanner = new Scanner(code);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.contains(";"))
                maxComment = Math.max(line.indexOf(';'), maxComment);
            else
                maxComment = Math.max(line.length(), maxComment);
        }
        scanner = new Scanner(code);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String formattedLine;
            if(line.contains(";")){
                int comment = line.indexOf(';');
                formattedLine = line.substring(0, comment) + getSpaces(maxComment - comment + 2) + line.substring(comment);
            }
            else
                formattedLine = line;
            formattedCode.append(formattedLine).append("\n");
        }
        return formattedCode.toString();
    }

    private static String getSpaces(int n){
        return " ".repeat(n);
    }
}
