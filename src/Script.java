import java.io.*;
import java.util.*;

public class Script {

    private static String capitalise(String s){
        s=s.trim().toLowerCase();
        char[] arr=s.toCharArray();
        arr[0]=Character.toUpperCase(arr[0]);

        return new String(arr);
    }

    public static void main(String[] args){
        try{
            ArrayList<Bird> birds=new ArrayList<>();

            BufferedReader reader=new BufferedReader(new InputStreamReader(BirdBrain.class.getResourceAsStream("/birds.txt")));
            String file= reader.readAllAsString();
            reader.close();

            for (String bird:file.split("\\r?\\n")){
                if (bird.isBlank())continue;
                birds.add(new Bird(bird));

            }

            Scanner sc=new Scanner(System.in);

            StringBuilder sb=new StringBuilder();

            for (int i=0;i<birds.size();i++){
                Bird bird=birds.get(i);
                String family="", order="";

                boolean confirm=false;
                while (!confirm) {
                    while (family.isBlank() || order.isBlank()) {
                        System.out.printf("Enter family for %s: ", bird.species);
                        family = sc.nextLine();
                        System.out.printf("Enter order for %s: ", bird.species);
                        order = sc.nextLine();

                    }
                    System.out.printf("Family: '%s'\nOrder: '%s'\nConfirm? (y/n): ", family, order);
                    String choice=sc.nextLine();
                    if (choice.equalsIgnoreCase("y")){
                        confirm=true;
                    }
                    else{
                        family=order="";
                    }
                }
                bird.family=capitalise(family);
                bird.order=capitalise(order);

                sb.append(String.format("%s,%s,%s\n",bird.species,bird.family,bird.order));

            }
            sc.close();

            FileWriter writer=new FileWriter(System.getProperty("user.home")+"/birds.txt");

            writer.write(sb.toString());
            writer.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
