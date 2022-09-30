//[Error:m1|10]

interface I1{
    void m1();
}

interface I2 extends I1{}

class A{
    int m1(){}
}

class B extends A{}

class C extends B implements I2{
    static void main(){}
}