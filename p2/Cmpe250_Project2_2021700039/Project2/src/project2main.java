import java.io.*;
import java.util.*;


class EventManager{
	public PriorityQueue<Event> eventQ = new PriorityQueue<Event> (Comparator.comparing(Event::getArrivalTime).thenComparing(Event::getPlayerId));
	public PriorityQueue<Player> trainingQ = new PriorityQueue<Player>(Comparator.comparing(Player::getTrainArrivalTime).thenComparing(Player::getId));
	public PriorityQueue<Player> physioQ = new PriorityQueue<Player>(Comparator.comparing(Player::getTrainingDuration).reversed().thenComparing(Comparator.comparing(Player::getPhysioArrivalTime)).thenComparing(Player::getId));
	public PriorityQueue<Player> massageQ = new PriorityQueue<Player>(Comparator.comparing(Player::getSkill).reversed().thenComparing(Comparator.comparing(Player::getMassageArrivalTime)).thenComparing(Player::getId));

	public TreeSet<Trainer> trainerSet = new TreeSet<Trainer> (Comparator.comparing(Trainer::getId));
	public TreeSet<Masseuse> masseuseSet = new TreeSet<Masseuse> (Comparator.comparing(Masseuse::getId));
	public TreeSet<Physiotherapist> physioSet = new TreeSet<Physiotherapist> (Comparator.comparing(Physiotherapist::getId));
	public HashMap<Integer, Player> playerMap = new HashMap<Integer, Player> ();

	public int availableTrainerCount = 0;
	public int availableMasseuseCount = 0;
	public int availablePhysioCount = 0;
}


class Statistics {
	public int trainSessionCount = 0;
	public int physioSessionCount = 0;
	public int massageSessionCount = 0;

	public double trainSessionTime = 0.0;
	public double physioSessionTime = 0.0;
	public double massageSessionTime = 0.0;
	public double totalTurnaroundTime = 0.0;

	public int canceledAttempt = 0;
	public int invalidAttempt = 0;
	public int maxLenTrainQ = 0;
	public int maxLenPhysioQ = 0;
	public int maxLenMassageQ = 0;

	public double totalWaitTQPlayer = 0.0;
	public double totalWaitPQPlayer = 0.0;
	public double totalWaitMQPlayer = 0.0;
	public List<Number> outputList = new ArrayList<Number>();
}


public class project2main {
	public static EventManager readInput(String inputPath) {
		// read input method

		EventManager eventManager = new EventManager();
		System.out.println("Reading input file...");

		try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {

		     String strCurrentLine;
		     int playerNum = 0;
		     int eventNum = 0;
		     int physioNum = 0;
		     int trainerNum = 0;
		     int masseuseNum = 0;
		     int lineCounter = 1;

		     while ((strCurrentLine = br.readLine()) != null) {

		    	 String[] splitted = strCurrentLine.split(" ");

		    	 // read number of players
		    	 if (lineCounter == 1) {
		    		 playerNum = Integer.parseInt(splitted[0]);
		    	 }

		    	 // read players
		    	 else if (lineCounter < playerNum + 2) {
		    		 int currPlayerId = Integer.parseInt(splitted[0]);
		    		 int currPlayerSkill = Integer.parseInt(splitted[1]);

    				 Player currPlayer = new Player(currPlayerId, currPlayerSkill);
    				 eventManager.playerMap.put(currPlayerId, currPlayer);
		    	 }

		    	 // number of events
		    	 else if (lineCounter == playerNum + 2) {
		    		 eventNum = Integer.parseInt(splitted[0]);
		    	 }


		    	 // read events
		    	 else if (lineCounter < playerNum + eventNum + 3) {
		    		 char eventType = splitted[0].charAt(0);
		    		 String eventTypeModified = "";

		    		 int playerId = Integer.parseInt(splitted[1]);
		    		 double arrivalTime = Double.parseDouble(splitted[2]);
		    		 double eventDuration = Double.parseDouble(splitted[3]);

		    		 if (eventType == 't') {
		    			 eventTypeModified = "ts";
		    		 }

		    		 else {
		    			 eventTypeModified = "ms";
		    		 }

    				 Event currEvent = new Event(eventTypeModified,
    						 playerId, arrivalTime, eventDuration);

    				 eventManager.eventQ.add(currEvent);
		    	 }


		    	 // read physiotherapists
		    	 else if (lineCounter == playerNum + eventNum + 3) {
		    		 physioNum = Integer.parseInt(splitted[0]);

		    		 // for loop, create physios one-by-one.
		    		 for (int i = 0; i < physioNum; i++) {
		    			 double serviceTime = Double.parseDouble(splitted[i+1]);
		    			 Physiotherapist currPhysio = new Physiotherapist(i, serviceTime);
		    			 eventManager.physioSet.add(currPhysio);
		    			 eventManager.availablePhysioCount += 1;
		    		 }
		    	 }


		    	 // read training & masseuse
		    	 else if (lineCounter == playerNum + eventNum + 4) {
		    		 trainerNum = Integer.parseInt(splitted[0]);
		    		 masseuseNum = Integer.parseInt(splitted[1]);

		    		 for (int i = 0; i < trainerNum; i++) {
		    			 Trainer currTrainer = new Trainer(i);
		    			 eventManager.trainerSet.add(currTrainer);
		    			 eventManager.availableTrainerCount += 1;
		    		 }

		    		 for (int i = 0; i < masseuseNum; i++) {
		    			 Masseuse currMasseuse = new Masseuse(i);
		    			 eventManager.masseuseSet.add(currMasseuse);
		    			 eventManager.availableMasseuseCount += 1;
		    		 }
		    	 }

		    	 lineCounter += 1;
		     }
		}

		catch (IOException e) {
			 System.out.println("Catch error.");
		     e.printStackTrace();
		}

		System.out.println("Done.");
		return eventManager;
	}


	public static void writeOutput(String outputPath, Statistics stats) {
		try {
			// create output file
		    File myOutputFile = new File(outputPath);

		    if (myOutputFile.createNewFile()) {
		    	System.out.println("Output file created: " + outputPath);
		    }

		    FileWriter myWriter = new FileWriter(outputPath);
		    String newLine = System.getProperty("line.separator");
		    System.out.println("Writing results to the output file...");

		    for (int i = 0; i < stats.outputList.size(); i++) {
		    	if (i == 10) {
		    		break;
		    	}
	    		myWriter.write(stats.outputList.get(i) + newLine);
		    }

		    myWriter.write(stats.outputList.get(10) + " " + stats.outputList.get(11) + newLine);
		    myWriter.write(stats.outputList.get(12) + " " + stats.outputList.get(13) + newLine);
		    myWriter.write(stats.outputList.get(14) + newLine);
		    myWriter.write(stats.outputList.get(15) + newLine);
		    myWriter.write(stats.outputList.get(16) + newLine);
		    myWriter.close();

		} catch (IOException e) {
			System.out.println("Error occured.");
		    e.printStackTrace();
		}

		System.out.println("Done.");
	}

	public static double roundVal(double val) {
		return Math.round(val * 1000.0) / 1000.0;
	}


	public static void main(String[] args) {
		EventManager eventManager = readInput(args[0]);
		String outputPath = args[1];

		Statistics stats = new Statistics();
		double clock = 0.0;

		// iterate over events
		while (!eventManager.eventQ.isEmpty()) {
			// save current clock & events
			clock = eventManager.eventQ.peek().arrivalTime;
			Event currEvent = eventManager.eventQ.poll();
			Player currPlayer = eventManager.playerMap.get(currEvent.playerId);

			// reject invalid attempt for massage
			if (currEvent.eventType == "ms" && currPlayer.massageCount == 3) {
				stats.invalidAttempt += 1;
			}

			// reject in-process player requests
			else if ((currPlayer.isInProcess || currPlayer.isInQueue) && Arrays.asList("ts", "ms").contains(currEvent.eventType)) {
				stats.canceledAttempt += 1;
			}

			// PHYSIOTHERAPY DONE ROUTINE
			else if (currEvent.eventType == "pd") {
				// update related stats
				stats.totalTurnaroundTime += clock - currPlayer.trainPhysioStart;
				currPlayer.isInProcess = false;

				// make busy physiotherapist free
				Iterator<Physiotherapist> iterPhysio = eventManager.physioSet.iterator();

				while(iterPhysio.hasNext()) {
					Physiotherapist busyPhysio = iterPhysio.next();

					if (busyPhysio.id == currEvent.staffId) {
						// update stats
						stats.physioSessionTime += busyPhysio.serviceTime;

						// check for waiting players in the queue
						if (!eventManager.physioQ.isEmpty()) {
							Player queuePlayer = eventManager.physioQ.poll();

							// update waiting stats
							double physioWait = clock - queuePlayer.physioArrivalTime;
							stats.totalWaitPQPlayer += physioWait;

							// player in physioQ gets physiotherapy
							queuePlayer.isInProcess = true;
							queuePlayer.isInQueue = false;
							queuePlayer.maxPhysioWaitTime += physioWait;

							// add new Physiotherapy done event
							Event newEvent = new Event("pd", queuePlayer.id,
									clock + busyPhysio.serviceTime, busyPhysio.serviceTime, busyPhysio.id);

							eventManager.eventQ.add(newEvent);
							stats.physioSessionCount += 1;
						}

						else {
							busyPhysio.isAvailable = true;
							eventManager.availablePhysioCount += 1;
						}

						break;
					}
				}
			}


			else if (currEvent.eventType == "md") {
				// update related stats
				stats.massageSessionCount += 1;
				stats.massageSessionTime += currEvent.eventDuration;
				currPlayer.isInProcess = false;

				// make busy masseuse free
				Iterator<Masseuse> iterMasseuse = eventManager.masseuseSet.iterator();

				while(iterMasseuse.hasNext()) {
					Masseuse busyMasseuse = iterMasseuse.next();

					if (busyMasseuse.id == currEvent.staffId) {
						if (!eventManager.massageQ.isEmpty()) {
							Player queuePlayer = eventManager.massageQ.poll();

							// update related stats
							double massageWait = clock - queuePlayer.massageArrivalTime;
							stats.totalWaitMQPlayer += massageWait;
							queuePlayer.minMassageWaitTime += massageWait;

							// player in massageQ gets massage
							queuePlayer.isInQueue = false;
							queuePlayer.isInProcess = true;
							queuePlayer.massageCount += 1;
							Double eventDuration = queuePlayer.massageDurations.pop();

							// add new "massage done" event
							Event newEvent = new Event("md", queuePlayer.id,
									clock + eventDuration, eventDuration, busyMasseuse.id);

							eventManager.eventQ.add(newEvent);
						}

						else {
							busyMasseuse.isAvailable = true;
							eventManager.availableMasseuseCount += 1;
						}

						break;
					}
				}
			}

			else {
				// adjust player state
				currPlayer.isInQueue = true;
				currPlayer.isInProcess = false;

				// PHYSIOTHERAPY START ROUTINE
				if (currEvent.eventType == "ps") {
					currPlayer.physioArrivalTime = clock;
					eventManager.physioQ.add(currPlayer);

					Iterator<Trainer> iterTrainer = eventManager.trainerSet.iterator();

					while(iterTrainer.hasNext()) {
						Trainer busyTrainer = iterTrainer.next();

						if (busyTrainer.id == currEvent.staffId) {
							if (!eventManager.trainingQ.isEmpty()) {
								Player queuePlayer = eventManager.trainingQ.poll();

								// update stats
								stats.totalWaitTQPlayer += clock - queuePlayer.trainArrivalTime;

								// player in the trainingQ gets training
								queuePlayer.isInProcess = true;
								queuePlayer.isInQueue = false;
								Double eventDuration = queuePlayer.trainingDurations.pop();
								queuePlayer.lastTrainDuration = eventDuration;

								// create new "training done" event
								Event newEvent = new Event("ps", queuePlayer.id,
										clock + eventDuration, eventDuration, busyTrainer.id);

								eventManager.eventQ.add(newEvent);
							}

							else {
								// make busy trainer free
								busyTrainer.isAvailable = true;
								eventManager.availableTrainerCount += 1;
							}

							break;
						}
					}

					stats.trainSessionCount += 1;
					stats.trainSessionTime += currEvent.eventDuration;


					if (eventManager.availablePhysioCount > 0) {
						Iterator<Physiotherapist> iterPhysio = eventManager.physioSet.iterator();

						while(iterPhysio.hasNext()) {
							Physiotherapist availablePhysio = iterPhysio.next();

							if (availablePhysio.isAvailable) {
								// select player from queue
								Player queuePlayer = eventManager.physioQ.poll();

								// update related stats
								double physioWait = clock - queuePlayer.physioArrivalTime;
								stats.totalWaitPQPlayer += physioWait;

								// update info of filled physiotherapist
								availablePhysio.isAvailable = false;
								eventManager.availablePhysioCount -= 1;

								// player gets physiotherapy
								queuePlayer.isInProcess = true;
								queuePlayer.isInQueue = false;
								queuePlayer.maxPhysioWaitTime += physioWait;

								// create new PD event
								Event newEvent = new Event("pd", queuePlayer.id,
										clock + availablePhysio.serviceTime, availablePhysio.serviceTime, availablePhysio.id);

								eventManager.eventQ.add(newEvent);
								stats.physioSessionCount += 1;
								break;
							}
						}
					}

					// update stats
					if (eventManager.physioQ.size() > stats.maxLenPhysioQ) {
						stats.maxLenPhysioQ = eventManager.physioQ.size();
					}
				}

				// MASSAGE START ROUTINE
				else if (currEvent.eventType == "ms") {
					currPlayer.massageArrivalTime = clock;
					currPlayer.massageDurations.push(currEvent.eventDuration);
					eventManager.massageQ.add(currPlayer);

					if (eventManager.availableMasseuseCount > 0) {
						Iterator<Masseuse> iterMasseuse = eventManager.masseuseSet.iterator();

						while(iterMasseuse.hasNext()) {
							Masseuse availableMasseuse = iterMasseuse.next();

							if (availableMasseuse.isAvailable) {
								Player queuePlayer = eventManager.massageQ.poll();
								double massageWait = clock - queuePlayer.massageArrivalTime;

								// update info of filled masseuse
								availableMasseuse.isAvailable = false;
								eventManager.availableMasseuseCount -= 1;

								// player in massageQ gets massage
								queuePlayer.isInQueue = false;
								queuePlayer.isInProcess = true;
								queuePlayer.minMassageWaitTime += massageWait;
								queuePlayer.massageCount += 1;

								// create new "massage done" event
								Event newEvent = new Event("md", queuePlayer.id,
										clock + currEvent.eventDuration, currEvent.eventDuration, availableMasseuse.id);

								eventManager.eventQ.add(newEvent);
								break;
							}
						}
					}

					// update stats
					if (eventManager.massageQ.size() > stats.maxLenMassageQ) {
						stats.maxLenMassageQ = eventManager.massageQ.size();
					}
				}

				// TRAINING START ROUTINE
				else if (currEvent.eventType == "ts") {
					currPlayer.trainArrivalTime = clock;
					currPlayer.trainingDurations.push(currEvent.eventDuration);
					currPlayer.trainPhysioStart = clock;
					eventManager.trainingQ.add(currPlayer);

					if (eventManager.availableTrainerCount > 0) {
						Iterator<Trainer> iterTrainer = eventManager.trainerSet.iterator();

						while(iterTrainer.hasNext()) {
							Trainer availableTrainer = iterTrainer.next();

							if (availableTrainer.isAvailable) {
								Player queuePlayer = eventManager.trainingQ.poll();
								stats.totalWaitTQPlayer += clock - queuePlayer.trainArrivalTime;

								// update state of filled trainer
								availableTrainer.isAvailable = false;
								eventManager.availableTrainerCount -= 1;

								// player in trainingQ gets training
								queuePlayer.isInProcess = true;
								queuePlayer.isInQueue = false;
								queuePlayer.lastTrainDuration = currEvent.eventDuration;

								// create new "physiotherapy start" event
								Event newEvent = new Event("ps", queuePlayer.id,
										clock + currEvent.eventDuration, currEvent.eventDuration, availableTrainer.id);

								eventManager.eventQ.add(newEvent);
								break;
							}
						}
					}

					// update stats
					if (eventManager.trainingQ.size() > stats.maxLenTrainQ) {
						stats.maxLenTrainQ = eventManager.trainingQ.size();
					}
				}
			}
		}

		// save stats
		stats.outputList.add(stats.maxLenTrainQ);
		stats.outputList.add(stats.maxLenPhysioQ);
		stats.outputList.add(stats.maxLenMassageQ);

		stats.outputList.add(roundVal(stats.totalWaitTQPlayer / stats.trainSessionCount));
		stats.outputList.add(roundVal(stats.totalWaitPQPlayer / stats.physioSessionCount));
		stats.outputList.add(roundVal(stats.totalWaitMQPlayer / stats.massageSessionCount));

		stats.outputList.add(roundVal(stats.trainSessionTime / stats.trainSessionCount));
		stats.outputList.add(roundVal(stats.physioSessionTime / stats.physioSessionCount));
		stats.outputList.add(roundVal(stats.massageSessionTime / stats.massageSessionCount));
		stats.outputList.add(roundVal(stats.totalTurnaroundTime / stats.trainSessionCount));

		PriorityQueue<Player> statPhysioQ = new PriorityQueue<Player>(Comparator.comparing(Player::getMaxPhysioWaitTime).reversed().thenComparing(Comparator.comparing(Player::getId)));
		PriorityQueue<Player> statMassageQ = new PriorityQueue<Player>(Comparator.comparing(Player::getMinMassageWaitTime).thenComparing(Comparator.comparing(Player::getId)));
		Iterator<HashMap.Entry<Integer, Player>> iterPlayer = eventManager.playerMap.entrySet().iterator();

		while(iterPlayer.hasNext()) {
			HashMap.Entry<Integer, Player> currPlayerEntry = iterPlayer.next();
			Player currPlayer = currPlayerEntry.getValue();
			statPhysioQ.add(currPlayer);

			if(currPlayer.massageCount == 3) {
				statMassageQ.add(currPlayer);
			}
		}

		if (statPhysioQ.peek() != null) {
			stats.outputList.add(statPhysioQ.peek().id);
			stats.outputList.add(roundVal(statPhysioQ.peek().maxPhysioWaitTime));
		}


		else {
			stats.outputList.add(Collections.min(eventManager.playerMap.keySet()));
			stats.outputList.add(0.000);
		}


		if (statMassageQ.peek() != null) {
			stats.outputList.add(statMassageQ.peek().id);
			stats.outputList.add(roundVal(statMassageQ.peek().minMassageWaitTime));
		}

		else {
			stats.outputList.add(-1.0);
			stats.outputList.add(-1.000);
		}

		stats.outputList.add(stats.invalidAttempt);
		stats.outputList.add(stats.canceledAttempt);
		stats.outputList.add(roundVal(clock));
		writeOutput(outputPath, stats);
	}
}
