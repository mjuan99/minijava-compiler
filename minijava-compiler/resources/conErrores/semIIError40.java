//[Error:A|8]

class A{
    A m1(){
        return new A();
    }
    A m2(){
        return new A(1, 2);
    }
    static void main(){}
}