import java.util.Properties;
import java.io.IOException;
import java.text.ParseException;
import javax.mail.*;
import javax.mail.internet.*;

public class Email {
	private String host;
	private String username;
	private String password;
	private Properties prop;
	private Store store;

	public Email() throws NoSuchProviderException {
		boolean login = false;

		this.host = "outlook.office365.com";
		this.username = "";
		this.password = "";

		this.prop = new Properties();

		prop.put("mail.poop3.host", host);
		prop.put("mail.poop3.port", "995");
		prop.put("mail.poop3.starttls.enable", "true");

		Session email = Session.getDefaultInstance(prop);

		store = email.getStore("pop3s");

	}

	public boolean InboxEmail() throws MessagingException, IOException, ParseException {

		boolean cont = false;
		
		do {
			

			try {
				store.connect(host, username, password);
				Folder folder = store.getFolder("INBOX");

				if (!folder.exists()) {
					System.out.println("No INBOX...");
					System.exit(0);
				}

				folder.open(Folder.READ_WRITE);

				Message[] msg = folder.getMessages();

				for (int i = 0; i < msg.length; i++) {

					String from = InternetAddress.toString(msg[i].getFrom());

					if (from.equals("Trello <do-not-reply@trello.com>")) {

						System.out.println("chegou um novo email...executando o codigo");

						Multipart multipart = (Multipart) msg[i].getContent();

						for (int x = 0; x < multipart.getCount(); x++) {

							BodyPart bodyPart = multipart.getBodyPart(x);

							if (bodyPart.getContent().toString().indexOf("Veja o que você perdeu no Trello.") == 0) {

								msg[i].setFlag(Flags.Flag.DELETED, true); // apaga a menssagem do inbox

								cont = true;
							}

						}

					}
				}

				folder.close(true);
				store.close();
//esta dando erro quando fica mto tempo rodando isso
			}

			catch (Exception e) {
				System.out.println(e);
			}

		} while (cont == false);

		return cont;

	}

}
