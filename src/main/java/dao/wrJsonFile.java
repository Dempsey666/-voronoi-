package dao;

import com.alibaba.fastjson.JSON;

import java.io.*;

/**
 * Package: dao
 * Description：
 * Author: Dempsey
 * Date:  2020/3/8 23:04
 * Modified By:
 */
public class wrJsonFile {
    /**
     * 写文件到.Json
     * @param filePath
     * @param sets
     * @throws IOException
     */
    public static void writeFile(String filePath, String sets)
            throws IOException {
        FileWriter fw = new FileWriter(filePath);
        PrintWriter out = new PrintWriter(fw);
        out.write(sets);
        out.println();
        fw.close();
        out.close();
    }

    /**
     * 读Json文件
     * @param path
     * @return
     */
    public static Object ReadFile(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr = laststr + tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return JSON.parse(laststr);
    }

}
