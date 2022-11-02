class A{
    public int x;
    static void main(){
        var a = new A();
        var b = new B();
        a = b;
        a.m1(9);
        System.printIln(a.x);
        System.printIln(a.getXA());
    }

    int getXA(){
        return x;
    }

    void m1(int i){
        x = i;
    }
}

class B extends A{
    public int x;

    void m1(int i){
        x = i;
        super.y = 7;
        super.m1(i);
    }
}