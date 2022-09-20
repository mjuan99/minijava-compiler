package symbolTable;

import java.util.HashMap;

public class STMethod {
    private String name;
    private boolean isStatic;
    private STType returnType;
    private HashMap<String, STArgument> stArguments;
    //private STBlock block;

    public void print() {
        System.out.println("    " + (isStatic ? "static " : "") + returnType + " " + name);
        stArguments.forEach((key, stArgument) -> {
            stArgument.print();
        });
    }

}
