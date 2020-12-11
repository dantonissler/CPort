package compiladores.cPort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @since 08/11/2020
 */
public class Semantico {
	private List<String> codigoFonte;
	private List<String> tabelaSimbolos;
	private String token = "";

	public void analisarSemantico(String nomeFonte) throws IOException {
		int erros = 0;
		int numeroLinha = 0;
		Arquivo arq_fonte = new Arquivo(nomeFonte);
		this.codigoFonte = arq_fonte.lerConteudo();
		tabelaSimbolos = new ArrayList<String>();
		String instrucao = "";
		for (String linha : this.codigoFonte) {
			System.out.println(linha);
			instrucao = linha.trim().split(" ")[0]; // pega a priemira palavra
													// da linha
			erros += validarVariavel(linha, instrucao, numeroLinha);
			erros += validarMostrar(linha, instrucao, numeroLinha);
			// System.out.println(" (fim)");
			numeroLinha++;
		}
		System.err.println("Quantidade de erros: " + erros);
	}

	public String separarVariavel(String linha) {
		int i;
		String[] palavras = linha.split(" ");
		for (i = 0; i < palavras.length; i++) {
			if (palavras[i].startsWith("$")) {
				return palavras[i];
			}
		}
		return "()";
	}

	public int validarVariavel(String linha, String instrucao, int numeroLinha) {
		int erros = 0;
		// faz a separacao dos tokens
		String token = separarVariavel(linha.trim());
		// System.out.println("\nToken: " + token);
		if (!token.equals("()")) {
			if (instrucao.equals("int") || instrucao.equals("float")) {
				// guardar a variavel na tabela de simbolos
				tabelaSimbolos.add(token);
				// System.out.println("Adicionado na tabela de simbolos: " +
				// token);
			} else if (instrucao.equals("libere")) {
				if (tabelaSimbolos.contains(token)) {
					tabelaSimbolos.remove(token);
				} else {
					erros++;
					System.err.println("Variavel não declarada na linha " + numeroLinha + " => " + token.toString());
				}
				// System.out.println("Removido da tabela de simbolos: " +
				// token);
			} else {
				if (!tabelaSimbolos.contains(token)) {
					erros++;
					System.err.println("Variavel não declarada na linha " + numeroLinha + " => " + token.toString());
				} else {
					// System.out.println("Variavel esta na tabel de simbolos: "
					// + token);
				}
			}
		}
		return erros;
	}

	public int validarMostrar(String linha, String instrucao, int numeroLinha) {
		int erros = 0;
		if (instrucao.equals("print")) {
			String nlinha = linha.replace("print", "").trim();
			if (!nlinha.startsWith("\"") && nlinha.endsWith("\"") && !nlinha.startsWith("#")) {
				System.err.println("print com String invalida na linha " + numeroLinha + " " + nlinha);
				erros++;
			}
		}
		return erros;
	}

	public List<String> getCodigoFonte() {
		return codigoFonte;
	}

	public void setCodigoFonte(List<String> codigoFonte) {
		this.codigoFonte = codigoFonte;
	}

	public List<String> getTabelaSimbolos() {
		return tabelaSimbolos;
	}

	public void setTabelaSimbolos(List<String> tabelaSimbolos) {
		this.tabelaSimbolos = tabelaSimbolos;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
