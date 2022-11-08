//prueba asignacion

class A{
    public int a1;
    public A a2;
    public B a3;

    A m1(){
        a1 = 7 * 3 / a1;
        m1().m1().a1 += a1;
        a1 -= 4 % 3 + a1;
        this.m1().a2 = m1().m1().a3;
        m1().a3 = m1().m1().a3;
        new A().a1 = 3;
        return new A();
    }

    static void main(){}
}

class B extends A{}