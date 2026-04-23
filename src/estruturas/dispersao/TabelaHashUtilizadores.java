package estruturas.dispersao;


import estruturas.lineares.ListaDuplamenteLigada;
import modelo.Utilizador;

public class TabelaHashUtilizadores {
    private ListaDuplamenteLigada[] tabelaHash;
    private final int NUM_CATEGORIAS = 26;

    public TabelaHashUtilizadores() {
        tabelaHash = new ListaDuplamenteLigada[NUM_CATEGORIAS];
        for (int i = 0; i < NUM_CATEGORIAS; i++) {
            tabelaHash[i] = new ListaDuplamenteLigada();
        }
    }

    private int codigoHash(String chave) {
        return Math.abs(chave.toLowerCase().charAt(0) % NUM_CATEGORIAS);
    }

    public void adiciona(Utilizador u) {
        int indice = codigoHash(u.getUsername());
        tabelaHash[indice].adicionaFim(u);
    }

    public Utilizador busca(String username) {
        int indice = codigoHash(username);
        ListaDuplamenteLigada lista = tabelaHash[indice];

        for (int i = 0; i < lista.tamanho(); i++) {
            Utilizador u = (Utilizador) lista.pega(i);
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
}