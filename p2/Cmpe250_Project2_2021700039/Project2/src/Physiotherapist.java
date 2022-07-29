public class Physiotherapist {
	public int id;
	public boolean isAvailable = true;
	public double serviceTime;
	
	public Physiotherapist(int id, double serviceTime) {
		this.id = id;
		this.serviceTime = serviceTime;
	}
	
	public double getId() {
		// required for Comparator
		return this.id;
	}
}
