package servidor.logic;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListaUsuarios implements Serializable {
    private Set<Usuario> lista;

    public ListaUsuarios(Map<String, Usuario> mapa) {
        Set<Usuario> lista = new HashSet<>();
        for (Usuario user : mapa.values()) {
            lista.add(user);
        }
        this.lista = lista;
    }

    public Set<Usuario> getUsuarios() {
        return lista;
    }
}
