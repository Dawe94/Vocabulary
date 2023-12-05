package com.vocabulary;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		// Inicializálás, rövid útmutató kiírása
		System.out.println("The program will write English words, "+
		"\n"+"please write their Hungarian equivalent after.");
		String path = System.getProperty("user.dir")+ "/Vocabulary/VocabularyCollection.xlsx";
		Scanner scanner = new Scanner(System.in);
		Reader reader = new Reader(path, 3);
		System.out.println(reader.getSumOfHits());
		String input = null;
		
		// - Első szavak lekérése a Táblázatból
		List<Word> list = reader.lazyRead();
		
		// - Input mező alapján lezárható ciklus indítása pl ("Exit")
		outer: while (true) {
		// - Inputmező beolvas minden bekérés után (belső ciklus)
			for (Word word : list) {
				System.out.print(word.getWord() + "--> ");
				input = scanner.nextLine();
				
				// - Input mezőn kapott válasz ellenőrzése
				// 		- Kilépési feltétel
				if (input.equalsIgnoreCase("exit")) break outer;
				
				//		- Találat esetén az aktuális szó találatának növelése
				if (word.isThereMatch(input.trim())) {
					word.incrementHit();
					System.out.println("Correct answer!");
				} else {
				//		- Ha nem talált a megoldások kiírása
					System.out.println("Is not a correct answer!");
					System.out.println("The correct answer is next: "+word.getMeaningsAsString());
				}
			}
			// - A ciklus végén mentés és újabb beolvasás
			list = reader.lazyReadAndWrite(list);
		}
		
		// Ha a kilépű feltétel teljesült az addigiak mentése
		reader.lazyWrite(list);	
		
		System.out.println("Finish!");
	}
	
	public static void calculateTime(long start) {
		long end = System.nanoTime();
		long elapsedTime = end - start;
		int sec = (int)(elapsedTime / 1_000_000_000);
		int mills = (int)(elapsedTime % 1_000_000_000) / 1_000_000;
		int nanos = (int)(elapsedTime % 1_000_000);
		System.out.println("Elapsed time: " + sec + "sec, " + mills + "mills, " + nanos + "nanos.");
	}

}
