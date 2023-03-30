import java.util.ArrayList;

public class CardClass {
	private boolean ci = false;
	private int enviado = 0;
	private String desc = "";
	private String idlist = "";
	private String idcard = "";
	private ArrayList<CheckListJson> cardCL = new ArrayList<CheckListJson>();// cada um cartao tera 3 desses

	public CardClass(String idcard, String idlist) {
		this.idcard = idcard;
		this.idlist = idlist;
	}

	public boolean envio() {// verifica se ja pode ser enviado ou ainda nao
		// so vai poder ser envado quando tiver tudo selecionado
		boolean resp = false;
		int cont = 0;
		if (!desc.equals("") && !idlist.equals("") && !idcard.equals("")) {//
			System.out.println("tamanho de cardcL"+ cardCL.size());
			for (CheckListJson confirm : cardCL) {
				if (confirm.getArea().isEmpty() == false|| !confirm.getProg().equals("") || !confirm.getTipo().equals("")) {
					cont++;
				}
				if (cont == 3) {
					resp = true;
					this.enviado ++;//seta como true o envio
				}
			}
		}
		System.out.println("valor do cardclass" +resp);
		return resp;
	}



	public void arrayinput(CheckListJson checklist) {
		cardCL.add(checklist);
	}

	public String getIdlit() {
		return idlist;
	}

	public void setIdlist(String idlit) {
		this.idlist = idlit;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public ArrayList<CheckListJson> getCardCL() {
		return cardCL;
	}

	public void setCardCL(ArrayList<CheckListJson> cardCL) {
		this.cardCL = cardCL;
	}

	public String getIdlist() {
		return idlist;
	}

	public void setdesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public String paraStr() {
		String info = "";
		info += this.idcard + "\n";
		info += this.idlist + "\n";
		for (CheckListJson cljson : this.cardCL) {
			info += cljson.paraStr() + "\n";
		}
		return info;
	}

	public boolean isCi() {
		return ci;
	}

	public void setCi(boolean ci) {
		this.ci = ci;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int isEnviado() {
		return enviado;
	}

	public void setEnviado(int enviado) {
		this.enviado = enviado;
	}
}
