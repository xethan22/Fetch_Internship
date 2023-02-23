import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime; 
import java.time.ZoneId;
import java.time.format.DateTimeFormatter; 
import java.util.*;
import java.time.Instant;

public class Fetch {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Make sure to use the format: java Fetch.java <number of points to spend> <transactions csv file name>");
            System.exit(1);
        }

        int pointsToSpend = Integer.parseInt(args[0]);
        String csvFile = args[1];
        List<Transaction> transactions = new ArrayList<>();
        Map<String, Integer> payerLog = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine == true) {
                    firstLine = false;
                    continue;
                }

                String[] inputs = line.split(",");
                if (inputs.length != 3) {
                    System.err.println("Invalid transaction csv format");
                    System.exit(1);
                }

                String payer = inputs[0];
                int points = Integer.parseInt(inputs[1]);
                String temp_timestamp = inputs[2];
                temp_timestamp  = temp_timestamp.substring(1, temp_timestamp.length() - 1); // remove "" characters from the beginning and end of String

                Instant tester = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(temp_timestamp));
                ZoneId temp_zoneId = ZoneId.systemDefault(); 
                ZonedDateTime temp_zonedDateTime = tester.atZone(temp_zoneId);
                LocalDateTime timestamp = temp_zonedDateTime.toLocalDateTime(); // Convert type to LocalDateTime
                
                Transaction transaction = new Transaction(payer, points, timestamp);
                transactions.add(transaction);

                if (!payerLog.containsKey(payer)) {
                    payerLog.put(payer, points);
                }
                else {
                    payerLog.put(payer, payerLog.get(payer) + points);
                }
            }
        } catch (IOException e) {
            System.out.println("Couldn't read your csv file!");
            System.exit(1);
        }

        transactions.sort(Comparator.comparing(Transaction::getTimestamp));

        for (Transaction transaction : transactions) {
            if (pointsToSpend == 0) {
                break;
            }

            int availPayerPoints = payerLog.get(transaction.getPayer());
            int minSpending = Math.min(pointsToSpend, availPayerPoints);
            minSpending = Math.min(minSpending, transaction.getPoints());
            int newBalance = availPayerPoints - minSpending;
            payerLog.put(transaction.getPayer(), newBalance);
            pointsToSpend -= minSpending;
        }

        // print out payer balances
        for (Map.Entry<String, Integer> entry : payerLog.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        
    }
}

class Transaction {
    private String payer;
    private int points;
    private LocalDateTime timestamp;

    public Transaction(String payer, int points, LocalDateTime timestamp) {
        this.payer = payer;
        this.points = points;
        this.timestamp = timestamp;
    }

    public String getPayer() {
        return payer;
    }

    public int getPoints() {
        return points;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}