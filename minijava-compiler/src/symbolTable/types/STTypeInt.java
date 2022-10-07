package symbolTable.types;

public class STTypeInt implements STPrimitiveType{
    public String toString(){
        return "int";
    }

    public boolean checkDeclaration() {
        return true;
    }

    @Override
    public boolean conformsWith(STType stType) {
        return stType instanceof STTypeInt;
    }

    public boolean equals(STType stType){
        return stType instanceof STTypeInt;
    }

    @Override
    public boolean isTypeReference() {
        return false;
    }
}
