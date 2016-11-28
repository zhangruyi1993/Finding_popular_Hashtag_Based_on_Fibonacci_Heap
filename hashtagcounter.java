import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class hashtagcounter {
    public static void main(String[] args) {
        //initial the output file
        File output = new File("output_file.txt");
        try {
            //read the txt file
            BufferedReader in = new BufferedReader(new FileReader(args[0]));
            FibonacciHeap hashTagHeap = new FibonacciHeap();
            HashMap<String, FibonacciNode> hashTagMap = new HashMap<>();
            output.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(output));
            //read and process the input file line by line
            while (in.ready()) {
                String tempString = in.readLine();
                //classify the input into three types, "stop", hashtag and request in response to three condition
                if (tempString.equalsIgnoreCase("stop")) {
                    break;
                } else if (tempString.charAt(0) == '#') {
                    String[] hashTag = tempString.substring(1).split(" ");
                    //add the new line info into hashmap and heap
                    if (hashTagMap.containsKey(hashTag[0])) {
                        hashTagHeap.increase(hashTagMap.get(hashTag[0]),Integer.parseInt(hashTag[1]));
                    } else {
                        FibonacciNode node = new FibonacciNode(hashTag[0],Integer.parseInt(hashTag[1]));
                        hashTagMap.put(hashTag[0], node);
                        hashTagHeap.insert(node);
                    }
                } else {
                    int query = Integer.parseInt(tempString.trim());
                    FibonacciHeap tempHeap = new FibonacciHeap();
                    String result = "";
                    for (int i = 0; i < query; i++) {
                        FibonacciNode tempNode = new FibonacciNode(hashTagHeap.max.key,hashTagHeap.max.value);
                        tempHeap.insert(tempNode);
                        hashTagMap.put(tempNode.key,tempNode);
                        result = result.concat(tempNode.key);
                        if (i < query - 1) {
                            result = result.concat(",");
                        } else {
                            out.write(result.concat("\r\n"));
                            out.flush();
                        }
                        hashTagHeap = hashTagHeap.deleteMax();
                    }
                    hashTagHeap.merge(tempHeap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
