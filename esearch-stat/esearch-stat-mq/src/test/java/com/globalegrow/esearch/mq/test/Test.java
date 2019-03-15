package java.com.globalegrow.esearch.mq.test;

import com.globalegrow.esearch.mq.util.MapperUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * Created by nieruiqun on 2018/11/24.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        String path = Test.class.getResource("/esearch-stat-mq-2018-11-22.log").getPath();
        File filename = new File(path);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);

        File writeName = new File("./10002_00000_1.log");
        writeName.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(writeName));

        File writeName1 = new File("./10007_00000_1.log");
        writeName1.createNewFile();
        BufferedWriter out1 = new BufferedWriter(new FileWriter(writeName1));

        File writeName2 = new File("./10013_00000_1.log");
        writeName2.createNewFile();
        BufferedWriter out2 = new BufferedWriter(new FileWriter(writeName2));

        File writeName3 = new File("./1111111.log");
        writeName3.createNewFile();
        BufferedWriter out3 = new BufferedWriter(new FileWriter(writeName3));

        String line = "";
        line = br.readLine();
        while (line != null) {
            line = br.readLine();
            if (StringUtils.isNotBlank(line) && line.contains("Receive domain:10002") && line.contains("(RabbitMQConsumer.java:49)")){
                String message = StringUtils.substringBetween(line, "message:[", "].");
                String row = MapperUtil.getTableString(message);
                out.write(row + "\n");
            } else if (StringUtils.isNotBlank(line) && line.contains("Receive domain:10007") && line.contains("(RabbitMQConsumer.java:49)")){
                String message = StringUtils.substringBetween(line, "message:[", "].");
                String row = MapperUtil.getTableString(message);
                out1.write(row + "\n");
            } else if (StringUtils.isNotBlank(line) && line.contains("Receive domain:10013") && line.contains("(RabbitMQConsumer.java:49)")){
                String message = StringUtils.substringBetween(line, "message:[", "].");
                String row = MapperUtil.getTableString(message);
                out2.write(row + "\n");
            } else {
                out3.write(line + "\n");
            }
        }

        out.flush();
        out.close();
        out1.flush();
        out1.close();
        out2.flush();
        out2.close();
        out3.flush();
        out3.close();

    }

}
