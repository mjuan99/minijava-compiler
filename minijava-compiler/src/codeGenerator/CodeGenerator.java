package codeGenerator;

import symbolTable.ST;

public class CodeGenerator {
    public static String code = "";
    public static String tagMain;

    public String generateCode(){
        String tagHeapInit = TagManager.getTag();
        tagMain = TagManager.getTag();
        generateCode(".code");
        generateCode("PUSH " + tagHeapInit);
        generateCode("CALL");
        generateCode("PUSH " + tagMain);
        generateCode("CALL");
        generateCode("HALT");

        generateCode(tagHeapInit + ": RET 0");
        ST.symbolTable.generateCode();
        return code;
    }

    public static void generateCode(String instruction){
        code += instruction + "\n";
    }

    public static void setNextInstructionTag(String tag){
        code += tag + ": ";
    }
}
