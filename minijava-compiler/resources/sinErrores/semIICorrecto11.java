//prueba declaraciones de variables

class A{
    public String a;
    A m1(){
        var a = 3 * 7 + 1;
        var b = true || false && !false;
        var c = "asd";
        var d = this.a;
        var e = new A().m1();
        var f = m1();
        var g = (1 >= 3);
    }

    static void main(){}
}

class B extends A{}