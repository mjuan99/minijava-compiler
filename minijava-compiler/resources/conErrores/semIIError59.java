//[Error:m2|15]

class A{
    void m1(){
        var x = 7;
        {
            {
                {
                    return;
                }
            }
        }
    }

    void m2(){
        var x = 7;
        {
            {
                {
                    return;
                }
            }
            var x = 5;
        }
    }

    static void main(){}
}