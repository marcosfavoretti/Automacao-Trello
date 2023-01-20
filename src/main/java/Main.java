
import java.io.ObjectInputFilter.Config;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Main {
	public static void main(String[] args) throws Exception {

		Api api = new Api();// classe para fazer requisiçoes na api do trello

		GeraTxt txt = new GeraTxt();// classe para gerar um txt com o nome do cartao e as infos dele

		Email email = new Email();// construtor vai fazer login e conectar com a conta do outloook

	

		for (;;) {

			System.out.println("esperando modificaçao no trello...");

			 email.InboxEmail();//fica em loop verificando se houve um email novo sobre o trello

			System.out.println("procurando melhoria");

			ArrayList<Object> listid = api.getIdlistas();

			ArrayList<JSONObject> melhorias = api.verficaCartao(listid);// manda o json dos cartoes melhorias precisa

			// selecionar apenas o id para passa para prox funçao
			int criado = 0;

			for (JSONObject json : melhorias) {

				if (json.get("idChecklists") == null || ((ArrayList) json.get("idChecklists")).size() <= 1) {

					api.mudarPadrao(json.get("id").toString());

					melhorias = new ArrayList<JSONObject>();// cria uma nova lista e repasso o melhorias com os novos
															// campos
					melhorias = api.verficaCartao(listid);

					criado++;

				} else {
					System.out.println("ja tem dois checklists");
					criado = 0;
				}
			}

			for (JSONObject json2 : melhorias) {// tenho que fazer outro for pois nao aparece os valores dos id
												// checkslists
				{

					JSONArray arraychecks = (JSONArray) json2.get("idChecklists");

					if (criado == 1) {
						for (int i = 0; arraychecks.size() > i; i++) {

							api.criaCheckitem(i, arraychecks.get(i).toString());// cria o checklist das listas
						}
					} else {
						System.out.println("ja tem os check itens");
					}
				} 

				// recebe a area e o tipo que foi selecionado no check list
				int verificacao = 0;
				ArrayList<String> alternativas;
				JSONArray arraychecks;
				String desc;

				do {
					for (JSONObject json : melhorias) {// verficia qual alternativa foi selecionada

						verificacao = 0;

						arraychecks = (JSONArray) json.get("idChecklists");// id de todos os checklist do cartao

						alternativas = new ArrayList<String>();

						desc = api.pegaDesc(json.get("id").toString());// verificar qual a descriçao

						for (int i = 0; arraychecks.size() > i; i++) {

							String nameschecked = api.getVotacao(arraychecks.get(i).toString(), i); // passa o id do
																									// checklist e pega
																									// o valor
																									// selecionado

							System.out.println(i + " " + nameschecked);// ver qual alternativa foi esoclhida

							alternativas.add(nameschecked);

							if (!nameschecked.equals("") && !desc.equals("")) {

								verificacao++;
							}
						}

						if (verificacao == 3) {

							String comment = "";
							// como so o campo pode ter de 3-1 campos selecionados

							comment += " -Area: " + alternativas.get(0).substring(0, alternativas.get(0).length() - 2);// area
																														// assinalada
							comment += "\n -Tipo: " + alternativas.get(1);// tipo asssinalada
							comment += "\n -Programaçao: " + alternativas.get(2);// tipo assinalado
							comment += "\n -Descricao: " + desc+"\n";

							// pegar quem fez o cartao e que horas ee foi gerado

							api.moverCardtoBoard(json.get("id").toString());

							comment += "nome: " + api.buscarcriador(json.get("id").toString()).get(0)+"\n";// pega nome do craidor
							comment += "nick: " + api.buscarcriador(json.get("id").toString()).get(1);// pega nick name do criado
																							// no
																							// trello

							comment += "\n -Data de criçao: " + api.getDatacard(json.get("id").toString());

							Gestao gest = new Gestao(json.get("name").toString(),
									alternativas.get(0).substring(0, alternativas.get(0).length() - 2),
									alternativas.get(1), alternativas.get(2), desc,
									api.buscarcriador(json.get("id").toString()).get(0),
									api.buscarcriador(json.get("id").toString()).get(1),
									api.getDatacard(json.get("id").toString()));
							// cria um objeto gestao
							

							System.out.println("\n -verificaçao concluida apagar checklist e mover lista");

							api.escreveComent(json.get("id").toString(), comment);

							for (int i = 0; arraychecks.size() > i; i++) {
								api.apagaChecklist(arraychecks.get(i).toString());// depois descomentar isso
							}
							System.out.println(json.get("name").toString().replace(" ", "-"));
							txt.criadiretorio(json.get("name").toString().replace(" ", "-"));
							txt.criararquivo();//cria o txt dpeois com o nome do cartao
							txt.escrevertrelllo(gest.paraString());
						}

					}
				} while (verificacao != 3);
			}
		}
	}
	
	
}