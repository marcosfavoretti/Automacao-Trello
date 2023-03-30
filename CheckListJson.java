import java.util.ArrayList;

public class CheckListJson {
	private String tipo = "";
	private String prog = "";
	private ArrayList<String> area = new ArrayList<String>();
	private String idCL;//id do check list verficio qual eu quero e depois seto os campos 
	
	public CheckListJson(String idCL) {
		this.idCL = idCL;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getProg() {
		return prog;
	}
	public void setProg(String prog) {
		this.prog = prog;
	}
	public ArrayList<String> getArea() {
		return area;
	}
	public void setArea(ArrayList<String> area) {
		this.area = area;
	}
	public String getIdCL() {
		return idCL;
	}
	public void setIdCL(String idCL) {
		this.idCL = idCL;
	}
	
	public void arrayinput(String nome) {
		area.add(nome);
	}
	
	public void remove(int idxnametopic) {
		this.area.remove(idxnametopic);//remove o nome que foi passado 

	}
	
	public void arrayremove(String nametopic) {
		int indx = 0;
		for(String str: this.area) {
			if(str.replace("[", "").replace("]", "").equals(nametopic)) {
				System.out.println(indx);
				remove(indx);
				System.out.println("foi removido" );
			}
			indx ++;
		}
	}
	
	public String paraStr() {
		String info = "";
		info += this.idCL + "\n";
		info += this.prog+ "\n";
		info += this.tipo+ "\n";
		for(String str : this.area) {
			info+=str +"\n";
		}
		return info;
	}




	
}
