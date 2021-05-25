package drafter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class DrafterRunner {

	public static void main(String[] args) throws MalformedURLException, IOException {

		ArrayList<Card> setList = new ArrayList<Card>();
		ArrayList<Card> draftPack = new ArrayList<Card>();
		FileInputStream setListFile = new FileInputStream("./src/drafter/StrixhavenList.txt");
		Scanner sc = new Scanner(setListFile);
		final int raresPerPack = 1;
		final int uncommonsPerPack = 3;
		final int commonsPerPack = 11;

		while (sc.hasNextLine()) {
			Card card = new Card();
			String[] cardSplit = sc.nextLine().split(",");
			card.setId(Integer.parseInt(cardSplit[0].trim()));
			card.setName(cardSplit[1].trim());
			card.setRarity(cardSplit[2].trim());
			setList.add(card);
		}

		ArrayList<Card> rares = new ArrayList<Card>();
		for (int i = 0; i < setList.size(); i++) {
			if (correctRarity(setList.get(i), "Rare") || correctRarity(setList.get(i), "Mythic")) {
				rares.add(setList.get(i));
			}
		}

		ArrayList<Card> uncommons = new ArrayList<Card>();
		for (int i = 0; i < setList.size(); i++) {
			if (correctRarity(setList.get(i), "Uncommon")) {
				uncommons.add(setList.get(i));
			}
		}

		ArrayList<Card> commons = new ArrayList<Card>();
		for (int i = 0; i < setList.size(); i++) {
			if (correctRarity(setList.get(i), "Common")) {
				commons.add(setList.get(i));
			}
		}

		for (int i = 0; i < raresPerPack; i++) {
			boolean present = false;
			Card card = new Card();
			do {
				card = generateCard(rares);
				present = isPresent(draftPack, card);
			} while (present);
			draftPack.add(card);
		}

		for (int i = 0; i < uncommonsPerPack; i++) {
			boolean present = false;
			Card card = new Card();
			do {
				card = generateCard(uncommons);
				present = isPresent(draftPack, card);
			} while (present);
			draftPack.add(card);
		}

		for (int i = 0; i < commonsPerPack; i++) {
			boolean present = false;
			Card card = new Card();
			do {
				card = generateCard(commons);
				present = isPresent(draftPack, card);
			} while (present);
			draftPack.add(card);
		}

		// System.out.println(draftPack);
		/*
		 * for (Card card : draftPack) { System.out.println(card.getName()); }
		 */
		for (Card card : draftPack) {
			String cardName = card.getName().replace(" ", "-");
			URLConnection connection = new URL("https://scryfall.com/card/stx/" + card.getId() + "/" + cardName)
					.openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			String content = scanner.next();
			String strToSearch = "name=\"twitter:image\" content=\"";
			int startUrlIndex = content.indexOf(strToSearch) + strToSearch.length();
			int endUrlIndex = content.indexOf("\"", startUrlIndex);
			String url = content.substring(startUrlIndex, endUrlIndex);
			downloadAndSaveImage(url, "./src/drafter/Draft Pack/"+card.getName()+".jpg", "jpg");
			//System.out.println(url);
			
		}
		
		System.out.println("Finito!");
		// scanner.close();

		sc.close();
	}

	static Card generateCard(ArrayList<Card> setList) {
		Card card = new Card();
		int index = (int) (Math.random() * setList.size());
		int id = setList.get(index).getId();
		card.setId(id);
		String name = setList.get(index).getName();
		card.setName(name.trim());
		String rarity = setList.get(index).getRarity();
		card.setRarity(rarity.trim());
		return card;
	}

	static boolean isPresent(ArrayList<Card> draftPack, Card card) {
		boolean present = false;
		int i = 0;
		while (i < draftPack.size() && present == false) {
			if (card.getName().equalsIgnoreCase(draftPack.get(i).getName())) {
				present = true;
			} else {
				present = false;
			}
			i++;
		}
		return present;
	}

	static boolean correctRarity(Card card, String rarity) {
		boolean hasRarity = false;
		hasRarity = card.getRarity().equalsIgnoreCase(rarity);
		return hasRarity;
	}

	static void downloadAndSaveImage(String imageUrl, String folder, String type) {
		BufferedImage image = null;
		try {
			URL url = new URL(imageUrl);
			image = ImageIO.read(url);
			ImageIO.write(image, type, new File(folder));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
