import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class BilibiliProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public void process(Page page) {
//        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
//        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
//        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
//        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));

        page.addTargetRequests(page.getHtml().links().regex("(http://www\\.bilibili\\.com/\\w+)").all());
        page.addTargetRequests(page.getHtml().links().regex("(http://www\\.bilibili\\.com/video/\\w+)").all());

        System.out.println(page.getHtml().links().regex("(http://www\\.bilibili\\.com/\\w+)").all().toString());
        System.out.println();

        page.putField("uri", page.getUrl().toString());
        page.putField("sub", page.getHtml().xpath("//option/text()").all().toString());
        page.putField("name", page.getHtml().xpath("//div[@class='v-title']/h1/text()").toString());
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new BilibiliProcessor())
                .addUrl("http://bangumi.bilibili.com/22/")
                .thread(1)
                .run();
    }
}