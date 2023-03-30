import java.io.IOException;

import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLEncoder;
//import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mysql.cj.xdevapi.JsonArray;

public class Api {

	private String autenticaçao;
	private String urlbase;
	private URL url;

	public Api() {

		this.autenticaçao = "&key=0f11d1be0b03e571d6c61226021e671b&token=ATTA88947b8a25cf11e4e1ce844d08acc80ab17e0d7b19f37357870a2216521096bf0E056C36";
		this.urlbase = "https://api.trello.com/1/";

	}

	public JSONArray Idchecklist(String idcard) throws IOException, ParseException {// colocaar try e catch e ainda
																					// colocar como private o connect
		JSONArray idlist = null;

		url = new URL(this.urlbase + "cards/" + idcard + "?" + this.autenticaçao);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("GET");
		conn.connect();

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("HttpResponseCode: " + conn.getResponseCode());
		} else {
			StringBuilder informationString = new StringBuilder();// pega o codigo json
			Scanner scanner = new Scanner(url.openStream());// le o json
			while (scanner.hasNext()) {// quanto tiver linha para ler ele le
				informationString.append(scanner.nextLine());// concatena o q o scanner le no informationString
			}
			scanner.close();
			JSONParser parse = new JSONParser();
			JSONObject json = (JSONObject) parse.parse(String.valueOf(informationString));
			idlist = (JSONArray) json.get("idChecklists");

		}

		return idlist;// linko com o cardclass a lista
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
		String [] models;
		models = new String [] {
				"640f25cd82c2739cb2812e1a",//area
				"640f25ce3e5a275897073e60",//tipo
				"640f25ce0b97d2b084814ea6"//programa
		};//array com os checklist do model
		
			for (int i = 0; models.length > i; i++) {
			try {
				url = new URL(this.urlbase + "checklists?idCard=" + id + "&idChecklistSource=" + models[i] + this.autenticaçao);

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
		System.out.println("criado");
	}

	public void criaCheckitem(int i, String idcheck) {// apenas seta o nome com base em uma array
		String[] name;

		if (i == 0) {
			name = new String[] { "qualidade", "meio ambiente", "segunraça" };

		} else if (i == 1) {
			name = new String[] { "melhoria", "corretivo" };

		} else {
			name = new String[] { "5s", "MR", "alt. processos", "logix" };

		}
		geraChecklist(idcheck, name);
	}

	private void geraChecklist(String idcheck, String[] name) {
		for (int j = 0; name.length > j; j++) {
			try {
				url = new URL(this.urlbase + "checklists/" + idcheck + "/checkItems?name="
						+ URLEncoder.encode(name[j], StandardCharsets.UTF_8.toString()) + this.autenticaçao);
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

		return melhorias;
	}

	public void escreveComent(String idcard, String comment) {

		try {
			url = new URL(this.urlbase + "cards/" + idcard + "/actions/comments?text="
					+ URLEncoder.encode(comment, StandardCharsets.UTF_8.toString()) + this.autenticaçao);

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
