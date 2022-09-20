package symbolTable;

public class STArgument {
    private String name;
    private STType type;

    public void print() {
        System.out.println("        " + type + " " + name);
    }
}
