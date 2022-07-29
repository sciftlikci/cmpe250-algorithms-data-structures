import java.io.*;
import java.util.*;


class InputArgs{
	// class for returning multiple arguments from readInput method
	public TreeSet<House> houseSet;
	public TreeSet<Student> studentSet;
	public int maxSemester;
	
	public InputArgs(TreeSet<House> houseSet, 
			TreeSet<Student> studentSet, int maxSemester) {
		// constructor
		this.houseSet = houseSet;
		this.studentSet = studentSet;
		this.maxSemester = maxSemester;
	}
}


public class project1main {

	public static InputArgs readInput(String inputPath) {
		// read input method
		
		// Collection for houses, sorted by id
		TreeSet<House> houseSet = new TreeSet<House>(Comparator.comparing(House::getId));

		// Collection for students, sorted by id
		TreeSet<Student> studentSet = new TreeSet<Student>(Comparator.comparing(Student::getId));
		
		// saving highest duration from the input file 
		// program will run up to this duration
		int maxSemester = 0;
		
		System.out.println("Reading input file...");

		try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {

		     String strCurrentLine;

		     while ((strCurrentLine = br.readLine()) != null) {
		    	 String[] splitted = strCurrentLine.split(" ");
	    	 	
		    	 // read input line of a student
		    	 if (splitted[0].charAt(0) == 's') {
		    		 int studId = Integer.parseInt(splitted[1]);
		    		 String studName = splitted[2];
		    		 int studDuration = Integer.parseInt(splitted[3]);
		    		 double studRatingThresh = Double.parseDouble(splitted[4]);
	    		 	 
		    		 // comparing & updating the max number of semesters
		    		 if (maxSemester < studDuration) {
		    			 maxSemester = studDuration;
		    		 }

		    		 Student currentStudent = new Student(studId,
									 					  studName,
									 					  studDuration,
									 					  studRatingThresh);
		    		 studentSet.add(currentStudent);
		    	 }
	    	 	
		    	 // read input line of a house
		    	 else {
		    		 int houseId = Integer.parseInt(splitted[1]);
		    		 int houseDuration = Integer.parseInt(splitted[2]);
		    		 double houseRating = Double.parseDouble(splitted[3]);
		    		 
		    		 House currentHouse = new House(houseId,
		    				 						houseDuration,
		    				 						houseRating);
		    		 houseSet.add(currentHouse);
		    	 }
		     }
		}

		catch (IOException e) {
			 System.out.println("Catch - An error occurred.");
		     e.printStackTrace();
		}
		
		System.out.println("Done.");
		
		// returning multiple arguments
		InputArgs inputs = new InputArgs(houseSet, studentSet, maxSemester);
		
		return inputs;
	}


	public static void writeOutput(String outputPath, TreeSet<Student> outStudents) {
		try {
			// create output file
		    File myOutputFile = new File(outputPath);
		    
		    if (myOutputFile.createNewFile()) {
		    	System.out.println("Output file created: " + outputPath);
		    }
		    
		    FileWriter myWriter = new FileWriter(outputPath);
		    
		    String newLine = System.getProperty("line.separator");
		    Iterator<Student> studentIter = outStudents.iterator();
		    
		    System.out.println("Writing results to the output file...");
		    
		    // write non-allocated students to output file
		    while (studentIter.hasNext()) {
		    	String nextName = studentIter.next().name;
	    		myWriter.write(nextName + newLine);
		    }
		    
		    myWriter.close();

		} catch (IOException e) {
			System.out.println("Catch - An error occurred.");
		    e.printStackTrace();
		}
		
		System.out.println("Done.");
	}

	public static TreeSet<Student> allocateHouses(TreeSet<House> houseSet,
			TreeSet<Student> studentSet, TreeSet<Student> outStudentSet, int remainingSemester) {

		// base case
		if (remainingSemester == 0) {
			return outStudentSet;
		}
		
		else {
			// allocate students to available houses
			Iterator<Student> studentIter = studentSet.iterator();
			
			while (studentIter.hasNext()) {
				Student currStudent = studentIter.next();
				
				// remove graduated students before allocation
				if (currStudent.remainingDuration == 0) {
					outStudentSet.add(currStudent);					
					studentIter.remove();
				}
				
				else {
					// student is not graduated yet, try to allocate a house
					Iterator<House> houseIter = houseSet.iterator();
					
					while (houseIter.hasNext()) {
						House currHouse = houseIter.next();
						
						// allocate student to current available house, if rating is also OK
						if (currHouse.isAvailable & currStudent.ratingThresh <= currHouse.rating) {
							currHouse.isAvailable = false;
							currHouse.remainingDuration = currStudent.remainingDuration;
							
							// remove allocated student from the waiting list
							studentIter.remove();
							break;
						}
					}
				}
				
				// reduce remaining duration for the student
				currStudent.remainingDuration -= 1;
			}
			
			Iterator<House> houseIter2 = houseSet.iterator();
			
			// arrange all houses' availability
			while (houseIter2.hasNext()) {
				House currHouse = houseIter2.next();
				
				// reduce remainingDuration for the house
				currHouse.remainingDuration -= 1;

				// free up the house when time is up.
				if (currHouse.remainingDuration <= 0) {
					currHouse.isAvailable = true;
					currHouse.remainingDuration = 0;
				}
			}
		}
		
		// reduce number of remaining semester 
		remainingSemester--;

		return allocateHouses(houseSet, studentSet,
				outStudentSet, remainingSemester);
	}


	public static void main(String[] args) {
		// read & collect input
		InputArgs results = readInput(args[0]);
		
		// get input collections
		TreeSet<House> houseSet = results.houseSet;		
		TreeSet<Student> studentSet = results.studentSet;
		int maxSemester = results.maxSemester;

		// track non-allocated students
		TreeSet<Student> outStudentSet = new TreeSet<Student>(Comparator.comparing(Student::getId));

		// iterate over the semesters & allocate houses recursively
		TreeSet<Student> outStudents = allocateHouses(houseSet, studentSet,
				outStudentSet, maxSemester);

		// write results to the output file
		writeOutput(args[1], outStudents);
		
		System.out.println("Program completed.");
	}
}
