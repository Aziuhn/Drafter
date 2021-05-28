package drafter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;

public class DrafterRunner {

	public static void main(String[] args) throws MalformedURLException, IOException {
		int players;
		final int packsPerPlayer = 3;
		Drafter strixHaven = new Drafter("./src/drafter/StrixhavenList.txt", "stx",
				"./src/drafter/DraftPack/HTMLDraftPack.html");
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Insert number of players: ");
		players = sc.nextInt();
		
		for (int i=0; i<players*packsPerPlayer; i++) {
			strixHaven.generatePack(i+1);
			System.out.println("Pack "+(i+1)+" generated");
		}
		
		System.out.println("Task completed");
		
		sc.close();
	}
}
