//prueba de consolidación clases (interfaces implementadas)

interface MiInterfaz1 extends MiInterfaz2{
    MiClase m1();
}

interface MiInterfaz2{
    void m2();
}

class MiClase implements MiInterfaz1{
    MiClase m1(){}
    static void m2(){}
    static void main(){}
}