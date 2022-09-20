class MiClase1 extends MiClase2{
    int a1;
    char a2, a3, a4;
    boolean m1(String a, int b, MiClase c){}
    int m2(){}

}
class MiClase2 implements MiInterfaz1, MiInterfaz2, MiInterfaz3{}
class MiClase3 extends MiClase2 implements MiInterfaz1, MiInterfaz2, MiInterfaz3{}
interface MiInterfaz1{
}
interface MiInterfaz2 extends MiInterfaz1, MiInterfaz2, MiInterfaz3{}