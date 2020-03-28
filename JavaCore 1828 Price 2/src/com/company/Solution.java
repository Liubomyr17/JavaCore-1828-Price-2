package com.company;

/*

1828 Prices 2
CrUD for table inside file
Read console file name for CrUD operations
The program starts with one of the following sets of parameters:
-u id productName price quantity
-d id
Parameter Values:
where id is 8 characters
productName - product name, 30 chars (60 bytes)
price - price, 8 characters
quantity - quantity, 4 characters
-u - updates product data with the given id
-d - makes a physical removal of the product with the given id (all data that relates to the transferred id)
The data is stored in the file in the following sequence (without separating spaces):
id productName price quantity
Data is padded with spaces to their length
Example:
19847 Blue beach shorts 159.00 12
198479 Black beach shorts with a pattern173.00 17
19847983 Jacket for snowboarders, size 10173.991234

Requirements:
1. The program should read the file name for CrUD operations from the console.
2. When starting the program without parameters, the list of products should remain unchanged.
3. When starting the program with the parameters "-u id productName price quantity", the product information in the file should be updated.
4. When starting the program with the parameters "-d id", the product line with the given id should be deleted from the file.
5. Streams created for files must be closed.

 */

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//Принято
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = reader.readLine();
        reader.close();

        ArrayList<String> products = new ArrayList<>();
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        while (fileReader.ready())
            products.add(fileReader.readLine());
        fileReader.close();

        switch (args[0]) {
            case "-u": {
                if (args.length < 5)
                    return;

                float price;
                int qty;
                int idFromCommand = 0;

                //Если что-то передали не то
                try {
                    price = Float.parseFloat(args[args.length - 2]);
                    qty = Integer.parseInt(args[args.length - 1]);
                    idFromCommand = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    return;
                }

                //Если у нас аргументов больше 4, из-за пробелов в строке productName
                String productName;
                if (args.length > 5) {
                    StringBuffer buf = new StringBuffer();
                    for (int i = 2; i < args.length - 2; i++)
                        buf.append(args[i]).append(" ");
                    productName = buf.substring(0, buf.length() - 1);
                } else
                    productName = args[2];

                //Get ID from line and update
                Pattern p = Pattern.compile("([0-9]{1,8})");
                for (int i = 0; i < products.size(); i++) {
                    String s = products.get(i);
                    Matcher m = p.matcher(s);
                    if (m.lookingAt()) {
                        try {
                            int id = Integer.parseInt(s.substring(m.start(), m.end()));
                            if (id == idFromCommand) {
                                products.set(i, String.format(Locale.ROOT, "%-8d%-30s%-8.2f%-4d", id, productName, price, qty));
                                break;
                            }
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                }
                break;
            }

            case "-d": {
                Iterator<String> iter = products.iterator();
                while (iter.hasNext()) {
                    String product = iter.next();
                    if (product.startsWith(args[1]))
                        iter.remove();
                }
                break;
            }
        }

        PrintWriter fileWriter = new PrintWriter(fileName);
        for (String s : products)
            fileWriter.println(s);
        fileWriter.close();
    }
}


