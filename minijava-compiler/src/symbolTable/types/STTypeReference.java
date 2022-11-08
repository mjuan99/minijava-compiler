package symbolTable.types;

import errors.SemanticError;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.entities.STClass;
import symbolTable.entities.STInterface;

import java.util.Objects;

public class STTypeReference implements STType{
    private final Token reference;

    public STTypeReference(Token reference){
        this.reference = reference;
    }

    public String toString(){
        return reference.getLexeme();
    }

    public boolean checkDeclaration(){
        if(!ST.symbolTable.stClassExists(reference.getLexeme()) && !ST.symbolTable.stInterfaceExists(reference.getLexeme())) {
            ST.symbolTable.addError(new SemanticError(reference, "la clase " + reference.getLexeme() + " no fue declarada"));
            return false;
        }else
            return true;
    }

    @Override
    public boolean conformsWith(STType stType) {
        if(!(stType instanceof STTypeReference))
            return reference.getLexeme().equals("String") && (stType instanceof STTypeChar || stType instanceof STTypeInt);
        else
            if(Objects.equals(stType.toString(), "Object"))
                return true;
            else
                return isSubtype(reference.getLexeme(), stType.toString());
    }

    private boolean isSubtype(String subType, String parentType){
        if(Objects.equals(subType, parentType))
            return true;
        else if(Objects.equals(subType, "Object"))
            return false;
        else {
            STClass subTypeClass = ST.symbolTable.getSTClass(subType);
            if(subTypeClass != null)
                if(isSubtype(subTypeClass.getTKClassItExtends().getLexeme(), parentType))
                    return true;
                else
                    for(String interfaceName : subTypeClass.getInterfacesItImplements()) {
                        if (isSubtype(interfaceName, parentType))
                            return true;
                    }
            else{
                STInterface subTypeInterface = ST.symbolTable.getSTInterface(subType);
                for(Token tkInterface : subTypeInterface.getTKInterfacesItExtends().values()) {
                    if (isSubtype(tkInterface.getLexeme(), parentType))
                        return true;
                }
            }
        }
        return false;
    }

    public boolean equals(STType stType){
        if(stType instanceof STTypeReference){
            return Objects.equals(reference.getLexeme(), ((STTypeReference) stType).reference.getLexeme());
        }else
            return false;
    }

    @Override
    public boolean isTypeReference() {
        return true;
    }
}
