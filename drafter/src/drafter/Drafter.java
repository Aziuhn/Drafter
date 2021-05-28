package drafter;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class Drafter {

		private ArrayList<Card> setList = new ArrayList<Card>();
		private String setAcronym;
		private FileInputStream setListFile;
		private String outputFileName;
		private Scanner sc;
		private final int raresPerPack = 1;
		private final int uncommonsPerPack = 3;
		private final int commonsPerPack = 11;
		private ArrayList<Card> mythics;
		private ArrayList<Card> rares;
		private ArrayList<Card> uncommons;
		private ArrayList<Card> commons;

		
		public Drafter(String setListFileName, String setAcronym, String outputFileName) throws MalformedURLException, IOException {
			setListFile = new FileInputStream(setListFileName);
			this.outputFileName = outputFileName;
			this.setAcronym = setAcronym;
			sc = new Scanner(setListFile);
			
			while (sc.hasNextLine()) {
				Card card = new Card();
				String[] cardSplit = sc.nextLine().split(",");
				card.setId(Integer.parseInt(cardSplit[0].trim()));
				card.setName(cardSplit[1].trim());
				card.setRarity(cardSplit[2].trim());
				setList.add(card);
			}
			
			mythics = new ArrayList<Card>();
			for (int i = 0; i < setList.size(); i++) {
				if (correctRarity(setList.get(i), "Mythic")) {
					mythics.add(setList.get(i));
				}
			}

			rares = new ArrayList<Card>();
			for (int i = 0; i < setList.size(); i++) {
				if (correctRarity(setList.get(i), "Rare")) {
					rares.add(setList.get(i));
				}
			}
			
			uncommons = new ArrayList<Card>();
			for (int i = 0; i < setList.size(); i++) {
				if (correctRarity(setList.get(i), "Uncommon")) {
					uncommons.add(setList.get(i));
				}
			}

			commons = new ArrayList<Card>();
			for (int i = 0; i < setList.size(); i++) {
				if (correctRarity(setList.get(i), "Common")) {
					commons.add(setList.get(i));
				}
			}
		}

//	Functions
		
	public void generatePack (int packNumber) throws IOException {
		ArrayList<Card> draftPack = new ArrayList<Card>();
		
		if(Math.random()<0.125) {
			addCardToPack (raresPerPack, mythics, draftPack);
		} else {
			addCardToPack (raresPerPack, rares, draftPack);
		}
		addCardToPack (uncommonsPerPack, uncommons, draftPack);
		addCardToPack (commonsPerPack, commons, draftPack);
		
		FileWriter htmlFile = new FileWriter(outputFileName.replace(".html", "_"+packNumber+".html"));
		
		for (Card card : draftPack) {
			String cardName = card.getName().replace(" ", "-");
//			Substitute "stx" with a generic variable
			URLConnection connection = new URL("https://scryfall.com/card/"+setAcronym+"/" + card.getId() + "/" + cardName)
					.openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			String content = scanner.next();
			String strToSearch = "name=\"twitter:image\" content=\"";
			int startUrlIndex = content.indexOf(strToSearch) + strToSearch.length();
			int endUrlIndex = content.indexOf("\"", startUrlIndex);
			String url = content.substring(startUrlIndex, endUrlIndex);
//			downloadAndSaveImage(url, "./src/drafter/Draft Pack/"+card.getName()+".jpg", "jpg");
//			System.out.println(url);
			htmlFile.write("<img style='height:336;width:240;border:1px solid white' src='"+url+"'>");
			System.out.println("Card generated");
			// scanner.close();
		}
//		sc.close();
		htmlFile.close();
	}
	
	private void addCardToPack (int rarityPerPack, ArrayList<Card> rarityList, ArrayList<Card> draftPack) {
		for (int i = 0; i < rarityPerPack; i++) {
			boolean present = false;
			Card card = new Card();
			do {
				card = generateCard(rarityList);
				present = isPresent(draftPack, card);
			} while (present);
			draftPack.add(card);
		}
	}
	
	private Card generateCard(ArrayList<Card> setList) {
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

	private boolean isPresent(ArrayList<Card> draftPack, Card card) {
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

	private boolean correctRarity(Card card, String rarity) {
		boolean hasRarity = false;
		hasRarity = card.getRarity().equalsIgnoreCase(rarity);
		return hasRarity;
	}

//	private void downloadAndSaveImage(String imageUrl, String folder, String type) {
//		BufferedImage image = null;
//		try {
//			URL url = new URL(imageUrl);
//			image = ImageIO.read(url);
//			ImageIO.write(image, type, new File(folder));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
