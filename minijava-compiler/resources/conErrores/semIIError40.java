//[Error:A|6]

class A{
    A m1(){
        return new A();
        return new A(1, 2);
    }
    static void main(){}
}