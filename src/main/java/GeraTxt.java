import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeraTxt {
//verificar o q aconece quando vc tenta criar algo q ja esta com nome igual eu acho q ele substui as infos
	private File caminhoInfos;

	public void criadiretorio(String nome) {

		File file = new File("../baseDados");

		if (!file.exists()) {// se nao existe ele cria o diretorio
			file.mkdirs();
		}

		this.caminhoInfos = new File("..\\baseDados\\melhoria-" + nome + ".txt");
	}

	public void criararquivo() throws IOException {//verifica se o arquivo do cartao existe e caso nao exista cria 
		//caso dois cartao tenha o memso nome o porgrama ira criar um distinçao pelo nome do arquivo numerando ele pos o nome de 1 .....

		boolean trelloexist = caminhoInfos.exists();

		if (trelloexist == false) {
			System.out.println("arquivos trello criado");
			caminhoInfos.createNewFile();
		}

		else {
				

			boolean create = false;
			int l = 1;
			do {
				

				String temp = this.caminhoInfos.toString();
				boolean exist;

				int tam = temp.length();

				if (l == 1) {

					this.caminhoInfos = new File(temp.substring(0, (tam - 4)) + "-" + l + ".txt");
					exist = caminhoInfos.exists();
					l++;
					if (exist == false) {

						caminhoInfos.createNewFile();
						create = true;
					}

				}

				else {
					this.caminhoInfos = new File(temp.substring(0, (tam - 5)) + l + ".txt");

					exist = caminhoInfos.exists();

					l++;
					if (exist == false) {
						caminhoInfos.createNewFile();
						create = true;
					}

				}

			} while (create == false);

			caminhoInfos.createNewFile();

		}

	}

	public void escrevertrelllo(String compra) throws IOException {

		FileWriter fw = new FileWriter(this.caminhoInfos, false);

		BufferedWriter bw = new BufferedWriter(fw);

		bw.write(compra);

		bw.close();

		fw.close();

	}
}
