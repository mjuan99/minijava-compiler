package symbolTable.types;

public interface STType {
    boolean checkDeclaration();
    boolean equals(STType stType);

    boolean isTypeReference();

    boolean conformsWith(STType stType);
}
