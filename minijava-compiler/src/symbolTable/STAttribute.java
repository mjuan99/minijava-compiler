package symbolTable;

public class STAttribute {
    private String name;
    private STType type;
    private String visibility;

    public void print() {
        System.out.println("    " + visibility + " " + type + " " + name);
    }
}
