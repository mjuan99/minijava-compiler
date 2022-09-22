package symbolTable.types;

public class STTypeBoolean implements STPrimitiveType{
    public String toString(){
        return "boolean";
    }

    public void checkDeclaration() {
    }

    public boolean equals(STType stType){
        return stType instanceof STTypeBoolean;
    }
}
