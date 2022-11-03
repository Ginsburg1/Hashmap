import java.io.File;  
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;
import edu.du.dudraw.Draw;
public class Driver {

	private static Draw draw;
	public static Scanner myReader = null;
	public static HashMap<String, Double>[] newArray = new HashMap[142]; //hashMap of all numbers 
	public static double[] numBabies = new double[142];  //stores all of the total amounts of babies in each year
	public final static int NUMFILES = 141;
	public final static int FIRSTYEAR = 1880;
	public final static int LASTYEAR = 2021;

	public static void main(String[] args) {

		//Opens the files and makes sure they exist
		for (int i = 0; i <= NUMFILES; i++) {
			String filename = "yob" + (i+FIRSTYEAR) + ".txt";
			try {
				newArray[i] = new HashMap<String, Double>();
				myReader = new Scanner(new File(filename));
			} catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}


			//reads in files and adds male and female's together
			while (myReader.hasNextLine()) {
				String word = myReader.nextLine();
				String name = word.split(",")[0];				
				Double number = (double) Integer.parseInt(word.replaceAll("[,a-zA-Z]", ""));
				numBabies[i] += number;
				if(newArray[i].containsKey(name)) {
					newArray[i].put(name, (number +newArray[i].get(name)));
				}else {
					newArray[i].put(name, number);
				}
			}
		}

		//Divides the amount of names by the toal amount of babies born 
		//that year
		for (int j = 0; j <= NUMFILES; j++) {
			Double currNum = numBabies[j];
			Iterable<Entry<String, Double>> temp = newArray[j].entrySet();
			for (Entry<String, Double> currEntry: temp) {
				Double result = currEntry.getValue() / currNum;
				newArray[j].put(currEntry.getKey(), result);
			}
		}
		//closes the reader and prints DONE
		//to indicate being done.
		myReader.close();
		System.out.println("DONE");

		do {
			String input = getUserInput();
			int year = 0;

			//End program when user wants it tp be done
			if (input.equals("x") || input.equals("X")) {
				System.exit(0);

				//outputs situation 2
			} if(input.matches(".*[0-9].*")) {
				year = Integer.parseInt(input);
				System.out.println(year);
				if (year >= FIRSTYEAR && year <= LASTYEAR) {
					DisplayPop(year);
				}else {
					System.out.println("Invalid year:" + input);
				}

				//Situation 1
			}else {
				int tester = 0;
				for (int i = 0; i <= NUMFILES; i++) {
					if (newArray[i].containsKey(input)){
						tester++;
					}
				}
				//if there are no matches
				//goes back to beginning of loop
				if (tester == 0) {
					System.out.println("Invalid name: Does not contain " + input);

					//if there are matches, calls the print method to print
				}else {
					Double max = findMax(input);
					setUpDraw(max);
					printFreq(input, findMax(input), findMaxYear(input), findFirstYear(input));
				}
			}
		}while(true);

	}



	//finds max percentage 
	public static Double findMax(String input) {
		Double max = 0.0;
		for (int i = 0; i <= NUMFILES; i++) {
			if(newArray[i].containsKey(input)) {
				Double temp = newArray[i].get(input);
				if (temp > max) {
					max = temp; 
				}
			}
		}
		return max;
	}


	//Finds max year that correlates with percent 
	public static int findMaxYear(String input) {
		Double max = 0.0;
		int year = 0;
		for (int i = 0; i <= NUMFILES; i++) {
			if(newArray[i].containsKey(input)) {
				Double temp = newArray[i].get(input);
				if (temp > max) {
					max = temp; 
					year = i+ FIRSTYEAR;
				}
			}
		}
		return year;
	}


	//finds the first year of a given name
	public static int findFirstYear(String input) {
		for (int i = 0; i <= NUMFILES; i++) {
			if(newArray[i].containsKey(input)) {
				return (i + FIRSTYEAR);
			}
		}
		return 0;
	}


	//name to display names pn graph
	//number to display numbers
	//null for combination 
	public static String getUserInput() {

		Scanner myObj = new Scanner(System.in); 
		System.out.print("name of person, year, or 'x' to exit program:");
		String temp =  myObj.nextLine();
		String number = temp.replaceAll("[a-zA-Z]", "");

		if (number.equals("")) {
			return temp;
		} else {
			return number;
		}
	} 

	//prints to draw the outputs and text
	public static void printFreq(String name, Double percent, int year, int firstY) {
		draw.clear();
		for (int i = 0; i <= 141; i++) {
			if(newArray[i].containsKey(name)){
				draw.filledRectangle(i, newArray[i].get(name) , 1, newArray[i].get(name));
			}
		}
		DecimalFormat df = new DecimalFormat("#.##");
		draw.text(60, percent * 2.15, name + " first year " + firstY);
		draw.text(60, percent * 2.05, "Maximum Frequency " + df.format(percent *100) + "%" + " in " + year);
		draw.show();
	}


	//prints the max number of names of a certain year to concel 
	public static void DisplayPop(int year) {
		System.out.print("Amount of most popular names in " + year + ": "); //O(1);
		Scanner myObj = new Scanner(System.in); //O(1);
		String input = myObj.nextLine();//O(1);
		int amountPop = 0;//O(1);
		while(!input.matches(".*[0-9].*")){ //O(1);
			System.out.print("Input valid number:");//O(1);
			input = myObj.nextLine();//O(1);
		}
		amountPop = Integer.parseInt(input); //O(1);
		Set<Entry<String, Double>> findMax = newArray[year-FIRSTYEAR].entrySet(); //O(n);
		Double maxVal = 0.0; //O(1);
		Entry<String, Double> maxEntry = null; //O(1);
		System.out.print("The " + amountPop + " most popular name in " + year + " are: "); //O(1);
		for (int i = 0; i < amountPop; i++) { //O(n);
			for (Entry<String, Double> currEntry: findMax) {  //O(1);
				Double temp = currEntry.getValue();//O(1);
				if(temp > maxVal) { //O(1);
					maxVal = temp;
					maxEntry = currEntry; //O(1);
				}
			}
			System.out.print(maxEntry.getKey().toString() + " "); //O(1);
			findMax.remove(maxEntry); //O(1);
			maxVal = 0.0;
		}
		System.out.println();
	}
	//Runs in O(n) time



	//sets up the canvas with y scale of the max *2.5 for scale.
	public static void setUpDraw(Double max) {
		draw = new Draw();
		draw.setCanvasSize(600, 600);
		draw.setXscale(0,141);  
		draw.setYscale(0, max *= 2.5);
	}


}
