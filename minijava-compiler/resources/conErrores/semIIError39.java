//[Error:return|29]

class A{
    public I3b i3b;
    public C1 c1;
    public C2 c2;
    public C3 c3;
    I4a m1(){
        return i3b;
        return c3;
    }
    I3b m2(){
        return i3b;
        return c3;
    }
    I1b m3(){
        return c1;
        return c2;
        return c3;
    }
    C1 m4(){
        return c1;
        return c2;
        return c3;
    }
    I2a m5(){
        return c2;
        return c3;
        return c1;
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