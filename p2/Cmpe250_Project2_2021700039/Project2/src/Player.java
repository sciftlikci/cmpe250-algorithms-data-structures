import java.util.Stack;

public class Player {
	public int id;
	public int skillLevel;
	public int massageCount = 0; // at most 3, else, "invalid attempt"
	
	// player state attributes
	public boolean isInProcess = false;
	public boolean isInQueue = false;
	public double lastTrainDuration;
	public Stack<Double> trainingDurations;
	public Stack<Double> massageDurations;
	
	// required for stats calculation
	public double physioArrivalTime;
	public double massageArrivalTime;
	public double trainArrivalTime;
	public double trainPhysioStart;
	public double minMassageWaitTime = 0.0;
	public double maxPhysioWaitTime = 0.0;
	
	public Player(int id, int skillLevel) {
		this.id = id;
		this.skillLevel = skillLevel;
		this.trainingDurations = new Stack<Double>();
		this.massageDurations = new Stack<Double>();
	}
	
	public int getId() {
		// required for Comparator
		return this.id;
	}
	
	public int getSkill() {
		// required for Comparator
		return this.skillLevel;
	}
	
	public double getTrainingDuration() {
		// required for Comparator
		return this.lastTrainDuration;
	}
	
	public double getTrainArrivalTime() {
		// required for Comparator
		return this.trainArrivalTime;
	}
	
	public double getPhysioArrivalTime() {
		// required for Comparator
		return this.physioArrivalTime;
	}
	
	public double getMassageArrivalTime() {
		// required for Comparator
		return this.massageArrivalTime;
	}
	
	public int getMassageCount() {
		// required for Comparator
		return this.massageCount;
	}
	
	public double getMinMassageWaitTime() {
		return this.minMassageWaitTime;
	}
	
	public double getMaxPhysioWaitTime() {
		return this.maxPhysioWaitTime;
	}
}

