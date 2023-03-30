
import java.io.ObjectInputFilter.Config;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.protobuf.Empty;
import com.mysql.cj.xdevapi.JsonArray;

public class Main {
	public static void main(String[] args) throws Exception {

		Api api = new Api();// classe para fazer requisiçoes na api do trello

		Webhook web = new Webhook();
		web.doWebhook();// faz a conexao do webhook com o painel do trello

		Bdverifica bd = new Bdverifica();

		for (;;) {// abaixar esse for infinito para onde realmente precisa

			do {
				bd.ProcOM();
				System.out.println("->>>>>" + bd.ProcOM().isEmpty());
				Thread.sleep(3000);
			} while (bd.getOmInfos().isEmpty() == true);// recebe como resposta o lista private do bdverifica
			System.out.println("antes new" + bd.getNewomInfos().size());
			System.out.println("antes" + bd.getOmInfos().size());
			bd.getOmInfos().clear();
			System.out.println(bd.getNewomInfos().size());
			System.out.println(bd.getOmInfos().size());

			int criado = 0;

			try {
				for (CardClass info : bd.getNewomInfos()) {
					System.out.println("padrao mudado TRYYYYY");
					if (api.Idchecklist(info.getIdcard()).isEmpty() == true) {// ver se nao tem lista geradas ja
						System.out.println("padrao mudado");
						api.mudarPadrao(info.getIdcard());// aki vai ser colocado tudo
					} else {
						System.out.println("ja tem uma lista");
					}
				}

			}

			catch (Exception e) {
				System.out.println("nao precisa de checklists" + e);
			}
			int verificacao = 0;
			String desc;

			for (CardClass info : bd.getNewomInfos()) {// pega as melhorias e ainda ve se pode deve criar check list ou
														// nao
				verificacao = 0;
				ArrayList<String> alternativas = new ArrayList<String>();

				try {
					if (info.isCi() == false) {// se ainda nao criou o check list
						for (int l = 0; api.Idchecklist(info.getIdcard()).size() > l; l++) {
							CheckListJson cl = new CheckListJson(api.Idchecklist(info.getIdcard()).get(l).toString());
							info.getCardCL().add(cl);// starto o array passando o id de cada CL
							// api.criaCheckitem(l,api.Idchecklist(info.getIdcard()).get(l).toString());//cria
							// o check list
						}
					} else {
						System.out.println("ja tem checklist");
					}
				} // try
				catch (Exception e) {
					System.out.println(e);
				}

			}

		}
	}
}
