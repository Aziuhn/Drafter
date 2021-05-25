package drafter;

public class Card {
	private int id;
	private String name;
	private String rarity;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRarity() {
		return rarity;
	}
	public void setRarity(String rarity) {
		this.rarity = rarity;
	}
	
	@Override
	public String toString() {
		return "["+id+", "+name+", "+rarity+"]\n";
	}
	
	
}
