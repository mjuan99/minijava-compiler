package codeGenerator;

import java.util.HashMap;

public class TagManager {
    private static final HashMap<String, Integer> tags = new HashMap<>();

    public static String getTag(String tagPrefix){
        if(tagPrefix == null)
            tagPrefix = "";
        Integer tagNumber = tags.get(tagPrefix);
        if(tagNumber == null){
            tagNumber = 1;
            tags.put(tagPrefix, tagNumber);
        }else{
            tagNumber++;
            tags.put(tagPrefix, tagNumber);
        }
        return tagPrefix + "$" + tagNumber;
    }
}
