//[Error:a|5]

class A{
    int m1(){
        return B.a();
    }

    static void main(){}
}

class B{
    int a(){}
}