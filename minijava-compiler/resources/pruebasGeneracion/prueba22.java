//8
//3
//9

class A{
    public int x;
    static void main(){
        var a = new A();
        var b = new B();
        a = b;
        a.m1(9);
        System.printIln(b.getXA());
        a.x = 3;
        System.printIln(b.getXA());
        System.printIln(b.getXB());
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
        super.x = i-1;
    }

    int getXB(){
        return x;
    }
}