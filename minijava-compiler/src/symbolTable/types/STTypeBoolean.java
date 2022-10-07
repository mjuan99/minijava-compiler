package symbolTable.types;

public class STTypeBoolean implements STPrimitiveType{
    public String toString(){
        return "boolean";
    }

    public boolean checkDeclaration() {
        return true;
    }

    public boolean equals(STType stType){
        return stType instanceof STTypeBoolean;
    }

    @Override
    public boolean conformsWith(STType stType) {
        return stType instanceof STTypeBoolean;
    }

    @Override
    public boolean isTypeReference() {
        return false;
    }
}
