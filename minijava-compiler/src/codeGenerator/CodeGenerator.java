package codeGenerator;

import symbolTable.ST;

public class CodeGenerator {
    public static String code;
    public static String tagMain;
    public String generateCode(){
        String tagHeapInit = TagManager.getTag();
        tagMain = TagManager.getTag();
        code = ".code\n" +
                "PUSH " + tagHeapInit + "\n" +
                "CALL\n" +
                "PUSH " + tagMain + "\n" +
                "CALL\n" +
                "HALT\n";

        code += tagHeapInit + ": RET 0\n";
        ST.symbolTable.generateCode();
        return code;
    }
}
