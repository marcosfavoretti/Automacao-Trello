import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Api {

	private String autenticaçao;
	private String urlbase;
	private URL url;

	public Api() {

		this.autenticaçao = "";
		this.urlbase = "https://api.trello.com/1/";

	}

	public ArrayList<Object> getIdlistas() {

		ArrayList<Object> listsID = new ArrayList<Object>();

		try {

			url = new URL(this.urlbase + "boards/ylHajPHQ/lists?" + this.autenticaçao);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();

			int responseCode = conn.getResponseCode();

			if (responseCode != 200) {// verfica se deu certo a requisi�ao
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			} else {

				StringBuilder informationString = new StringBuilder();// pega o codigo json
				Scanner scanner = new Scanner(url.openStream());// le o json

				while (scanner.hasNext()) {// quanto tiver linha para ler ele le
					informationString.append(scanner.nextLine());// concatena o q o scanner le no informationString
				}

				scanner.close();

				JSONParser parse = new JSONParser();

				JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationString));

				int i = 0, sair = 0;

				do {
					try {
						JSONObject obj = (JSONObject) dataObject.get(i);
						listsID.add(obj.get("id"));
						i++;
					} catch (Exception e) {
						sair++;
					}
				} while (sair == 0);
			}
		} catch (Exception e) {
			System.out.println("erro: " + e);
		}
		return listsID;
	}

	public ArrayList<JSONObject> verficaCartao(ArrayList<Object> ListsID) {

		ArrayList<JSONObject> melhorias = new ArrayList<JSONObject>();

		int i = 0, sair = 0;

		do {

			try {

				url = new URL(this.urlbase + "lists/" + ListsID.get(i).toString() + "/cards?" + this.autenticaçao);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("GET");
				conn.connect();

				int responseCode = conn.getResponseCode();

				if (responseCode != 200) {
					throw new RuntimeException("HttpResponseCode: " + responseCode);
				}

				else {

					StringBuilder informationString = new StringBuilder();
					Scanner scanner = new Scanner(url.openStream());

					while (scanner.hasNext()) {
						informationString.append(scanner.nextLine());
					}
					scanner.close();

					JSONParser parse2 = new JSONParser();
					JSONArray dataObject2 = (JSONArray) parse2.parse(String.valueOf(informationString));

					int i2 = 0, sair2 = 0;

					do {

						try {

							JSONObject cardlist = (JSONObject) dataObject2.get(i2);

							JSONObject cover = (JSONObject) cardlist.get("cover");

							String info = (String) cover.get("color");

							if (info != null) {
								if (info.equals("yellow")) {// se a cor da capa nao for amarela ele nao vai realizar
									melhorias.add(cardlist);
									i2++;
								}
							} else {
								i2++;
							}

						} catch (Exception e) {
							sair2++;

						}

					} while (sair2 == 0);

					i++;
				}
			} catch (Exception e) {
				sair++;

			}

		} while (sair == 0);

		return melhorias;// retorna apenas os cartoes que estao com a capa melhorias ativados
	}

	public void moverCardtoBoard(String idcard) {
		try {

			url = new URL(this.urlbase + "cards/" + idcard
					+ "?idBoard=63a63683d1860501941f2a3d&idList=63a9940760e2c4012457ecf2" + this.autenticaçao);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("PUT");
			conn.connect();

			int responseCode = conn.getResponseCode();

			if (responseCode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			}

			else {

				System.out.println("movido");

			}
		} catch (IOException e) {
			System.out.println(e);

		}
	}

	public void moverCard(String id) {
		try {
			url = new URL(this.urlbase + "cards/" + id + "?idList=63a9941055df410015d65ae3" + this.autenticaçao);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("PUT");
			conn.connect();

			int responseCode = conn.getResponseCode();

			if (responseCode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			}

			else {

				System.out.println("movido");

			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void mudarPadrao(String id) {
		String name = "";
		for (int i = 0; 3 > i; i++) {
			if (i == 0) {
				name = "Area";
			} else if (i == 1) {
				name = "Tipo";
			} else {
				name = "Programa";
			}

			try {
				url = new URL(this.urlbase + "cards/" + id + "/checklists?name="
						+ URLEncoder.encode(name, StandardCharsets.UTF_8) + this.autenticaçao);

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.connect();

				int responseCode = conn.getResponseCode();

				if (responseCode != 200) {
					throw new RuntimeException("HttpResponseCode: " + responseCode);
				}

			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	public void criaCheckitem(int i, String idcheck) {
		String name = "";

		if (i == 0) {
			for (int j = 0; 3 > j; j++) {
				if (j == 0) {
					name = "qualidade";
				} else if (j == 1) {
					name = "meio ambiente";
				} else if (j == 2) {
					name = "segurança";// dar um jeito de imprimir o ç e adicionar try cathc para criaçao dos cartoes
										// para nao ficar um try de erro

				}
				try {
					url = new URL(this.urlbase + "checklists/" + idcheck + "/checkItems?name="
							+ URLEncoder.encode(name, StandardCharsets.UTF_8) + this.autenticaçao);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();

					conn.setRequestMethod("POST");
					conn.connect();

					int responseCode = conn.getResponseCode();

					if (responseCode != 200) {
						throw new RuntimeException("HttpResponseCode: " + responseCode);
					}

					else {
						System.out.println("criado campo");

					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}

		} else if (i == 1) {
			for (int j = 0; 2 > j; j++) {
				if (j == 0) {
					name = "melhoria";
				} else if (j == 1) {
					name = "corretivo";
				}
				try {
					url = new URL(this.urlbase + "checklists/" + idcheck + "/checkItems?name="
							+ URLEncoder.encode(name, StandardCharsets.UTF_8) + this.autenticaçao);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();

					conn.setRequestMethod("POST");
					conn.connect();

					int responseCode = conn.getResponseCode();

					if (responseCode != 200) {
						throw new RuntimeException("HttpResponseCode: " + responseCode);
					}

					else {
						System.out.println("criado campo");

					}

				} catch (IOException e) {
					System.out.println(e);
				}
			}
		} else {
			for (int j = 0; 4 > j; j++) {
				if (j == 0) {
					name = "5s";
				} else if (j == 1) {
					name = "MR";
				} else if (j == 2) {
					name = "Alt. processos";
				} else if (j == 3) {
					name = "Logix";
				}
				try {
					url = new URL(this.urlbase + "checklists/" + idcheck + "/checkItems?name="
							+ URLEncoder.encode(name, StandardCharsets.UTF_8) + this.autenticaçao);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();

					conn.setRequestMethod("POST");
					conn.connect();

					int responseCode = conn.getResponseCode();

					if (responseCode != 200) {
						throw new RuntimeException("HttpResponseCode: " + responseCode);
					}

					else {
						System.out.println("criado campo");

					}

				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
	}

	public String getVotacao(String idchecklist, int index) throws ParseException {

		String vetor = "";

		try {

			url = new URL(this.urlbase + "checklists/" + idchecklist + "?" + this.autenticaçao);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.connect();

			int responseCode = conn.getResponseCode();

			if (responseCode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			}

			else {

				StringBuilder informationString = new StringBuilder();// pega o codigo json
				Scanner scanner = new Scanner(url.openStream());// le o json

				while (scanner.hasNext()) {// quanto tiver linha para ler ele le
					informationString.append(scanner.nextLine());// concatena o q o scanner le no informationString
				}

				scanner.close();

				JSONParser parse = new JSONParser();

				JSONObject camposlist = (JSONObject) parse.parse(String.valueOf(informationString));
				JSONArray checkitens = (JSONArray) camposlist.get("checkItems");

				int num = 0;// serve para ordenar os nome do chekclist complete

				for (int i = 0; i < checkitens.size(); i++) {// checkitens sao os itens do check list

					JSONObject campo = (JSONObject) checkitens.get(i);

					String status = campo.get("state").toString();

					try {

						if (status.equals("complete") && index == 0) {// caso seja o primeiro check

							String[] completecheck = new String[3];

							completecheck[num] = campo.get("name").toString();// pega o nome do campo selecionado dentro
																				// do checklist

							vetor += completecheck[num] + ", ";

							num++;

						} // tratar na hora de passsar o array para o commnet

						else if (status.equals("complete") && index > 0) {// caso nao seja a primeiro check

							vetor = campo.get("name").toString();// NESSE CASO ACHO Q SEMPRE SERA SUBSTITUIDO O VALOR
																	// CASO SEJA ASSINALADO DOIS

						}

						else {
							System.out.println("nao selecionado");
						}

					} catch (Exception e) {
						System.out.println(e);

					}

				}

			}

		} catch (IOException e) {
			System.out.println(e);
		}
		return vetor;
	}

	public String pegaDesc(String idcard) {

		int i = 0, sair = 0;
		String melhorias = "";

		try {

			url = new URL(this.urlbase + "cards/" + idcard + "?" + this.autenticaçao);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.connect();

			int responseCode = conn.getResponseCode();

			if (responseCode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			}

			else {

				StringBuilder informationString = new StringBuilder();
				Scanner scanner = new Scanner(url.openStream());

				while (scanner.hasNext()) {
					informationString.append(scanner.nextLine());
				}

				scanner.close();

				JSONParser parse2 = new JSONParser();
				JSONObject dataObject2 = (JSONObject) parse2.parse(String.valueOf(informationString));

				melhorias = dataObject2.get("desc").toString();

			}
		} catch (Exception e) {
			sair++;
		}

		return melhorias;// retorna apenas os cartoes que estao com a capa melhorias ativados
	}

	public ArrayList<String> buscarcriador(String idcard) {

		ArrayList<String> infoscriacor = new ArrayList();
		;

		try {

			url = new URL(this.urlbase + "boards/63a63683d1860501941f2a3d/actions?" + this.autenticaçao
					+ "&filter=createCard&fields=idMemberCreator&idModels=" + idcard);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.connect();

			int responseCode = conn.getResponseCode();

			if (responseCode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			}

			else {

				StringBuilder informationString = new StringBuilder();
				Scanner scanner = new Scanner(url.openStream());

				while (scanner.hasNext()) {
					informationString.append(scanner.nextLine());
				}

				scanner.close();

				JSONParser parse2 = new JSONParser();

				JSONArray dataObject2 = (JSONArray) parse2.parse(String.valueOf(informationString));

				JSONObject member = (JSONObject) dataObject2.get(0);

				JSONObject status = (JSONObject) member.get("memberCreator");

				infoscriacor.add(status.get("fullName").toString());

				infoscriacor.add(status.get("username").toString());

			}

		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println(infoscriacor.size());
		return infoscriacor;

	} // thiago processos problema com o web team

	public void escreveComent(String idcard, String texto) {
		try {

			url = new URL(this.urlbase + "cards/" + idcard + "/actions/comments?text="
					+ URLEncoder.encode(texto, StandardCharsets.UTF_8) + this.autenticaçao);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.connect();

			int responseCode = conn.getResponseCode();

			if (responseCode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			}

			else {
				System.out.println("comment feito");
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void apagaChecklist(String idchecklist) {

		try {
			url = new URL(this.urlbase + "checklists/" + idchecklist + "?" + this.autenticaçao);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("DELETE");
			conn.connect();

			int responseCode = conn.getResponseCode();

			if (responseCode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			}

			else {
				System.out.println("exclusao feita");
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public String getDatacard(String cardID) {// card id estara em hexa decimal

		System.out.println("pegar data ");
		long decimalID = Integer.parseInt(cardID.substring(0, 8), 16);
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String convertedDate = formato.format(new java.util.Date(decimalID * 1000));

		return convertedDate;

	}

}

//https://api.trello.com/1/checklists/{id}/checkItems?name={name}&key=APIKey&token=APIToken
//https://api.trello.com/1/cards/63a7766d3c650a05425d4866?idBoard=62ffb77e936a0533aae376d0&idList=62ffb7809e6ed0503ddb97b0&key=0f11d1be0b03e571d6c61226021e671b&token=ATTA88947b8a25cf11e4e1ce844d08acc80ab17e0d7b19f37357870a2216521096bf0E056C36
//https://api.trello.com/1/cards/63a7766d3c650a05425d4866?idList=63a67deb9e706900cab5314b&key=0f11d1be0b03e571d6c61226021e671b&token=ATTA88947b8a25cf11e4e1ce844d08acc80ab17e0d7b19f37357870a2216521096bf0E056C36
//https://api.trello.com/1/cards?name=melhoria%20no%20sistema%20de%20separacao%20da%20Rome%20entre%20o%20setor%20de%20laser%20e%20dobra"+ "&idList=63a67dc62509cd01c7f63daf&key=0f11d1be0b03e571d6c61226021e671b&token=ATTA88947b8a25cf11e4e1ce844d08acc80ab17e0d7b19f37357870a2216521096bf0E056C36
//https://api.trello.com/1/boards/ylHajPHQ/lists?key=0f11d1be0b03e571d6c61226021e671b&token=ATTA88947b8a25cf11e4e1ce844d08acc80ab17e0d7b19f37357870a2216521096bf0E056C36
//https://api.trello.com/1/lists/" + card.get(i).toString()+"/cards?key=0f11d1be0b03e571d6c61226021e671b&token=ATTA88947b8a25cf11e4e1ce844d08acc80ab17e0d7b19f37357870a2216521096bf0E056C36
