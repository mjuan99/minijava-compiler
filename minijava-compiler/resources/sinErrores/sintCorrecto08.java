//Prueba de accesos
class MiClase{
    int miMetodo(){
        this;
        this.miVariable;
        this.miVariable.miMetodo();

        miVariable;
        miVariable.miMetodo((a));
        miVariable.miMetodo(a, b).miVariable;

        new MiClase();
        new MiClase().miMetodo(a >= b, c);
        new MiClase().miMetodo(a, b, c).miMetodo(a+b+c+d+e+f);

        miMetodo();
        miMetodo(a).miMetodo(b, 'c');
        miMetodo(25).miVariable;

        MiClase.metodoEstatico();
        MiClase.metodoEstatico(null);
        MiClase.metodoEstatico(a, (b+c) != d);

        (a > b);
        (a + b * c).miMetodo("asd");
        (a + b * c).miMetodo(true).miMetodo(false);
    }
}