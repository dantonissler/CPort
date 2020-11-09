package compiladores.cPort;

import java.io.IOException;

/**
 * @author danton.issler
 * @since 08/11/2020
 */
public class CPort {
	public static void main(String[] args) throws IOException {
		// Scanner teclado = new Scanner(System.in);
		// System.out.println("Digite o nome do arquivo fonte:");
		// String nomeFonte = teclado.nextLine();
		String nomeFonte = "exemplo.txt";

		System.out.println("Analisando Lexicamente");
		Lexico scanner = new Lexico();
		scanner.AnalisarLexico(nomeFonte);
		System.out.println("Fim da Analise Léxica");

		Sintatico sintatico = new Sintatico();
		System.out.println("Analisando Sintáticamente");
		sintatico.analisarSintatico(nomeFonte);
		System.out.println("Fim da Analise Sintática");

		Semantico semantico = new Semantico();
		System.out.println("Analisando Semanticamente");
		semantico.analisarSemantico(nomeFonte);
		System.out.println("Fim da Analise Semantica");

		Interpretador interpretador = new Interpretador(nomeFonte);
		System.out.println("Interpretando o fonte");
		System.out.println("--------------------------------------------------------");
		interpretador.intepretar();
		System.out.println("Fim de Interpretacao");
	}
}
