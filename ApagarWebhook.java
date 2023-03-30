import java.net.HttpURLConnection;
import java.net.URL;

public class ApagarWebhook {

	public static void main(String[] args) {
		
			String urlbase = "https://api.trello.com/1/";
			String callback = "https://d631-201-46-33-3.sa.ngrok.io/rota";//aki vai mudar o url pq e free
			String id = "6405ca7afda7fd9785965512";
			try {

				URL url = new URL(urlbase+ "tokens/ATTA88947b8a25cf11e4e1ce844d08acc80ab17e0d7b19f37357870a2216521096bf0E056C36/webhooks/"+id+"/?key=0f11d1be0b03e571d6c61226021e671b"
						//passa o model o id do q ele vai observar
						);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("DELETE");
				conn.connect();

				int responseCode = conn.getResponseCode();
				System.out.println(responseCode);
				if (responseCode != 200) {
					throw new RuntimeException("HttpResponseCode: " + responseCode);
				}

				else {

					System.out.println("webhook apagado");

				}

			} catch (Exception e) {
				System.out.println("post" + e);
			}
		}



}
