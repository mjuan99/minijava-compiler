class MiClase1 extends MiClase2{
    MiClase1 a1;
    MiClase2 a2, a3, a4;
    MiClase3 m1(){}
    MiClase4 m2(int a, String b, MiClase5 c){}
    MiClase(char d, MiInterfaz e){}

}
class MiClase2 implements MiInterfaz1, MiInterfaz2, MiInterfaz3{}
class MiClase3 extends MiClase2 implements MiInterfaz1, MiInterfaz2, MiInterfaz3{}
interface MiInterfaz1{
}
interface MiInterfaz2 extends MiInterfaz1, MiInterfaz2, MiInterfaz3{}