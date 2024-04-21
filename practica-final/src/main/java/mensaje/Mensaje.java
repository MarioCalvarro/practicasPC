package mensaje;

import java.io.Serializable;

public abstract class Mensaje implements Serializable {
    protected TipoMensaje msj;

    public TipoMensaje getTipo() {
        return msj;
    }
}
