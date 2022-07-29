public class Event {
	public String eventType;
	public double eventDuration;
	public double arrivalTime;
	public int playerId;
	public int staffId;
	
	public Event(String eventType, int playerId, 
			double arrivalTime, double eventDuration) {
		
		this.eventType = eventType;
		this.playerId = playerId;
		this.arrivalTime = arrivalTime;
		this.eventDuration = eventDuration;
	}
	
	public Event(String eventType, int playerId, 
			double arrivalTime, double eventDuration, int staffId) {
		
		this.eventType = eventType;
		this.playerId = playerId;
		this.arrivalTime = arrivalTime;
		this.eventDuration = eventDuration;
		this.staffId = staffId;
	}
	
	
	public double getArrivalTime() {
		// required for Comparator
		return this.arrivalTime;
	}
	
	
	public int getPlayerId() {
		return this.playerId;
	}
}
