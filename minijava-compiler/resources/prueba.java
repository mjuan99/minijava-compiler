//prueba de consolidación clases (interfaces implementadas)

class MiClase{
    int m1(char a, String b){}
    void m2(boolean c, MiClase d){}
    MiClase m2(){}
    String m4(){}
    static void m4(char g, String h){}
}

class MiClase2 extends MiClase{
    char m1(){}
    void m2(boolean e, MiClase f){}
    int m2(){}
    static String m4(){}
    void m4(char i, String j){}

    static void main(){}
}