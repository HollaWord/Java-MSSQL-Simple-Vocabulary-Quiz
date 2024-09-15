package function;

public class Elementary {


	@Override
	public String toString() {
		return "Elementary [Id=" + Id + ", capital=" + capital + ", word=" + word + ", categories=" + categories
				+ ", explain=" + explain + ", level=" + level + "]";
	}
	
	public String statement() {
		String s = word + " " + categories + "\n解釋：" + explain;
		return s;
		
	}
	
	private int Id;
	private String capital;
	private String word;
	private String categories;
	private String explain;
	private String level;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getCapital() {
		return capital;
	}
	public void setCapital(String capital) {
		this.capital = capital;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}

}
