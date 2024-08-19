import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class util {
    public static ArrayList<Integer> intList = new ArrayList<>();
    public static ArrayList<Double> doubleList = new ArrayList<>();
    public static ArrayList<String> stringList = new ArrayList<>();

    public static void stats(ArrayList<Integer> intList, ArrayList<Double> doubleList, ArrayList<String> stringList,
            boolean param) {
        if (param == false) {
            System.out.println("Short statistic for each data type\n");
            System.out.println("Integers count: " + intList.size() + "\n");
            System.out.println("Doubles count: " + doubleList.size() + "\n");
            System.out.println("Strings count: " + stringList.size() + "\n");
        } else {
            System.out.println("Full statistic for each data type\n");
            try {
                System.out.println("Integers count: " + intList.size() + "\n");
                System.out.println("Integers max: " + Collections.max(intList) + "\n");
                System.out.println("Integers min: " + Collections.min(intList) + "\n");

                double sum1 = 0;

                for (Integer i : intList) {
                    sum1 += i;
                }
                double intAvg = sum1 / intList.size();
                System.out.println("Integers sum: " + sum1 + "\n");
                System.out.println("Integers avg: " + intAvg + "\n");

            } catch (NoSuchElementException e) {
                System.out.println("No integer elements found");
            }
            try {

                System.out.println("Doubles count: " + doubleList.size() + "\n");
                System.out.println("Doubles max: " + Collections.max(doubleList) + "\n");
                System.out.println("Doubles min: " + Collections.min(doubleList) + "\n");
                double sum2 = 0;
                for (Double i : doubleList) {
                    sum2 += i;
                }
                double doubleAvg = sum2 / doubleList.size();
                System.out.println("Doubles sum: " + sum2 + "\n");
                System.out.println("Doubles avg: " + doubleAvg + "\n");
            } catch (NoSuchElementException e) {
                System.out.println("No double elements found");
            }
            try {

                System.out.println("String count: " + stringList.size() + "\n");
                String shortest = stringList
                        .stream()
                        .min(Comparator.comparing(String::length))
                        .get();
                String longest = stringList
                        .stream()
                        .max(Comparator.comparing(String::length))
                        .get();
                System.out.println("Longest string: " + longest + " " + longest.length() + " symbols" + "\n");
                System.out.println("Shortest string: " + shortest + " " + shortest.length() + " symbols " + "\n");

            } catch (NoSuchElementException e) {
                System.out.println("No string elements found");
            }

        }
    }

    private static void error(String message) {
        if (message != null) {
            System.err.println(message);
        }
        System.err.println("usage: myapp [-f]/[-s] [-o][</some/path/arg>] [-p][<result_arg>] [<inputN.txt>...][-a]");
        System.exit(1);
    }

    public static void help() {
        System.out.println("-f \n provides full statistics \n");
        System.out.println("-s \n provides short statistics\n");
        System.out.println("-o<arg> \n provides path for output files in [</some/path/arg>]\n");
        System.out.println("-p[<result_arg>] \n provides names for the output files \n");
        System.out.println("-a\n appends if file with the same name exists \n");
        System.out.println("<input1.txt>...<inputN.txt>\n provides input file/files in .txt format\n");
        System.exit(0);
    }

    public static void main(String[] args) {
        boolean param = false;
        boolean param1 = false;
        int index;
        ArrayList<String> inputs = new ArrayList<>();
        String resultName = "";
        String pathString = "";

        loop: for (index = 0; index < args.length; index++) {
            String opt = args[index];
            switch (opt) {
                case "-f":
                    param = true;
                    break;
                case "-h":
                    help();
                    break;
                case "-a":
                    param1 = true;
                    break;
                case "-s":
                    param = false;
                    break;
                case "-o":
                    if (!args[index + 1].isEmpty()) {
                        pathString = args[index + 1];
                        index += 1;
                        break;
                    }
                    error("No path provided");
                case "-p":
                    if (!args[index + 1].isEmpty()) {
                        resultName = args[index + 1];
                        index += 1;
                        break;
                    }
                    error("No output name provided");
                default:
                    if (!opt.isEmpty() && opt.charAt(0) == '-') {
                        error("Unknown option: '" + opt + "'");
                        break loop;
                    }
                    if (opt.isEmpty())
                        break loop;
                    if (!opt.isEmpty() && opt.charAt(0) != '-' && opt.charAt(0) != '/') {
                        inputs.add(opt);
                    }

            }
        }
        if (index > args.length) {
            error("Missing argument(s)");
        }

        String inputFile = "/input.txt";

        String intFile = pathString + '/' + resultName + "integers.txt";
        String doubleFile = pathString + '/' + resultName + "double.txt";
        String stringFile = pathString + '/' + resultName + "strings.txt";

        for (int j = 0; j < inputs.size(); j++) {
            if (inputs.size() != 0) {
                inputFile = inputs.get(j);
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                BufferedWriter intWriter = new BufferedWriter(new FileWriter(intFile, param1));
                BufferedWriter stringWriter = new BufferedWriter(new FileWriter(stringFile, param1));
                BufferedWriter doubleWriter = new BufferedWriter(new FileWriter(doubleFile, param1));

                String line;

                while ((line = reader.readLine()) != null) {
                    try {
                        // Попробуем распознать целое число
                        int intValue = Integer.parseInt(line);
                        intWriter.write(intValue + "\n");
                        intList.add(intValue);

                    } catch (NumberFormatException e1) {
                        try {
                            // Попробуем распознать число с плавающей точкой
                            double doubleValue = Double.parseDouble(line);
                            doubleWriter.write(doubleValue + "\n");
                            doubleList.add(doubleValue);

                        } catch (NumberFormatException e2) {
                            // Если не целое число и не число с плавающей точкой, то это строка
                            if (!line.isEmpty()) {
                                stringWriter.write(line + "\n");
                                stringList.add(line);
                            }
                        }
                    }
                }

                intWriter.close();
                doubleWriter.close();
                stringWriter.close();

            } catch (IOException e) {
                System.out.println("No such file");
                e.printStackTrace();
            }
        }
        stats(intList, doubleList, stringList, param);

    }
}
