//[Error:m1|12]

interface MiInterfaz1 extends MiInterfaz2{
    MiClase m1();
}

interface MiInterfaz2{
    void m2();
}

class MiClase implements MiInterfaz1{
    MiInterfaz2 m1(){}
    void m2(){}
}