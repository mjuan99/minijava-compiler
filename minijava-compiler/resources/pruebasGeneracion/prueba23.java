//m1 en B
//m1 en A
//7
//5

class A{
    public int x;
    static void main(){
        var b = new B();
        b.m1();
        System.printIln(b.getXA());
        System.printIln(b.getXB());
    }

    int getXA(){
        return x;
    }

    void m1(){
        System.printSln("m1 en A");
    }
}

class B extends A{
    public int x;

    void m1(){
        x = 5;
        super.x = 7;
        System.printSln("m1 en B");
        super.m1();
    }

    int getXB(){
        return x;
    }
}