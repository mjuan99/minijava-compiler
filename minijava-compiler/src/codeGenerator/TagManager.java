package codeGenerator;

public class TagManager {
    private static long tag = 0;

    public static String getTag(){
        return "t" + tag++;
    }
}
