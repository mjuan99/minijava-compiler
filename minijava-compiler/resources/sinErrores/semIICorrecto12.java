class A{
    public I3b i3b;
    public C1 c1;
    public C2 c2;
    public C3 c3;
    I4a m1a(){
        return i3b;
    }
    I4a m1b(){
        return c3;
    }
    I3b m2a(){
        return i3b;
    }
    I3b m2b(){
        return c3;
    }
    I1b m3a(){
        return c1;
    }
    I1b m3b(){
        return c2;
    }
    I1b m3c(){
        return c3;
    }
    C1 m4a(){
        return c1;
    }
    C1 m4b(){
        return c2;
    }
    C1 m4c(){
        return c3;
    }
    I2a m5a(){
        return c2;
    }
    I2a m5b(){
        return c3;
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