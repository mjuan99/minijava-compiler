//Prueba de genericidad
class MiClase{

}

class MiClase<A> extends MiClase<A,B> implements MiClase, Miclase<A, B<C<D<E>,F>>, G>{
    MiClase<A<B>> miMetodo(){

        new MiClase<A, B>();
        new MiClase<>();
        MiClase<A>.metodoEstatico();
    }
}

interface MiInterfaz<A, B, C> extends MiInterfaz, MiInterfaze<D>{}