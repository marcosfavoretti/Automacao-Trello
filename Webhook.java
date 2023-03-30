import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.stripe.exception.StripeException;


public class Webhook {

	private String urlbase;
	private String callback;
	
		public Webhook() {
			this.urlbase = "https://api.trello.com/1/";
			this.callback = "https://53e9-201-46-33-3.sa.ngrok.io/cad";//aki vai mudar o url pq e free
		}
		
		public void doWebhook() throws IOException {//inicio o webhook no quadro do trello
			URL url = new URL("https://api.trello.com/1/tokens/ATTA88947b8a25cf11e4e1ce844d08acc80ab17e0d7b19f37357870a2216521096bf0E056C36/webhooks/?key=0f11d1be0b03e571d6c61226021e671b&callbackURL=https://webhook.site/2b968a88-fec8-48ea-926c-18b43495beec&idModel=63b43485d4894e03a280177d&key=0f11d1be0b03e571d6c61226021e671b");
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
				System.out.println(informationString);
				try {
				JSONParser parse = new JSONParser();
				JSONArray json = (JSONArray) parse.parse(String.valueOf(informationString));
				if(json.isEmpty()==false) {
					System.out.println(" WebHook ja existente");

				}
				else {
					try {
						//model = o lugar onde sera observado no caso esta o quadro todo
						 url = new URL(this.urlbase+ "tokens/ATTA88947b8a25cf11e4e1ce844d08acc80ab17e0d7b19f37357870a2216521096bf0E056C36/webhooks/?key=0f11d1be0b03e571d6c61226021e671b&callbackURL="+this.callback+"&idModel=62ffb77e936a0533aae376d0&description="+System.currentTimeMillis()
								//passa o model o id do q ele vai observar
								);
						conn = (HttpURLConnection) url.openConnection();//aki foi mudado

						conn.setRequestMethod("POST");
						conn.connect();

						responseCode = conn.getResponseCode();
						System.out.println(responseCode);
						if (responseCode != 200) {
							throw new RuntimeException("HttpResponseCode: " + responseCode);
						}

						else {

							System.out.println("webhook criado");

						}
						
					} catch (Exception e) {
						
						System.out.println("post" + e);
						
					}
					
				}
				}
				
				catch(Exception e) {
					System.out.println("nao tem tamanho o array ou nao pode ser convertido para array");
					try {
						//model = o lugar onde sera observado no caso esta o quadro todo
						 url = new URL(this.urlbase+ "tokens/ATTA88947b8a25cf11e4e1ce844d08acc80ab17e0d7b19f37357870a2216521096bf0E056C36/webhooks/?key=0f11d1be0b03e571d6c61226021e671b&callbackURL="+this.callback+"&idModel=62ffb77e936a0533aae376d0&description="+System.currentTimeMillis()
								//passa o model o id do q ele vai observar
								);
						conn = (HttpURLConnection) url.openConnection();//aki foi mudado

						conn.setRequestMethod("POST");
						conn.connect();

						responseCode = conn.getResponseCode();
						System.out.println(responseCode);
						if (responseCode != 200) {
							throw new RuntimeException("HttpResponseCode: " + responseCode);
						}

						else {

							System.out.println("webhook criado");

						}
						
					} catch (Exception t) {
						
						System.out.println("post" + e);
						
					}
					
				}
				}
		}
}//copiei codigo duas vezes pois nao sei se caso der null dara problema na hora de usar isnull entao tratei de duas formas
	
