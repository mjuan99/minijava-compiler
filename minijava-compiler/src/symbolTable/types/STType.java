package symbolTable.types;

public interface STType {
    void checkDeclaration();
    boolean equals(STType stType);
}
