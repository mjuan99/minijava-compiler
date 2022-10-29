package codeGenerator;

public class TagManager {
    private static long tagNumber = 0;

    public static String getTag(String tagPrefix){
        return tagPrefix + "$" + tagNumber++;
    }
}
