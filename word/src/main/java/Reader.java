import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liujianxin
 * @date 2018-12-21 10:14
 */
public class Reader {


    public static void main(String[] args) throws IOException {
        Map<File,XWPFDocument> docs = new HashMap<>();
        getDocs(docs, new File("E:\\demo.docx"));

        docs.forEach((file,doc) ->{
            List<XWPFParagraph> paras = doc.getParagraphs();
            paras.forEach((k) -> {
                if ("1".equals(k.getStyleID())) {
                    System.out.println(k.getText());
                    file.renameTo(new File("E:\\"+k.getText()+".docx"));
                }
            });
        });
    }



    private static void getDocs(Map<File,XWPFDocument> docs, File file) throws IOException {
        if (file == null) {
            return;
        }
        if (file.isFile()) {
            if (file.getName().endsWith("docx")) {
                if (docs == null) {
                    docs = new HashMap<>();
                }
                docs.put(file,new XWPFDocument(new FileInputStream(file)));
            }
        } else if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                getDocs(docs, f);
            }
        }
    }
}
