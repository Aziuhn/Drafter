package drafter;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CleanMTGGoldfishList {

	public static void main(String[] args) throws IOException {
		FileInputStream MTGGoldfishList = new FileInputStream("./src/drafter/StrixhavenListGoldfish.txt");
		FileWriter newList = new FileWriter("./src/drafter/StrixhavenList.txt");
		Scanner sc = new Scanner(MTGGoldfishList);
		String s = "";
		int i=1;
		while (sc.hasNextLine()) {
			
				String regex = "[^a-zA-Z ]";
				String string = sc.nextLine();
				String subst = "";

				Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

				Matcher matcher = pattern.matcher(string);

				String result = matcher.replaceAll(subst).trim();
				

				s += i + ", " + result + ", ";

				string = sc.nextLine();
				subst = "";

				pattern = Pattern.compile(regex, Pattern.MULTILINE);
				matcher = pattern.matcher(string);

				result = matcher.replaceAll(subst).trim();

				s += result + "\n";
				i++;
			}
			System.out.println(s);
			sc.close();
			newList.write(s);
			newList.close();
			
			
		}

		

}
