//[Error:m2|5]

class A{
    int m1(){
        I.m2();
    }
    static void main(){}
}

interface I{
    int m2();
}