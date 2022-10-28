package codeGenerator;

public class CodeGenerator {
    public String generateCode(int i){
        return ".code\n" +
                "push " + i + "\n" +
                "iprint\n" +
                "halt";
    }
}
