import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import jakarta.servlet.RequestDispatcher;


/**
 * Servlet implementation class SchemeMatcher
 */
public class SchemeMatcher extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SchemeMatcher() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	
	
	
	
	//This method, readSchemesFromCSV(), is responsible for reading government schemes from a CSV file and returning a list of Scheme objects.
    //It initializes an empty list called schemes to store the parsed schemes.
    //It defines the filename of the CSV file as "C:\Users\LENOVO\IdeaProjects\Java\schemes.csv".
    //The try block begins, creating a BufferedReader and a CSVParser for reading and parsing the CSV file. The CSVFormat.DEFAULT.withFirstRecordAsHeader() indicates that the first row of the CSV file contains headers.
    //The try-with-resources statement ensures that both the BufferedReader and CSVParser are properly closed after use.
    private static List<Scheme> readSchemesFromCSV() {
        List<Scheme> schemes = new ArrayList<>();
        String filename = "C:\\Users\\avnig\\eclipse-workspace\\eYojanaConnectSystem\\src\\main\\java\\schemes.csv";
        try (
        		BufferedReader reader = new BufferedReader(new FileReader(filename));
           
        		CSVParser csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
            ) 
        {

            for (CSVRecord csvRecord : csvParser) { //This loop iterates through each record (row) in the CSV file obtained by the CSVParser.
                Scheme scheme = new Scheme(  //For each record, it creates a new Scheme object by extracting values for various attributes like "Scheme Code," "Scheme Name," etc.
                        csvRecord.get("Scheme Code"),
                        csvRecord.get("Scheme Name"),
                        csvRecord.get("Min age"),
                        csvRecord.get("Max age"),
                        csvRecord.get("Income"),
                        csvRecord.get("Gender"),
                        csvRecord.get("Caste"),
                        csvRecord.get("Minority Group"),
                        csvRecord.get("Physically Challenged"),
                        csvRecord.get("Beneficiaries"),
                        csvRecord.get("Benefits")
                );
                schemes.add(scheme); //The created Scheme object is then added to the schemes list.
            }
        } catch (IOException e) { //This catch block handles any IOException that might occur during the file reading or parsing process.
            e.printStackTrace();  //If an exception occurs, it prints the stack trace to the console.
        }
        return schemes;  //The method concludes by returning the list of Scheme objects (schemes) that have been read and parsed from the CSV file.
    }

    
    
    
  //This method, findBestScheme, takes a list of Scheme objects (schemes) and user input parameters such as age, income, gender, caste, etc., along with weights for each attribute.
    private static Scheme findBestScheme(List<Scheme> schemes, int age, double income, String gender, String caste, String minorityGroup, String physicallyChallenged,
                                         double ageWeight, double incomeWeight, double genderWeight, double casteWeight, double minorityWeight, double physicalChallengeWeight) {
        double bestSimilarityScore = -1.0; //bestSimilarityScore keeps track of the highest similarity score found
        Scheme bestScheme = null; //bestScheme keeps track of the scheme with the highest similarity.

        for (Scheme scheme : schemes) {  //It iterates through each Scheme object in the provided list (schemes).
            double similarityScore = scheme.calculateWeightedSimilarity(age, income, gender, caste, minorityGroup, physicallyChallenged,
                    ageWeight, incomeWeight, genderWeight, casteWeight, minorityWeight, physicalChallengeWeight); //For each scheme, it calculates the similarity score using the calculateWeightedSimilarity method of the Scheme class
            if (similarityScore > bestSimilarityScore) {
                bestSimilarityScore = similarityScore;
                bestScheme = scheme; //If the similarity score for the current scheme is greater than the current best similarity score, it updates the bestSimilarityScore and sets bestScheme to the current scheme.
            }
        }

        return bestScheme; //After iterating through all schemes, it returns the scheme with the highest similarity score.
    }
    static class Scheme { //This line defines a static inner class named Scheme. It is used to represent government schemes.
        private final String schemeCode;
        private final String schemeName;
        private final int minAge;
        private final int maxAge;
        private final double maxIncome;
        private final String gender;
        private final String caste;
        private final String minorityGroup;
        private final String physicallyChallenged;
        private final String beneficiaries;
        private final String benefits;
        private double similarityScore = 0.0; // Add a field to store the similarity score


        //This line declares a public method named setSimilarityScore. It is used to set the similarityScore field with the provided value.
        public void setSimilarityScore(double similarityScore) {
            this.similarityScore = similarityScore; 
            //This line assigns the provided similarityScore value to the similarityScore field of the scheme.
        }

        public Scheme(String schemeCode, String schemeName, String minAge, String maxAge, String maxIncome, String gender,
                      String caste, String minorityGroup, String physicallyChallenged, String beneficiaries, String benefits) {
            this.schemeCode = schemeCode;
            this.schemeName = schemeName;
            this.minAge = parseAge(minAge);
            this.maxAge = parseAge(maxAge);
            this.maxIncome = parseIncome(maxIncome);
            this.gender = gender;
            this.caste = caste;
            this.minorityGroup = minorityGroup;
            this.physicallyChallenged = physicallyChallenged;
            this.beneficiaries = beneficiaries;
            this.benefits = benefits;
   
        }
       //These lines declare public methods (getSchemeCode, getSchemeName, getBeneficiaries, getBenefits) to retrieve the values of the corresponding fields. These methods provide read-only access to the scheme information.
        public String getSchemeCode() {
            return schemeCode;
        }

        public String getSchemeName() {
            return schemeName;
        }

        public String getBeneficiaries() {
            return beneficiaries;
        }

        public String getBenefits() {
            return benefits;
        }
        

        //This line declares a public method named isMatching.
        // It checks whether the scheme matches the provided user input based on various criteria.
        //The method returns true if the scheme meets the specified conditions, such as age range, income range, gender, caste, minority group, and physical disability. Otherwise, it returns false.
        public boolean isMatching(int age, double income, String gender, String caste, String minorityGroup, String physicallyChallenged) {
            return (minAge <= age && age <= maxAge) &&
                    (maxIncome >= income) &&
                    ("both".equalsIgnoreCase(this.gender) || this.gender.equalsIgnoreCase(gender)) &&
                    (this.caste.equalsIgnoreCase(caste) || "any".equalsIgnoreCase(this.caste)) &&
                    (this.minorityGroup.equalsIgnoreCase(minorityGroup) || "any".equalsIgnoreCase(this.minorityGroup)) &&
                    (this.physicallyChallenged.equalsIgnoreCase(physicallyChallenged) || "any".equalsIgnoreCase(this.physicallyChallenged));
        }

        public double calculateWeightedSimilarity(int age, double income, String gender, String caste, String minorityGroup, String physicallyChallenged,
                                                  double ageWeight, double incomeWeight, double genderWeight, double casteWeight, double minorityWeight, double physicalChallengeWeight) {
            // Calculate the similarity score based on weighted attributes
            double ageScore = calculateAgeSimilarity(age);
            double incomeScore = calculateIncomeSimilarity(income);
            double genderScore = (this.gender.equalsIgnoreCase(gender) ? 1.0 : 0.0);
            double casteScore = (this.caste.equalsIgnoreCase(caste) ? 1.0 : 0.0);
            double minorityScore = (this.minorityGroup.equalsIgnoreCase(minorityGroup) ? 1.0 : 0.0);
            double physicalChallengeScore = (this.physicallyChallenged.equalsIgnoreCase(physicallyChallenged) ? 1.0 : 0.0);

            // Apply weight to each attribute score and normalize
            double totalWeight = ageWeight + incomeWeight + genderWeight + casteWeight + minorityWeight + physicalChallengeWeight; //The total weight is calculated as the sum of individual weights.
            double normalizedAgeScore = (ageScore / (maxAge - minAge + 1)) * ageWeight;
            double normalizedIncomeScore = (incomeScore / maxIncome) * incomeWeight;
            double normalizedGenderScore = genderScore * genderWeight;
            double normalizedCasteScore = casteScore * casteWeight;
            double normalizedMinorityScore = minorityScore * minorityWeight;
            double normalizedPhysicalChallengeScore = physicalChallengeScore * physicalChallengeWeight;

            // Sum up the weighted and normalized scores
            similarityScore = (normalizedAgeScore + normalizedIncomeScore + normalizedGenderScore +
                    normalizedCasteScore + normalizedMinorityScore + normalizedPhysicalChallengeScore) / totalWeight;

            // Ensure the final score is within the 0 to 1 range
            similarityScore = Math.max(0, Math.min(1, similarityScore));

            return similarityScore; //The method returns the calculated similarity score.
        }


        public double getSimilarityScore() {
            return similarityScore;
        }

        private double calculateAgeSimilarity(int age) { //It calculates the age similarity score based on whether the provided age falls within the specified range.
            if (age >= minAge && age <= maxAge) {
                return 1.0;
            }
            return 0.0;
        }

        private double calculateIncomeSimilarity(double income) { //It calculates the income similarity score based on whether the provided income falls within the specified range.
            if (income <= maxIncome || maxIncome == Double.POSITIVE_INFINITY) {
                return 1.0;
            }
            return 0.0;
        }

        private int parseAge(String age) { // It converts the provided age string to an integer, handling the case where "No Limit" is specified.
            return "No Limit".equalsIgnoreCase(age) ? 0 : Integer.parseInt(age);
        }

        private double parseIncome(String income) { //It converts the provided income string to a double, handling the case where "No Limit" is specified.
            return "No Limit".equalsIgnoreCase(income) ? Double.POSITIVE_INFINITY : Double.parseDouble(income);
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		List<Scheme> schemes = readSchemesFromCSV(); //This line of code calls the readSchemesFromCSV() method to read the schemes from the CSV file. The readSchemesFromCSV() method returns a list of Scheme objects.

//        Scanner scanner = new Scanner(System.in); //This line of code creates a new Scanner object to read user input.

//        System.out.print("Enter your age: "); //These lines prompt the user to enter their age and read the input as an integer.
        int age = Integer.parseInt(request.getParameter("htmlage"));
        System.out.println(age);

//        System.out.print("Enter your income: "); //These lines prompt the user to enter their income, read the input as a double, and consume the newline character left in the buffer.
        double income = Double.parseDouble(request.getParameter("htmlincome"));
//        scanner.nextLine(); // consume the newline character
        System.out.println(income);

//        if (income > 700000) {   //This block checks if the user's income is greater than 700,000. If true, it prints a message and exits the program.
//            System.out.println("You are not eligible for this process.");
//            return; // Exit the program
//        }

//        System.out.print("Enter your gender: "); //These lines prompt the user to enter their gender and read the input as a string, converting it to lowercase
        String gender = request.getParameter("gender");
        System.out.println(gender);

//        System.out.print("Enter your caste (General, OBC, SC, ST): "); //These lines prompt the user to enter their caste and read the input as a string, converting it to lowercase.
        String caste = request.getParameter("htmlcaste");
        System.out.println(caste);

//        System.out.print("Are you from a minority group? (yes/no): "); //These lines prompt the user to specify if they belong to a minority group and read the input as a string, converting it to lowercase.
        String minority =request.getParameter("minority").toLowerCase();
        System.out.println(minority);

//        System.out.print("Do you have a physical disability? (yes/no): "); //These lines prompt the user to specify if they have a physical disability and read the input as a string, converting it to lowercase.
        String physicalChallenge = request.getParameter("physcially").toLowerCase();
        System.out.println(physicalChallenge);
        
        System.out.print(age+" -------"+income+"----------"+gender+"------"+caste+"----"+minority+"--------"+physicalChallenge);
        // Define weights for attributes
        //These lines define weights for different attributes used in calculating the similarity score.
        double ageWeight = 0.8;
        double incomeWeight = 0.5;
        double genderWeight = 0.2;
        double casteWeight = 0.3;
        double minorityWeight = 0.4;
        double physicalChallengeWeight = 0.3;

        //This block iterates over the list of schemes and adds those that match the user's input to the matchingSchemes list.
        
        List<Scheme> matchingSchemes = new ArrayList<>();
        for (Scheme scheme : schemes) {
            if (scheme.isMatching(age, income, gender, caste, minority, physicalChallenge)) {
                matchingSchemes.add(scheme);
            }
        }
        
       //This block checks if there are matching schemes.
       // If true, it prints a message indicating that there are matching schemes.
     if (!matchingSchemes.isEmpty()) {
          System.out.println("\nMatching Schemes with Income Less than or Equal to Your Income:");

          //This block calculates the weighted similarity scores for all matching schemes and updates the similarity score for each scheme.
          for (Scheme scheme : matchingSchemes) {
               double similarityScore = scheme.calculateWeightedSimilarity(age, income, gender, caste, minority, physicalChallenge,
                       ageWeight, incomeWeight, genderWeight, casteWeight, minorityWeight, physicalChallengeWeight);

              scheme.setSimilarityScore(similarityScore); // Update the similarity score for the scheme
          }

           // Sort the matching schemes by similarity score in descending order
          matchingSchemes.sort(Comparator.comparing(Scheme::getSimilarityScore).reversed());
          // Print the matching schemes in a table
       // HTML structure with navbar, content, and footer
          out.println("<!DOCTYPE html>");
          out.println("<html>");
          out.println("<head>");
          out.println("<title>Matching Schemes</title>");
          out.println("<style>");
          out.println("body {");
          out.println("    background: linear-gradient(to bottom, #ff9933, #ffffff, #138808);");
          out.println("    font-family: Arial, sans-serif;");
          out.println("    margin: 0;");
          out.println("    padding: 0;");
          out.println("}");

          out.println("header {");
          out.println("    background-color: #333;");
          out.println("    padding: 10px;");
          out.println("    text-align: center;");
          out.println("    color: white;");
          out.println("}");
          
          out.println(" #home {");
          out.println("    background: transparent;");
          out.println("    border-radius: 5px;");
          out.println("    box-sizing: border-box;");
          out.println("    color: #00000;");
          out.println("    cursor: pointer;");
          out.println("    display: flex;");
          out.println("    font-family: Mija,-apple-system,BlinkMacSystemFont,Roboto,system-ui,Arial,sans-serif;");
          out.println("    font-size: 1.15em;");
          out.println("    font-weight: 100;");
          out.println("    justify-content: center;");
          out.println("    line-height: 33.4929px;");
          out.println("    padding: .5em 1em;");
          out.println("    text-align: center;");
          out.println("    text-decoration: none;");
          out.println("    text-decoration-skip-ink: auto;");
          out.println("    text-shadow: rgba(0, 0, 0, .3) 1px 1px 1px;");
          out.println("    text-underline-offset: 1px;");
          out.println("    transition: all .2s ease-in-out;");
          out.println("    user-select: none;");
          out.println("    -webkit-user-select: none;");
          out.println("    touch-action: manipulation;");
          out.println("    width: 150px;");
          out.println("    word-break: break-word;");
          out.println("    border: 0;");
          out.println("}");
          
          
          out.println(" #about {");
          out.println("    background: transparent;");
          out.println("    border-radius: 5px;");
          out.println("    box-sizing: border-box;");
          out.println("    color: #00000;");
          out.println("    cursor: pointer;");
          out.println("    display: flex;");
          out.println("    font-family: Mija,-apple-system,BlinkMacSystemFont,Roboto,system-ui,Arial,sans-serif;");
          out.println("    font-size: 1.15em;");
          out.println("    font-weight: 100;");
          out.println("    justify-content: center;");
          out.println("    line-height: 33.4929px;");
          out.println("    padding: .5em 1em;");
          out.println("    text-align: center;");
          out.println("    text-decoration: none;");
          out.println("    text-decoration-skip-ink: auto;");
          out.println("    text-shadow: rgba(0, 0, 0, .3) 1px 1px 1px;");
          out.println("    text-underline-offset: 1px;");
          out.println("    transition: all .2s ease-in-out;");
          out.println("    user-select: none;");
          out.println("    -webkit-user-select: none;");
          out.println("    touch-action: manipulation;");
          out.println("    width: 150px;");
          out.println("    word-break: break-word;");
          out.println("    border: 0;");
          out.println("}");
          
     
          out.println("nav {");
          out.println("    background-color: #f2f2f2;");
          out.println("    padding: 10px;");
          out.println("}");

          out.println("main,#recommended {");
          out.println("    margin: 20px auto;");
          out.println("    padding: 20px;");
          out.println("    border-radius: 10px;");
          out.println("    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);");
          out.println("}");

          out.println("footer {");
          out.println("    color: white;");
          out.println("    padding: 10px;");
          out.println("    text-align: center;");
          out.println("}");
          
          out.println("#apply {");
          out.println("    background: #e62143;");
          out.println("    border-radius: 11px;");
          out.println("    box-sizing: border-box;");
          out.println("    color: #fff;");
          out.println("    cursor: pointer;");
          out.println("    display: flex;");
          out.println("    font-family: Mija,-apple-system,BlinkMacSystemFont,Roboto,system-ui,Arial,sans-serif;");
          out.println("    font-size: 1.15em;");
          out.println("    font-weight: 700;");
          out.println("    justify-content: center;");
          out.println("    line-height: 33.4929px;");
          out.println("    padding: .5em 1em;");
          out.println("    text-align: center;");
          out.println("    text-decoration: none;");
          out.println("    text-decoration-skip-ink: auto;");
          out.println("    text-shadow: rgba(0, 0, 0, .3) 1px 1px 1px;");
          out.println("    text-underline-offset: 1px;");
          out.println("    transition: all .2s ease-in-out;");
          out.println("    user-select: none;");
          out.println("    -webkit-user-select: none;");
          out.println("    touch-action: manipulation;");
          out.println("    width: 150px;");
          out.println("    word-break: break-word;");
          out.println("    border: 0;");
          out.println("}");



          out.println("table {");
          out.println("    font-family: Arial, sans-serif;");
          out.println("    border-collapse: collapse;");
          out.println("    width: 100%;");
          out.println("}");

          out.println("th, td {");
          out.println("    border: 1px solid black;");
          out.println("    text-align: left;");
          out.println("    padding: 8px;");
          out.println("}");

          out.println("th {");
          out.println("    background-color: #f2f2f2;");
          out.println("}");
          out.println("</style>");
          out.println("</head>");
          
          
          out.println("<body>");
//          out.println("<header>");
          out.println("<center>");
          out.println("<h1>e-Yojana System</h1>");
          out.println("</center>");
//          out.println("</header>");

//          out.println("<nav>");
          // Add your navbar content here
          out.println("<div style='overflow: hidden;'>");
          out.println("<a id='home' href='index.html' style='float: left; margin-right: 10px;'>Home</a>");
          out.println("<a id='about' href='aboutus.html' style='float: left; margin-right: 10px;'>About us</a>");
          out.println("<a id='home' href='contactus' style='float: left; margin-right: 10px;'>Contact</a>");
          out.println("</div>");

//          out.println("</nav>");

          out.println("<main>");
          out.println("<h2>Matching Schemes:</h2>");
          out.println("<table>");
          out.println("<tr>");
          out.println("    <th>Scheme Code</th>");
          out.println("    <th>Scheme Name</th>");
          out.println("    <th>Similarity Score</th>");
          out.println("</tr>");
          
          // Print the matching schemes
           //This block prints the details of matching schemes, including the scheme code, scheme name, and similarity score.
            for (Scheme scheme : matchingSchemes) {
            	out.println("<tr>");
                out.println("    <td>" + scheme.getSchemeCode() + "</td>");
                out.println("    <td>" + scheme.getSchemeName() + "</td>");
                out.println("    <td>" + scheme.getSimilarityScore() + "</td>");
                out.println("</tr>");
               
           }
            out.println("</table>");
            out.println("</main>");

            Scheme bestScheme = matchingSchemes.get(0);

            out.println("<div id='recommended'>");
            out.println("<h2>Recommended Scheme:</h2>");
            out.println("<p>Scheme Code:"+ bestScheme.getSchemeCode()+ "</p>");
            out.println("<p>Scheme Name:"+ bestScheme.getSchemeName()+ "</p>");
            out.println("<p>Scheme Benifits:"+ bestScheme.getBenefits()+ "</p>");
            out.println("<a style='text-decoration:none' href='https://www.india.gov.in/smart-cities-mission-portal-ministry-urban-development' target='_blank'><button id='apply'>Apply Now</button></a>");
            out.println("</div>");
       
            

           out.println("<footer>");
           out.println("<p>&copy; eYojana Connect System || UPES project</p>");
           out.println("</footer>");

           out.println("</body>");
           out.println("</html>");
          
     } else {
           out.print("\nNo matching schemes found."); // If there are no matching schemes, it prints a message indicating that no matching schemes were found.
      }

       
	doGet(request, response);
       
    
}
}

