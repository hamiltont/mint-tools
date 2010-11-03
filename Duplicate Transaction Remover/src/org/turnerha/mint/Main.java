package org.turnerha.mint;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Main {

	public static Set<Transaction> transactionSet = new TreeSet<Transaction>();

	public static void main(String[] args) {

		int totalTransactions = 0, duplicateTransactions = 0;

		try {
			Scanner s = new Scanner(new BufferedReader(new FileReader(
					"/Users/hamiltont/transactions.csv")));
			s.useDelimiter(",");

			// Toss out the first line
			s.nextLine();

			while (s.hasNext()) {
				String date = trimQuotes(s.next(), s);
				String desc = trimQuotes(s.next(), s);
				String origDesc = trimQuotes(s.next(), s);
				String amount = trimQuotes(s.next(), s);
				String type = trimQuotes(s.next(), s);
				String category = trimQuotes(s.next(), s);
				String account = trimQuotes(s.next(), s);
				String labels = trimQuotes(s.next(), s);

				String notes = trimQuotes(s.nextLine().substring(1), s);

				Transaction t = new Transaction(date, desc, origDesc, amount,
						type, category, account, labels, notes);

				if (false == transactionSet.add(t))
					++duplicateTransactions;

				++totalTransactions;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		System.out
				.println("Total transactions processed: " + totalTransactions);
		System.out.println("Duplicates detected: " + duplicateTransactions);

		try {
			FileWriter file = new FileWriter(
					"/Users/hamiltont/transactions_new.csv");

			file.write(Transaction.getCSVHeader());
			file.append('\n');

			for (Transaction t : transactionSet) {
				file.append(t.toString());
				file.append('\n');
			}

			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String trimQuotes(String input, Scanner s) {

		// The scanner can get tripped up by a delimiter in the value e.g. in
		// between double quotes. A description like "went to the market, then
		// bought this" would be broken into 2 around the comma. This while
		// loop does not fix all problems regarding this problem, but it solves
		// 90% of them so i'm ok with that ;)
		while (input.endsWith("\"") == false) {
			input = input + s.next();
		}

		return input.substring(1, input.length() - 1);
	}
}
