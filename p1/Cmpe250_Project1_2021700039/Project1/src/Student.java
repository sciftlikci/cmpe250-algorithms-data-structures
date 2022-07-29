public class Student {
	public int id;
	public String name;
	public int remainingDuration;
	public double ratingThresh;
	
	public Student(int id, String name, int remainingDuration, double ratingThresh) {
		// constructor
		this.id = id;
		this.name = name;
		this.remainingDuration = remainingDuration;
		this.ratingThresh = ratingThresh;
	}
	
	public int getId() {
		// required for Comparator
		return this.id;
	}
}