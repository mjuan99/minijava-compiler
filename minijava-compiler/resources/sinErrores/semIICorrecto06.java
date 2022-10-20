//prueba de conformacion con tipos referencia y llamados a metodos

class A{
    public I3b i3b;
    public C1 c1;
    public C2 c2;
    public C3 c3;
    I4a m1(I4a a1, I4a a2, I4a a3){
        return m1(i3b, c3, null);
    }
    I3b m2(I3b a1, I3b a2, I3b a3){
        return m2(i3b, c3, null);
    }
    I1b m3(I1b a1, I1b a2, I1b a3, I1b a4){
        return m3(c1, c2, c3, null);
    }
    C1 m4(C1 a1, C1 a2, C1 a3, C1 a4){
        return m4(c1, c2, c3, null);
    }
    I2a m5(I2a a1, I2a a2, I2a a3){
        return m5(c2, c3, null);
    }
    static void main(){}
}

interface I1a{}
interface I1b{}
interface I2a{}
interface I2b{}
interface I3a{}
interface I3b extends I4a, I4b{}
interface I4a{}
interface I4b{}
class C1 implements I1a, I1b{}
class C2 extends C1 implements I2a, I2b{}
class C3 extends C2 implements I3a, I3b{}