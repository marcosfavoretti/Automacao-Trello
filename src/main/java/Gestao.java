import java.util.Date;

public class Gestao {// moldar a classe com as variaveis e o metodo de paraString
	private String nome;
	private String area;
	private String tipo;
	private String programacao;
	private String descricao;
	private String nomecomp;
	private String nicktrello;
	private String data;

	public Gestao(String nome , String area, String tipo, String programacao, String descricao, String nomecomp, String nicktrello,
			String data) {
		
		this.nome = nome;
		this.area = area;
		this.data = data;
		this.descricao = descricao;
		this.nicktrello = nicktrello;
		this.nomecomp = nomecomp;
		this.programacao = programacao;
		this.tipo = tipo;

	}

	public String paraString() {//passar o String para a gravacao do txt

		String info = "";
			
			info +="Nome-"+this.nome+";\n";
			info+="Area-"+this.area+";\n";
			info+="Tipo-"+this.tipo+";\n";
			info+="Programacao-"+this.programacao+";\n";
			info+="Descricao-"+this.descricao+";\n";
			info+="Nome Completo-"+nomecomp+";\n";
			info+="Nick no trello-"+nicktrello+";\n";
			info+="Data de criacao-"+data+";\n";

		return info;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getProgramacao() {
		return programacao;
	}

	public void setProgramacao(String programacao) {
		this.programacao = programacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomecomp() {
		return nomecomp;
	}

	public void setNomecomp(String nomecomp) {
		this.nomecomp = nomecomp;
	}

	public String getNicktrello() {
		return nicktrello;
	}

	public void setNicktrello(String nicktrello) {
		this.nicktrello = nicktrello;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
