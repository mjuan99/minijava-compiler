//Prueba de declaracion de clases e interfaces
class MiClase{ }

class MiClase extends MiClase{ }

class MiClase implements MiInterfaz{ }

class MiClase implements MiInterfaz, MiInterfaz{ }

class MiClase implements MiInterfaz, MiInterfaz, MiInterfaz{ }

class MiClase extends MiClase implements MiInterfaz{ }

class MiClase extends MiClase implements MiInterfaz, MiInterfaz{ }

class MiClase extends MiClase implements MiInterfaz, MiInterfaz, MiInterfaz{ }

interface MiInterfaz{ }

interface MiInterfaz extends MiInterfaz{ }

interface MiInterfaz extends MiInterfaz, MiInterfaz{ }

interface MiInterfaz extends MiInterfaz, MiInterfaz, MiInterfaz{ }