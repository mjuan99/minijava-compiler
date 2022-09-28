interface I1{
    void m1();
}

interface I2 extends I1{
    int m2(char a);
}

class A{
    void m1(){}
}

class B extends A{
    int m2(char a){}
}

class C extends B implements I2{
    static void main(){}
}