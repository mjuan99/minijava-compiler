package symbolTable.types;

public class STTypeInt implements STPrimitiveType{
    public String toString(){
        return "int";
    }

    public void checkDeclaration() {
    }

    public boolean equals(STType stType){
        return stType instanceof STTypeInt;
    }
}
