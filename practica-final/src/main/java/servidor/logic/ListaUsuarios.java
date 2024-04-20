package servidor.logic;

import java.util.Collection;
import java.util.Map;

public class ListaUsuarios implements Serializable {
    private Collection<Usuario> lista;

    public ListaUsuarios(Map<String, Usuario> mapa) {
        this.lista = mapa.values();
    }

    public Collection<Usuario> getUsuarios() {
        return lista;
    }
}
