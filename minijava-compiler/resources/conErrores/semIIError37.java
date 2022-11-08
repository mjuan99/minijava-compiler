//[Error:m1|8]

class A{
    int m1(){}
    static int m2(){}
    static void main(){
        m2();
        m1();
    }
}