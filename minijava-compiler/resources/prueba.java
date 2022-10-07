// Prueba un lado izquierdo simple

class A {
    int a(){}
    C c(){}
    B b(){}
    A m1(){
        return b();
    }
    static void main(){}
}

class B extends A{}

class C{}


