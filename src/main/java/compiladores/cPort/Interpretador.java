package compiladores.cPort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author danton.issler
 * @since 08/11/2020
 */
public class Interpretador {
	public List<String> codigoFonte;
	public Map<String, Object> ts;
	public String erros = "";
	public int nrlinha = 0;
	private Scanner teclado;

	// construtora inicial do programa
	public Interpretador(String programa) throws IOException {
		Arquivo arq = new Arquivo(programa);
		codigoFonte = arq.lerConteudo();
		ts = new HashMap<String, Object>();
	}

	// executa o bloco de codigo para chamada recursiva
	public Interpretador(List<String> bloco, Map<String, Object> tabsimb) throws IOException {
		codigoFonte = bloco;
		ts = tabsimb;
	}

	// percorre todo o codigo fonte, linha por linha
	public void intepretar() throws IOException {
		nrlinha = 0;
		// passa por todas as linhas do codigo fonte
		for (nrlinha = 0; nrlinha < codigoFonte.size(); nrlinha++) {
			String linha = codigoFonte.get(nrlinha); // pega a linha atual
			erros += interpretarLinha(linha, nrlinha);
		}
		if (erros.length() > 0) {
			System.out.println("Erros:" + erros);
		}
	}

	// identifica a instrução e executa o interpretador correto
	public String interpretarLinha(String linha, Integer nrlinha) throws IOException {
		String erro = "";
		// separa a linha em palavras
		String[] palavras = linha.trim().split(" ");
		// identifica o inicio do programa
		erro += interpretarInicioPrograma(palavras[0], linha, nrlinha);
		// identifica o fim do programa
		erro += interpretarFimPrograma(palavras[0], linha, nrlinha);
		// processa inclusao do simbolo na tabela de simbolos
		erro += interpretarInteiro(palavras, nrlinha);
		// processa a inclusao do simbolo na tabela de simbolos
		erro += interpretarFlutuante(palavras, nrlinha);
		// remove da tabela de simbolos
		erro += interpretarLibera(palavras, nrlinha);
		// apresenta uma string na tela
		erro += interpretarMostre(linha, nrlinha);
		// captura um dado do teclado
		erro += interpretarLeia(palavras, nrlinha);
		// escreve um dado na tela
		erro += interpretarEscreva(palavras, nrlinha);
		// calcula uma expressão aritmetica
		erro += interpretarExpressaoAritmetica(palavras, nrlinha);
		// atribuir valor na variavel
		erro += interpretarAtribuicao(palavras, nrlinha);
		// calcula a execução de uma Condição
		erro += interpretarSE(palavras, nrlinha);
		erro += interpretarPara(palavras, nrlinha);
		return erro;
	}

	// identifica o inicio do programa
	public String interpretarInicioPrograma(String palavra, String linha, Integer nrlinha) {
		// a primeira palavra
		if ((palavra.equals("inicioprograma") && nrlinha == 0)) { // ta certo
			return "";
		}
		if (!palavra.equals("inicioprograma")) {
			return "";
		}
		return "Erro na linha " + nrlinha + ": Declaracao de inicio de programa errado.";
	}

	// identifica o fim do programa
	public String interpretarFimPrograma(String palavra, String linha, Integer nrlinha) {
		// a ultima palavra
		if (palavra.equals("fimprograma") && nrlinha == codigoFonte.size() - 1) {
			return "";
		}
		if (!palavra.equals("fimprograma")) {
			return "";
		}
		return "Erro: Fim de programa inválido na linha " + nrlinha;
	}

	// processa inclusao do simbolo na tabela de simbolos
	public String interpretarInteiro(String[] palavras, Integer nrlinha) {

		if (palavras[0].equals("inteiro")) {

			String nomeVariavel = palavras[1];
			// se a variavel já existir na tabela de simbolos
			if (ts.containsKey(nomeVariavel)) {
				return "Erro na linha " + nrlinha + " variavel " + nomeVariavel + " redeclarada";
			}
			ts.put(nomeVariavel, 0);
		}
		return "";
	}

	// processa a inclusao do simbolo na tabela de simbolos
	public String interpretarFlutuante(String[] palavras, Integer nrlinha) {
		if (palavras[0].equals("flutuante")) {
			String nomeVariavel = palavras[1];
			// se a variavel já existir na tabela de simbolos
			if (ts.containsKey(nomeVariavel)) {
				return "Erro na linha " + nrlinha + " variavel " + nomeVariavel + " redeclarada";
			}
			ts.put(nomeVariavel, 0);
		}
		return "";
	}

	// remove da tabela de simbolos
	public String interpretarLibera(String[] palavras, Integer nrlinha) {

		if (palavras[0].equals("inteiro") || palavras[0].equals("flutuante")) {

			String nomeVariavel = palavras[1];
			// se a variavel nao existir na tabela de simbolos
			if (!ts.containsKey(nomeVariavel)) {
				return "Erro na linha " + nrlinha + " variavel " + nomeVariavel + " não declarada";
			}
			ts.remove(nomeVariavel);
		}
		return "";
	}

	// apresenta uma string na tela
	public String interpretarMostre(String linha, Integer nrlinha) {
		if (linha.startsWith("mostre")) {
			String str = linha.substring(7, linha.length() - 1);
			str = str.replace("\"", "");
			System.out.println(str);
			return "";
		}
		return "";
	}

	// captura um dado do teclado
	public String interpretarLeia(String[] palavras, Integer nrlinha) {
		if (!palavras[0].equals("leia")) {
			return "";
		}
		String nomeVariavel = palavras[1];
		teclado = new Scanner(System.in);
		Integer valor = teclado.nextInt();
		// vai para a tabela de simbolos com o valor
		ts.put(nomeVariavel, valor);
		return "";
	}

	// escreve um dado na tela
	public String interpretarEscreva(String[] palavras, Integer nrlinha) {
		if (palavras[0].startsWith("escreva")) {
			String nomeVariavel = palavras[1];
			String str = ts.get(nomeVariavel).toString();
			System.out.println(str);
			return "";
		}
		return "";
	}

	public String interpretarAtribuicao(String[] palavras, Integer nrlinha) {
		if (palavras.length == 3) {
			if (palavras[1].equals(":=")) {
				// ta tudo certo
				String variavel = palavras[0];
				String dado = palavras[2];
				Float valor;
				if (ts.containsKey(dado)) {
					valor = Float.parseFloat(ts.get(dado).toString());
				} else {
					valor = Float.parseFloat(dado);
				}
				ts.put(variavel, valor);
			}
		}
		return "";
	}

	// calcula uma expressão aritmetica
	public String interpretarExpressaoAritmetica(String[] palavras, Integer nrlinha) {
		if (palavras.length == 5) {
			if (palavras[1].equals(":=")) {
				// ta tudo certo
				String variavel = palavras[0];
				String operacao = palavras[3];
				String operador = palavras[2];
				String operando = palavras[4];
				String valorOperador = ts.get(operador).toString();
				String valorOperando = ts.get(operando).toString();
				Float valor1 = Float.parseFloat(valorOperador);
				Float valor2 = Float.parseFloat(valorOperando);
				Float resultado = calcularExpressao(valor1, valor2, operacao);
				if (resultado < 0) {
					return "Erro na linha " + nrlinha + " expressao aritmetica invalida";
				}
				ts.put(variavel, resultado);
			}
		}
		return "";
	}

	// fazer o calculo da expressao
	public Float calcularExpressao(Float valor1, Float valor2, String operacao) {
		if (operacao.equals("+")) {
			return valor1 + valor2;
		}
		if (operacao.equals("-")) {
			return valor1 - valor2;
		}
		if (operacao.equals("*")) {
			return valor1 * valor2;
		}
		if (operacao.equals("/")) {

			if (valor2 == 0)
				return -1f;
			return valor1 / valor2;
		}
		return 0f;
	}

	// calcula a execução de uma Condição
	public String interpretarSE(String[] palavras, Integer nrlinha) throws IOException {
		int i = 0;
		// verifica se não é um se entao encerra
		if (!palavras[0].equals("se")) {
			return "";
		} // contracao de if
		Boolean resultado = calcularOperacaoRelacional(palavras[1], palavras[2], palavras[3]);
		// contar o bloco de codigo do se
		List<String> bloco = new ArrayList<String>();
		if (resultado) { // pega o bloco se
			bloco = capturarBlocoSe(nrlinha);
		} else { // pega o bloco senao caso tenha
			bloco = capturarBlocoSenao(nrlinha);
		}
		// se o bloco é outro programa
		// vamos exeuta-lo
		if (bloco.size() > 0) {
			Interpretador i2 = new Interpretador(bloco, ts);
			i2.intepretar();
			this.ts = i2.ts;
		}
		i = nrlinha + 1; // procura o fimse e reposiciona o processamento
		while (!codigoFonte.get(i).equals("fimse")) {
			i++;
		}
		this.nrlinha = i; // reposiciona para a proxima linha depois do fim se
		return "";
	}

	// captura Bloco senao
	public List<String> capturarBlocoSenao(int nrlinha) {

		List<String> bloco = new ArrayList<String>();
		Integer i = nrlinha + 1;
		Boolean temSenao = false;

		while (i < codigoFonte.size()) {
			String linhaBloco = "";
			if (temSenao) {
				linhaBloco = codigoFonte.get(i);
			}
			if (codigoFonte.get(i).startsWith("senao")) {
				temSenao = true;
			}
			if (codigoFonte.get(i).startsWith("fimse")) {
				break;
			}
			if (linhaBloco.length() > 0) // se tem conteudo para adicionar
				bloco.add(linhaBloco);
			i++;
		}
		this.nrlinha = i; // reposiciona o ponteiro do programa
		return bloco;

	}

	// captura bloco se
	public List<String> capturarBlocoSe(int nrlinha) {
		List<String> bloco = new ArrayList<String>();
		Integer i = nrlinha + 1;
		while (i < codigoFonte.size()) {
			String linhaBloco = codigoFonte.get(i);
			if (codigoFonte.get(i).startsWith("senao")) {
				break;
			}
			if (codigoFonte.get(i).startsWith("fimse")) {
				break;
			}
			if (linhaBloco.length() > 0) // se tem conteudo para adicionar
				bloco.add(linhaBloco);
			i++;
		}
		this.nrlinha = i; // reposiciona o ponteiro do programa
		return bloco;
	}

	// capturar bloco para
	public List<String> capturarBlocoPara(int nrlinha) {
		List<String> bloco = new ArrayList<String>();
		Integer i = nrlinha + 1;
		while (i < codigoFonte.size()) {
			String linhaBloco = codigoFonte.get(i);
			if (codigoFonte.get(i).startsWith("fimpara")) {
				break;
			}
			if (linhaBloco.length() > 0) // se tem conteudo para adicionar
				bloco.add(linhaBloco);
			i++;
		}
		this.nrlinha = i; // reposiciona o ponteiro do programa
		return bloco;
	}

	// calcuçar Operacao relacional
	public Boolean calcularOperacaoRelacional(String variavel, String operacao, String operando) {
		// exemplo: $x = $y + $z
		// pega o valor da variavel na tabela de simblos
		String valorVar = ts.get(variavel).toString();
		// converte o valor da variavel em float
		Float valorVariavel = Float.parseFloat(valorVar);

		Float valorOperando;
		// verifica se é variavel
		if (ts.containsKey(operando))
			valorOperando = Float.parseFloat(ts.get(operando).toString());
		else
			valorOperando = Float.parseFloat(operando);
		// testa igual
		if (operacao.equals("="))
			return valorVariavel.equals(valorOperando);
		// testa maior
		if (operacao.equals(">"))
			return valorVariavel > valorOperando;
		// testaa menor
		if (operacao.equals("<"))
			return valorVariavel < valorOperando;
		// testa diferente
		if (operacao.equals("!"))
			return !valorVariavel.equals(valorOperando);
		// testa >=
		if (operacao.equals("<="))
			return valorVariavel <= valorOperando;
		// testa <=
		if (operacao.equals(">="))
			return valorVariavel >= valorOperando;
		return false;

	}

	// interpretar o para
	public String interpretarPara(String[] palavras, Integer nrlinha) throws IOException {
		// bloqueia para outras instrucoes
		if (!palavras[0].equals("para")) {
			return "";
		}
		// captura o nome da variavel na instrução para
		String variavel = palavras[1];
		// recupera o ponto de inicio e de fim do for
		int inicio = Integer.parseInt(palavras[3]);
		int fim = Integer.parseInt(palavras[5]);
		List<String> bloco = capturarBlocoPara(nrlinha);
		Integer i = 0;
		for (i = inicio; i <= fim; i++) {
			// setar o valor da variavel
			ts.put(variavel, i.toString());
			Interpretador iPara = new Interpretador(bloco, ts);
			iPara.intepretar();
			// atualizado a tabela de simbolos a partir do sub-processamento
			ts = iPara.ts;
		}
		// reposico o controlador de linha
		i = nrlinha + 1; // proxima linha
		while (!codigoFonte.get(i).equals("fimpara")) {
			i++;
		}
		this.nrlinha = i;
		return "";
	}
}
