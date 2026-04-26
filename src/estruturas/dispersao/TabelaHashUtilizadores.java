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

    /**
     * Percorre todos os 26 buckets da tabela hash e recolhe
     * todos os utilizadores num único array.
     * Necessário para listar/ordenar utilizadores na interface.
     */
    public Utilizador[] listarTodos() {
        // 1ª passagem: contar o total de utilizadores
        int total = 0;
        for (int i = 0; i < NUM_CATEGORIAS; i++) {
            total += tabelaHash[i].tamanho();
        }

        // 2ª passagem: preencher o array resultado
        Utilizador[] resultado = new Utilizador[total];
        int pos = 0;
        for (int i = 0; i < NUM_CATEGORIAS; i++) {
            ListaDuplamenteLigada lista = tabelaHash[i];
            for (int j = 0; j < lista.tamanho(); j++) {
                resultado[pos++] = (Utilizador) lista.pega(j);
            }
        }

        return resultado;
    }
}
