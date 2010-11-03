package org.turnerha.mint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class Transaction implements Comparable<Transaction> {
	public String mDate;
	public String mDescription;
	public String mOriginalDesc;
	public String mAmount;
	public String mTransactionType;
	public String mCategory;
	public String mAccountName;
	public String mLabels;
	public String mNotes;
	public Date mParsedDate;

	public static SimpleDateFormat formatter = new SimpleDateFormat(
			"MM/dd/yyyy");

	public Transaction(String date, String desc, String origDesc,
			String amount, String transactionType, String category,
			String accountName, String labels, String notes) {

		mDate = date;
		try {
			mParsedDate = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			try {
				mParsedDate = formatter.parse("01/01/2000");
			} catch (ParseException e1) {
				e.printStackTrace();
			}
		}
		mDescription = desc;
		mOriginalDesc = origDesc;
		mAmount = amount;
		mTransactionType = transactionType;
		mCategory = category;
		mAccountName = accountName;
		mLabels = labels;
		mNotes = notes;
	}

	@Override
	public int compareTo(Transaction o) {
		int dateComparison = mParsedDate.compareTo(o.mParsedDate);

		// If the dates are equal, compare amount
		if (dateComparison == 0) {
			int amountComparison = o.mAmount.compareTo(mAmount);

			// If the amounts are equal, compare descriptions
			if (amountComparison == 0) {
				int dist = StringUtils.getLevenshteinDistance(o.mOriginalDesc,
						mOriginalDesc);

				// The strings are very similar, this is the same transaction
				if (dist < 3)
					return 0;

				// Ignore To / From transaction pairs
				if (false == (mOriginalDesc.startsWith("TO") || mOriginalDesc
						.startsWith("FROM"))) {

					System.out.println("The following two transactions may be "
							+ "duplicates, but were not automatically"
							+ " detectable, so they are being considered"
							+ "separate transactions:");
					System.out.println(o.toString());
					System.out.println(toString());
				}
			}

			return amountComparison;
		}

		return dateComparison;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\"");
		sb.append(mDate).append("\",\"");
		sb.append(mDescription).append("\",\"");
		sb.append(mOriginalDesc).append("\",\"");
		sb.append(mAmount).append("\",\"");
		sb.append(mTransactionType).append("\",\"");
		sb.append(mCategory).append("\",\"");
		sb.append(mAccountName).append("\",\"");
		sb.append(mLabels).append("\",\"");
		sb.append(mNotes).append("\"");

		return sb.toString();
	}
	
	public static String getCSVHeader() {
		StringBuilder sb = new StringBuilder("\"");
		sb.append("Date").append("\",\"");
		sb.append("Description").append("\",\"");
		sb.append("Original Description").append("\",\"");
		sb.append("Amount").append("\",\"");
		sb.append("Transaction Type").append("\",\"");
		sb.append("Category").append("\",\"");
		sb.append("Account Name").append("\",\"");
		sb.append("Labels").append("\",\"");
		sb.append("Notes").append("\"");

		return sb.toString();

	}
}
