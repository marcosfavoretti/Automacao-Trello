import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Scanner;

import com.stripe.exception.StripeException;


public class Webhook {

	private String urlbase;
	
	
		public Webhook() {
			this.urlbase = "https://api.trello.com/1/";
			
		
		}
		
		public void doWebhook() {//inicio o webhook no quadro do trello
			try {

				URL url = new URL(this.urlbase + "tokens/ATTA88947b8a25cf11e4e1ce844d08acc80ab17e0d7b19f37357870a2216521096bf0E056C36/webhooks/?key=0f11d1be0b03e571d6c61226021e671b"
						+ "&callbackURL=https://requestinspector.com/inspect/01gpnkp2xaqyqqczsf0d2rj580&idModel=63a63683d1860501941f2a3d"
						//passa o model o id do q ele vai observar
						);
								
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.connect();

				int responseCode = conn.getResponseCode();
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

		public Object respwebhook() throws StripeException {//pego a respota do webhook
			
			Object autenticaçao = null;
			try {
			
				URL url = new URL("https://requestinspector.com/inspect/01gpnkp2xaqyqqczsf0d2rj580");
				
//				URL url = new URL(this.urlbase + "tokens/ATTA88947b8a25cf11e4e1ce844d08acc80ab17e0d7b19f37357870a2216521096bf0E056C36/webhooks/?key=0f11d1be0b03e571d6c61226021e671b"
//						+ "&callbackURL=https://webhook.site/2b968a88-fec8-48ea-926c-18b43495beec&idModel=63a63683d1860501941f2a3d");
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.connect();

				int responseCode = conn.getResponseCode();

				if (responseCode != 200) {
					throw new RuntimeException("HttpResponseCode: " + responseCode);
				}

				else {
					
					InputStreamReader read = new InputStreamReader(conn.getInputStream());
					StringBuilder informationString = new StringBuilder();
					Scanner scanner = new Scanner(read);// mexido

					while (scanner.hasNext()) {
						informationString.append(scanner.nextLine());
					}

					scanner.close();

					// JSONParser parse = new JSONParser();
					System.out.println("aki esta" + informationString);

					// JSONObject jsonobject = (JSONObject)
					// parse.parse(String.valueOf(informationString));

					// autenticaçao = jsonobject;
					// JSONArray dataObject2 = (JSONArray)
					// parse2.parse(String.valueOf(informationString));

					// System.out.println("PRIMERO SYSTem" + jsonobject);
				}

			} catch (Exception e) {
				System.out.println(e);
			}
			
		
			return autenticaçao;

		}

	}



