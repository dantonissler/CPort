package compiladores.cPort;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @since 08/11/2020
 */
public class Arquivo {

	private String nomeArquivo;
	private BufferedReader buffer;

	public Arquivo(String nome) {
		this.nomeArquivo = nome;
	}

	public List<String> lerConteudo() throws FileNotFoundException, IOException {
		List<String> conteudo = new ArrayList<String>();
		String nomeCompleto = this.nomeArquivo;
		FileReader arq = new FileReader(nomeCompleto);
		buffer = new BufferedReader(arq);
		String linha = "..";
		while (linha != null) {
			linha = buffer.readLine();
			if (linha != null) {
				linha = linha.trim();
				conteudo.add(linha);
			}
		}
		return conteudo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public BufferedReader getBuffer() {
		return buffer;
	}

	public void setBuffer(BufferedReader buffer) {
		this.buffer = buffer;
	}

}
