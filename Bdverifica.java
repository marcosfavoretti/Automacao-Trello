import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javassist.bytecode.ExceptionsAttribute;

public class Bdverifica {// entra no banco e ve se precisa ou nao executar o script

	private Api api = new Api(); // instancia da classe que faço minhas req na api do trello
	private ArrayList<CardClass> omInfos = new ArrayList<CardClass>();
	private ArrayList<CardClass> newomInfos = new ArrayList<CardClass>();
	private GeraTxt txt = new GeraTxt();
//ver o powerup vote para comprovar que vc quer enviar as coisas

	public ArrayList<CardClass> ProcOM() throws SQLException, ParseException, ClassNotFoundException, IOException {// array

		int cont = 0;
		Class.forName("com.mysql.cj.jdbc.Driver");
		JSONParser parse = new JSONParser();
		Connection connect = null;
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost/trello", "root", "Ju150203**");

			ResultSet rsCli = connect.createStatement().executeQuery("SELECT * FROM jsons where Status = 'new'");

			while (rsCli.next()) {// verifica todos antes os dados antes de rodar
				cont++;// primeiro indice
				JSONObject json = (JSONObject) parse.parse(String.valueOf(rsCli.getString("json")));// pegar valor do
																									// banco do json
				JSONObject action = (JSONObject) json.get("action");

				JSONObject data = (JSONObject) action.get("data");

				JSONObject card = (JSONObject) data.get("card");
				JSONObject cover = (JSONObject) card.get("cover");
				if (action.get("type").equals("updateCard")) {// caso seja um type de att temos que verificar
					try {
						if (cover.get("color").equals("yellow")) {// se for mudado msm para amarelo a cor do cartao
							System.out.println("uma OM codigo sera realizado");
							JSONObject lista = (JSONObject) data.get("list");
							CardClass jsonifo = new CardClass(card.get("id").toString(), lista.get("id").toString());
							this.omInfos.add(jsonifo);// adiciona a uma lista com todas as OM
							this.newomInfos.add(jsonifo);
							System.out.println("om add");
						}
					} catch (Exception p) {// se noa tiver conver vamos setar a desc para o normal
						for (CardClass cc : this.newomInfos) {
							if (cc.getIdcard().equals(card.get("id"))) {
								cc.setdesc(card.get("desc").toString());
								System.out.println(cc.getDesc());
							}
						}

					}
				} else if (action.get("type").equals("updateCheckItemStateOnCard")) {
					System.out.println("ALGO ASSINALDO ________");
					try {// quando o status for complete
						ArrayList<String> idcheckslists = new ArrayList<String>();
						JSONObject checkitem = (JSONObject) data.get("checkItem");
						try {
							if (!checkitem.get("name").toString().equals(null)
									&& checkitem.get("state").equals("complete")) {
								System.out.println("FOI ASSINALDO ________");

								for (CardClass cardinfos : this.newomInfos) {
									if (cardinfos.getIdcard().equals(card.get("id"))) {
										try {
											idcheckslists = api.Idchecklist(cardinfos.getIdcard());// lista de array que
																									// o cartao tem
										} catch (Exception j) {
											for (int y = 0; api.Idchecklist(cardinfos.getIdcard()).size() > y; y++) {
												idcheckslists
														.add(api.Idchecklist(cardinfos.getIdcard()).get(y).toString());
											}
										}

										System.out.println("print tamanho" + cardinfos.getCardCL().size());
										for (CheckListJson topic : cardinfos.getCardCL()) {// ja foi inicializada e tem
																							// o valor de null
											JSONObject clid = (JSONObject) data.get("checklist");
											if (topic.getIdCL().equals(clid.get("id"))) {
												if (clid.get("name").equals("Area")) {
													topic.arrayinput(checkitem.get("name").toString());
												} // fazer com q so pode o limite e ainda ser removido caso clicado
													// errado
												else if (clid.get("name").equals("Programa")) {
													topic.setProg(checkitem.get("name").toString());

												} else if (clid.get("name").equals("Tipo")) {

													topic.setTipo(checkitem.get("name").toString());
												}
											}
										}
										// System.out.println("str ________________\n" + cardinfos.getCardCL(). );

									} // estamos em um filtro do cartao

								}

							} // o else if vai entrar quando o status for incomplete
							else if (!checkitem.get("name").toString().equals(null)
									&& checkitem.get("state").equals("incomplete")) {
								System.out.println("precisa ser removido o checklist_____________________-");
								for (CardClass cardinfos : this.newomInfos) {
									if (cardinfos.getIdcard().equals(card.get("id"))) {
										try {
											idcheckslists = api.Idchecklist(cardinfos.getIdcard());// lista de array que
																									// o cartao tem
										} catch (Exception j) {
											for (int y = 0; api.Idchecklist(cardinfos.getIdcard()).size() > y; y++) {
												idcheckslists.add(api.Idchecklist(cardinfos.getIdcard()).get(y).toString());
											}
										}
										
										for (CheckListJson topic : cardinfos.getCardCL()) {
											JSONObject clid = (JSONObject) data.get("checklist");
											JSONObject ciid = (JSONObject) data.get("checkItem");
											if (topic.getIdCL().equals(clid.get("id"))) {
												System.out.println("_>>>>primeiro if");
												System.out.println("->" + ciid.get("name"));
												System.out.println("->" + topic.getArea());
												System.out.println("->" + clid.get("name"));
												System.out.println("->" + topic.getProg());
												
												if (clid.get("name").toString().equals("Area")) {
													System.out.println("o valor de AREA FOI MUDADO");
													topic.arrayremove(ciid.get("name").toString());
												} // fazer com q so pode o limite e ainda ser removido caso clicado errado
												else if (clid.get("name").equals("Programa")
														&& topic.getProg().equals(ciid.get("name").toString())) {
													System.out.println("o valor de PROG FOI MUDADO");

													topic.setProg("");// seta dnv para nada o valor do item
												} else if (clid.get("name").equals("Tipo")
														&& topic.getTipo().equals(ciid.get("name").toString())) {
													System.out.println("o valor de PROG FOI MUDADO");

													topic.setTipo("");// seta dnv para nada o valor do item
												}
											}
											System.out.println("str ________________\n" + topic.paraStr());
										}

									} // estamos em um filtro do cartao
								}
							}

						} catch (Exception e) {
							System.out.println(e);
						}

						// final da verificaçao no banco de dados

					}

					catch (Exception p) {
						System.out.println(p);
					}

				} else if (action.get("type").equals("voteOnCard")) {// requisito para ser enviado
					for (CardClass cc : this.newomInfos) {
						if (cc.getIdcard().equals(card.get("id"))) {
							// achou o cartao que quer ser enviado
							if (cc.envio() == true && cc.isEnviado() == 1) {//se ele tiver apto para ser enviado e nunca tenha sido enviado
								System.out.println("CARTAO ESTA SENDO MOVIDO");
								String info = "";// gera a var que sera usada para pegar as infos
								info += "Nome: " + card.get("name").toString() + "\n";
								info += "Desc: " + cc.getDesc() + "\n";
								JSONObject member = (JSONObject) action.get("memberCreator");
								info += "Nome completo: " + member.get("fullName").toString() + "\n";
								info += "Nickname: " + member.get("username").toString() + "\n";
								info += "Data: " + api.getDatacard(cc.getIdcard()) + "\n";
								for (CheckListJson cj : cc.getCardCL()) {
									if (cj.getArea().isEmpty() == false) {
										info += "Area: ";
										for (String str : cj.getArea()) {
											info += str + " ";
										}

									} else if (!cj.getProg().equals("")) {
										info += "\nPrograma: " + cj.getProg() + "\n";
									} else if (!cj.getTipo().equals("")) {
										info += "\nTipo: " + cj.getTipo();
									}
								}

								txt.criadiretorio("temptestes");
								txt.criadiretorio(card.get("name").toString().replace(" ", "-"));
								txt.criararquivo();// cria o txt com o nome do cartao
								txt.escrevertrelllo(info);// escreve dentro do txt
								api.escreveComent(card.get("id").toString(), info);
								api.moverCardtoBoard(card.get("id").toString());// se tiver tudo certo envio o cartao
								for (CheckListJson idcl : cc.getCardCL()) {
									System.out.println(idcl.getIdCL());
									api.apagaChecklist(idcl.getIdCL());
								}
							}
						}
					}
				}
				PreparedStatement preparedStatement = connect
						.prepareStatement("UPDATE jsons SET Status= '-' where id = " + rsCli.getString("id"));
				preparedStatement.execute();// uso a funçao para mudar o valor do update para '-'

			}
			connect.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return this.omInfos;// volta a lista com as melhorias encontradas
	}

	public void setOmInfos(ArrayList<CardClass> omInfos) {
		this.omInfos = omInfos;
	}

	public Api getApi() {
		return api;
	}

	public void setApi(Api api) {
		this.api = api;
	}

	public ArrayList<CardClass> getOmInfos() {
		return omInfos;
	}

	public ArrayList<CardClass> getNewomInfos() {
		return newomInfos;
	}

	public void setNewomInfos() {
		this.newomInfos = this.omInfos;
	}
}
//fazer que quando o card seja movido ele seja apagado no private Arraylist