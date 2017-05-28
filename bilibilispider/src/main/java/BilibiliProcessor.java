import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class BilibiliProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public static boolean isVideoPage(String url) {
        return url.matches("(http://www\\.bilibili\\.com/video/\\w+)");
    }

    public static boolean isCommentPage(String url) {
        return url.matches("(http://comment\\.bilibili\\.com/\\w+\\.xml)");
    }

    public void process(Page page) {
        String url = page.getUrl().toString();
        page.putField("sub", page.getHtml().xpath("//option/text()").all().toString());
        page.putField("name", page.getHtml().xpath("//div[@class='v-title']/h1/text()").toString());

        System.out.println(page.getHtml().links().regex("(http://www\\.bilibili\\.com/\\w+)").all().toString());

        if (isVideoPage(url)) {
            String cmtJs = page.getHtml().xpath("//div[@class='scontent']/script").toString();
            int head = cmtJs.indexOf("cid=") + 4;
            int tail = cmtJs.indexOf("&aid");
            String cmtUrl = "http://comment.bilibili.com/" + cmtJs.substring(head, tail) + ".xml";
            page.putField("cmtUrl", cmtUrl);
            page.addTargetRequest(cmtUrl);
        }

        if (isCommentPage(url)) {
            page.putField("xml", page.getRawText());
        }

        page.addTargetRequests(page.getHtml().links().regex("(http://www\\.bilibili\\.com/\\w+)").all());
        page.addTargetRequests(page.getHtml().links().regex("(http://www\\.bilibili\\.com/video/\\w+)").all());
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new BilibiliProcessor())
                .addPipeline(new FilePipeline("output/"))
                .addUrl("http://bangumi.bilibili.com/22/")
                .thread(4)
                .run();
    }
}