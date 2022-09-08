//Prueba de constructores y visibilidad implicita
class MiClase{
    public int a, b, c;
    private boolean d, e;
    private MiClase f;

    static boolean miMetodo(){}
    int miMetodo(){}
    char miMetodo(int a, char b, MiClase c){}
    void miMetodo(MiClase a){}

    boolean a, b;
    int c;
    char d;
    MiClase e, f, g;
    MiClase(){
        return new MiClase();
    }
    MiClase(int a, char b, MiClase c){
        return new MiClase(a, b, new MiClase());
    }
    MiClase miMetodo(){

    }
}