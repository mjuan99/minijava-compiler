//[Error:=|38]

class A{
    public I3b i3b;
    public C1 c1;
    public C2 c2;
    public C3 c3;
    I4a m1(){
        var x = m1();
        x = i3b;
        x = c3;
        return x;
    }
    I3b m2(){
        var x = m2();
        x = i3b;
        x = c3;
        return x;
    }
    I1b m3(){
        var x = m3();
        x = c1;
        x = c2;
        x = c3;
        return x;
    }
    C1 m4(){
        var x = m4();
        x = c1;
        x = c2;
        x = c3;
        return x;
    }
    I2a m5(){
        var x = m5();
        x = c2;
        x = c3;
        x = c1;
        return x;
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