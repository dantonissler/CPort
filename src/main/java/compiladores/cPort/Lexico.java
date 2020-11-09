package compiladores.cPort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author danton.issler
 * @since 08/11/2020
 */
public class Lexico {
	public List<String> dicionario;
	public List<String> codigoFonte;
	public List<String> tokens;

	public void AnalisarLexico(String nome) throws IOException {
		// ler arquivo do dicionario_exp
		// ler exemplo
		// separar o exemplo por token
		// procurar uma expressao do dicionario que valide o token
		// se houver enviar mensagem de erro
		// se todos houverem enviar mensagem de sucesso
		Arquivo arq_dic = new Arquivo("dicionario_exp.txt");
		this.dicionario = arq_dic.lerConteudo();
		Arquivo arq_fonte = new Arquivo(nome);
		this.codigoFonte = arq_fonte.lerConteudo();
		this.tokens = separarTokens(); // faz a separacao dos tokens
		Integer quantidadeErros = validarTokens();
		System.out.println("Total de erros: " + quantidadeErros);
		/*
		 * System.out.println("------------------------Dicionario"); for(int
		 * i=0;i < dicionario.size(); i++){ // percorrendo o conteudo
		 * System.out.println(dicionario.get(i)); }
		 * System.out.println("------------------------Fonte"); for(int i=0;i <
		 * codigoFonte.size(); i++){ // percorrendo o conteudo
		 * System.out.println(codigoFonte.get(i)); }
		 */

	}

	public List<String> separarTokens() {
		// preparara a lista de tokens
		List<String> tks = new ArrayList<String>();
		// percorrer o codigo fonte e separar os tokens de cada linha
		for (int i = 0; i < codigoFonte.size(); i++) { // percorrendo o conteudo
			String linha = codigoFonte.get(i);
			// processo de remover espacos
			linha = trocarEspacoPorPonto(linha);
			String[] palavras = linha.split(" ");
			for (int j = 0; j < palavras.length; j++) {
				if (!palavras[j].startsWith("\""))
					tks.add(palavras[j]);
			}
		}
		/*
		 * for(int i=0;i< tks.size();i++){ System.out.println(tks.get(i)); }
		 */
		return tks;
	}

	public String trocarEspacoPorPonto(String linha) {
		// se estiver entre aspas deve mudar o espaco para ponto
		if (linha.contains("\"")) {
			Integer posicaoInicial = linha.indexOf('"');
			Integer posicaoFinal = linha.lastIndexOf("\"") + 1;
			String texto = linha.substring(posicaoInicial, posicaoFinal);
			texto = texto.replace(" ", ".");
			linha = linha.substring(0, posicaoInicial - 1) + " " + texto;
		}
		return linha;
		/*
		 * mostre "Ola povo" texto = "Ola povo" texto = "Ola.povo" texto =
		 * "mostre Ola.Povo"
		 */

	}

	public Integer validarTokens() {

		int quantidadeErros = 0;
		int i;
		for (i = 0; i < this.tokens.size(); i++) {
			// procurar uma expressao regular que a valide
			String palavra = this.tokens.get(i);
			Boolean valido = false;
			for (int j = 0; j < this.dicionario.size(); j++) {
				String expressao = this.dicionario.get(j);
				if (palavra.matches(expressao)) {
					valido = true;
					break;
				}
			}
			if (!valido) {
				System.out.println("Token " + palavra + " invalido no termo:" + i);
				quantidadeErros++;
			} else {
				System.out.println("Token correto.." + palavra);
			}
		}
		return quantidadeErros;
	}
}
