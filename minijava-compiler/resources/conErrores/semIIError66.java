//[Error:x|6]

class Main{
    static void main(){
        var a = m1();
        var x = a.x;
    }

    static A m1(){
        return new B();
    }
}

class A{
    private int x;
}