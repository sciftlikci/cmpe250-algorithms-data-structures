public class House {
	public int id;
	public int remainingDuration;
	public double rating;
	public boolean isAvailable;
	
	public House(int id, int remainingDuration, double rating) {
		// constructor
		this.id = id;
		this.remainingDuration = remainingDuration;
		this.rating = rating;
		this.isAvailable = remainingDuration == 0 ? true : false;
	}
	
	public int getId() {
		// required for Comparator
		return this.id;
	}
}