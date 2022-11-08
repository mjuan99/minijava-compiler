//[Error:-=|6]

class A{
    public String a;
    void m1(){
        a -= new A();
    }

    static void main(){}
}

class B{}