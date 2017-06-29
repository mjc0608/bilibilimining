import org.xml.sax.SAXException;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mark-lee on 6/3/17.
 */
public class BarrageHandler implements PageProcessor {
    private final int NAME_POS = 28;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    private String filename;

    private List<BarrageObject> list;

    public BarrageHandler(String url) {
        filename = url.substring(NAME_POS);
    }

    public void process(Page page) {
        String xml = page.getRawText();
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        pw.print(xml);
        pw.flush();
        pw.close();

        try {
            list = BarrageParser.parseBarrage(filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        for (BarrageObject bo : list) {
            System.out.println(bo.getContent());
        }
    }

    public Site getSite() {
        return site;
    }

    public static List<BarrageObject> getContent(String url) {
        BarrageHandler handler = new BarrageHandler(url);

        Spider.create(handler)
                .addUrl(url)
                .thread(1)
                .run();

        return handler.list;
    }

    public static void main(String[] args) {
        BarrageHandler.getContent("http://comment.bilibili.com/2955655.xml");
    }
}