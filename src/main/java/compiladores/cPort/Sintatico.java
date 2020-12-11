package compiladores.cPort;

import java.io.IOException;
import java.util.List;

public class Sintatico {
	public List<String> codigoFonte;
	public List<String> gramatica;
	public int quantidade = 0;

	public void analisarSintatico(String nomeFonte) throws IOException {
		Arquivo arq_gramatica = new Arquivo("gramatica_exp.txt");
		this.gramatica = arq_gramatica.lerConteudo();
		Arquivo arq_fonte = new Arquivo(nomeFonte);
		this.codigoFonte = arq_fonte.lerConteudo();
		Boolean correto = false;
		for (String linha : codigoFonte) {
			correto = false;
			for (String grama : gramatica) {
				if (linha.matches(grama)) {
					if (linha.matches(grama)) {
						correto = true;
						System.out.println("Linha: " + linha + " -> ok");
						break;
					}
				}
			}
			if (!correto) {
				System.err.println("Erro de sintaxe an Linha: " + linha);
				quantidade++;
			}
		}
		System.err.println("Quantidade erros: " + quantidade);
	}
}
