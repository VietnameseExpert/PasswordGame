import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PasswordGame {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner input = new Scanner(System.in);
        String urPassword;

        // [{rule 1: T/F, unlocked: T/F}]
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        HashMap<String, String> word = new HashMap<>();
        word.put("Status", "pending"); // "unlocked"
        word.put("Description", "1. Your password must be at least 5 characters. ");
        word.put("Correct", "false");
        list.add(word);

        word = new HashMap<>(); // create a new one!
        word.put("Status", "locked");
        word.put("Description", "2. Your password must include a number.");
        word.put("Correct", "false");
        list.add(word);

        word = new HashMap<>();
        word.put("Status", "locked");
        word.put("Description", "3. Your password must include an uppercase letter.");
        word.put("Correct", "false");
        list.add(word);

        word = new HashMap<>();
        word.put("Status", "locked");
        word.put("Description", "4. Your password must include a special character.");
        word.put("Correct", "false");
        list.add(word);

        word = new HashMap<>();
        word.put("Status", "locked");
        word.put("Description", "5. The digits in your password must add up to 25.");
        word.put("Correct", "false");
        list.add(word);

        word = new HashMap<>();
        word.put("Status", "locked");
        word.put("Description", "6. Your password must include a month of the year.");
        word.put("Correct", "false");
        list.add(word);

        word = new HashMap<>();
        word.put("Status", "locked");
        word.put("Description", "7. Your password must include a roman numeral.");
        word.put("Correct", "false");
        list.add(word);

        word = new HashMap<>();
        word.put("Status", "locked");
        word.put("Description", "8. Your password must include one of our sponsors (Pepsi, Starbucks, Shell):");
        word.put("Correct", "false");
        list.add(word);

        word = new HashMap<>();
        word.put("Status", "locked");
        word.put("Description", "9. Your password must include one of our sponsors (Pepsi, Starbucks, Shell):");
        word.put("Correct", "false");
        list.add(word);

        word = new HashMap<>();
        word.put("Status", "locked");
        word.put("Description", "10. Your password must include one of our sponsors (Pepsi, Starbucks, Shell):");
        word.put("Correct", "false");
        list.add(word);

        word = new HashMap<>();
        word.put("Status", "locked");
        word.put("Description", "11. Your password must include today's Wordle answer.");
        word.put("Correct", "false");
        list.add(word);

        boolean win = false;
        while (!win) {
            System.out.println("\nEnter password: ");
            urPassword = input.nextLine();

            Boolean[] urGuessList = new Boolean[list.size()];
            urGuessList[0] = rule_01(urPassword);
            urGuessList[1] = rule_02(urPassword);
            urGuessList[2] = rule_03(urPassword);
            urGuessList[3] = rule_04(urPassword);
            urGuessList[4] = rule_05(urPassword);
            urGuessList[5] = rule_06(urPassword);
            urGuessList[6] = rule_07(urPassword);
            urGuessList[7] = rule_08(urPassword);
            urGuessList[8] = true;
            urGuessList[9] = true;
            urGuessList[10] = rule_11(urPassword);


            win = true;
            // if the password does not meet one of the rules
            // Win = false (game is set default to win) --> continue playing
            for (boolean ruleMet : urGuessList) {
                if (!ruleMet) {
                    win = false;
                    break;
                }
            }

            if (win) {
                System.out.println("Valid Password\n");
            }   else {
                System.out.println("Invalid Password\n");
            }

            listBuilder(list, urGuessList);
            display(list);
            System.out.println("-------------------------------------------------");
        }
    }

    public static boolean rule_01(String Password) {
        return Password.replace(" ", "").length() >= 5;
    }
    public static boolean rule_02(String Password) {
        char[] passChars = Password.toCharArray();
        for (char character : passChars) {
            try {
                Integer.parseInt(String.valueOf(character));
                return true;
            } catch (NumberFormatException nfe) {
                skip();
            }
        }
        return false;
    }
    public static boolean rule_03(String Password) {
        char[] passChars = Password.toCharArray();
        for (char character : passChars) {
            if (Character.isUpperCase(character)) {
                return true;
            }
        }
        return false;
    }
    public static boolean rule_04(String Password) {
        return Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE).matcher(Password).find();
    }
    public static boolean rule_05(String Password) {
        int sum = 0;
        char[] passChars = Password.toCharArray();
        for (char character : passChars) {
            try {
                int num = Integer.parseInt(String.valueOf(character));
                sum += num;
            } catch (NumberFormatException nfe) {
                skip();
            }
        }
        return sum == 25;
    }

    public static boolean rule_06(String password) {
        String lowerCased = password.toLowerCase();
        String[] months = {
                "january",
                "february",
                "march",
                "april",
                "may",
                "june",
                "july",
                "august",
                "september",
                "october",
                "november",
                "december"
        };

        for (String month : months) {
            if (lowerCased.contains(month)) {
                return true;
            }
        }

        return false;
        // return wordInPass(lowerCased, months);
    }

    public static boolean rule_07(String password) {
        String[] romanNumeral = {"I", "V", "X", "L", "C", "D", "M"};
        return wordInPass(password, romanNumeral);
    }

    public static boolean rule_08(String password) {
        String lowerCased = password.toLowerCase();

        String[] sponsors = {"pepsi", "starbucks", "shell"};
        for (String sponsor : sponsors) {
            if (lowerCased.contains(sponsor)) {
                return true;
            }
        }
        return false;
    }

    public static boolean rule_11 (String password) throws IOException, InterruptedException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        String API_URL = "https://neal.fun/api/password-game/wordle?date=" + dateFormat.format(date);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(API_URL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) { // successful
            System.out.println(response.body());

            ObjectMapper mapper = new ObjectMapper();
            Answer answer = mapper.readValue(response.body(), new TypeReference<Answer>() {
            });

            return password.contains(answer.getAnswer());
        }
        return false;
    }

    public static boolean wordInPass(String Password, String[] target) {
        for (int i = 0; i < Password.length(); i++) { // char character : Password.toCharArray()

            for (String element : target) {

                if (Password.charAt(i) == element.charAt(0)) {
                    char[] comparedMonth = new char[element.length()];
                    int index = i;
                    int put = 0;
                    while (index < i+element.length()) {
                        comparedMonth[put] = Password.charAt(index);

                        put++;
                        index++;

                        if (index == Password.length()) {
                            break;
                        }
                    }
                    boolean same = true;
                    for (int x = 0; x < element.length(); x++) {
                        if (comparedMonth[x] != element.charAt(x)) {
                            same = false;
                            break;
                        }
                    }
                    if (same) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void listBuilder(ArrayList<HashMap<String, String>> list, Boolean[] rule) {
        HashMap<String, String> editHash;

        int streak = 0;
        while (rule[streak].equals(true)) {
            streak++;
            if (streak == rule.length) {
                break;
            }
        }

        int pendingPos = 0;
        HashMap<String, String> miniHashmap = list.get(pendingPos);
        while (!miniHashmap.get("Status").equals("pending")) {
            pendingPos++;
            miniHashmap = list.get(pendingPos);
        }

        int posToPend = Math.max(streak, pendingPos);

        if (posToPend < list.size()) {
            editHash = new HashMap<>();
            miniHashmap = list.get(posToPend);
            editHash.put("Status", "pending");
            editHash.put("Description", miniHashmap.get("Description"));
            editHash.put("Correct", miniHashmap.get("Correct"));
            list.set(posToPend, editHash);
        }

        int index = 0;
        while (index < posToPend) {
            miniHashmap = list.get(index);
            editHash = new HashMap<>();

            editHash.put("Status", "unlocked");
            editHash.put("Description", miniHashmap.get("Description"));
            editHash.put("Correct", miniHashmap.get("Correct"));

            list.set(index, editHash);
//            System.out.println(list);

            index++;

            if (index == list.size()) {
                break;
            }
        }
        //--------------status generated

        // key: Correct;
        index = 0;
        miniHashmap = list.get(index);
        while (!miniHashmap.get("Status").equals("locked")) {

            miniHashmap = list.get(index);
            if (rule[index]) {
                editHash = new HashMap<>();
                editHash.put("Status", miniHashmap.get("Status"));
                editHash.put("Description", miniHashmap.get("Description"));
                editHash.put("Correct", "true");
                list.set(index, editHash);
            } else {
                editHash = new HashMap<>();
                editHash.put("Status", miniHashmap.get("Status"));
                editHash.put("Description", miniHashmap.get("Description"));
                editHash.put("Correct", "false");
                list.set(index, editHash);
            }

            index++;
            if (index == list.size()) {
                break;
            }
        }
    }

    public static void display(ArrayList<HashMap<String, String>> list) {
        ArrayList<String> displayList = new ArrayList<>();
        int i = 0;
        int index =0;
        HashMap<String, String> miniHashmap = list.get(index);
        while (!miniHashmap.get("Status").equals("locked")) {
//            miniHashmap = (HashMap<String, String>) list.get(index);
            if (miniHashmap.get("Correct").equals("true")) {
                System.out.println(miniHashmap.get("Description") + "✅✅✅");
            }   else {
                displayList.add(i, miniHashmap.get("Description") + "❌❌❌");
                i++;
            }

            index++;
            if (index == list.size()) {
                break;
            }
            miniHashmap = list.get(index);
        }
        for (String description : displayList) {
            System.out.println(description);
        }
    }

    public static void skip () {
    }
}