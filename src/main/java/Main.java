import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class Main {

    private static boolean validateString(String line) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '\"') {
                count++;
            }
            if (ch == ';') {
                if (count == 0 || count == 2) {
                    count = 0;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String filePath = "";
        if (args.length > 0) {
            filePath = args[0];
        }
        Path path = Paths.get(filePath);
        if (!Files.isReadable(path)) {
            System.out.println("Приложение не имеет прав на чтение файла.");
            return;
        }
        List<Map<String, Value>> matrix = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int k = 1;
            while ((line = reader.readLine()) != null) {
                if (validateString(line)) {
                    String[] ar = line.split(";");
                    List<Value> listValue = new ArrayList<>();
                    for (int i = 0; i < ar.length; i++) {
                        if (matrix.size() == i) {
                            matrix.add(new TreeMap<>());
                        }
                        if (!"\"\"".equals(ar[i]) && !ar[i].isEmpty()) {
                            Value value;
                            if (matrix.get(i).containsKey(ar[i])) {
                                value = matrix.get(i).get(ar[i]);
                                value.add(line);
                            } else {
                                String[] newList = new String[]{line};
                                value = new Value(newList, k);
                                matrix.get(i).put(ar[i], value);
                            }
                            listValue.add(value);
                        }
                    }
                    int min = listValue.stream().mapToInt(Value::getGroupNumber).min().orElse(0);//todo throw
                    listValue.forEach(v -> v.setGroupNumber(min));
                    k++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Set<String>> out = matrix.stream()
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .map(Map.Entry::getValue)
                .filter(v -> v.getList().length > 1)
                .collect(Collectors.groupingBy(Value::getGroupNumber, Collectors.toList()))
                .values()
                .parallelStream()
                .map(values -> values.stream().map(Value::getList).flatMap(Arrays::stream).collect(Collectors.toSet()))
                .filter(l -> l.size() > 1)
                .sorted((o1, o2) -> Integer.compare(o2.size(), o1.size()))
                .collect(Collectors.toList());
        System.out.println("Количество групп " + out.size());
        filePath = "out.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Количество групп: " + out.size());
            writer.newLine();
            int kl = 1;
            for (Set<String> set : out) {
                writer.write("Группа " + kl);
                writer.newLine();
                for (String str : set) {
                    writer.write(str);
                    writer.newLine();
                }
                kl++;
            }
            System.out.println("Запись завершена в файл out.txt");
            System.out.println("Время выполнения:" + (System.currentTimeMillis() - start) + " мс");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



