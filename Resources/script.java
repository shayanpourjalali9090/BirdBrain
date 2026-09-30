import java.io.*;
public static void hang(){while (true);}
public static void main(String[] args){
    File folder=new File("images");
    try{
        FileReader reader=new FileReader("birds.txt");
        String str1=reader.readAllAsString();

        for (String name:str1.split("\\r?\\n")){
            if (name.isBlank())continue;
            boolean found=false;
            for (File f: folder.listFiles()){
                if (f.getName().equalsIgnoreCase(name)){
                    found=true;
                    break;
                }
            }
            if (!found){
                System.out.printf("\"%s\" has NO image\n",name);
            }
            else System.out.printf("\"%s\" does have an image\n",name);

        }
        reader.close();

    } catch (java.lang.Exception e) {
        throw new RuntimeException(e);
    }


}