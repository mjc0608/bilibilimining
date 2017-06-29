import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashMap;

public class BilibiliProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100).setTimeOut(3000);

    private static boolean firstTime;
    private static HashMap<Integer, String> seasonIdToName;

    public static boolean isMetaPage(String url) {
        return url.matches("(http://bangumi\\.bilibili\\.com/jsonp/seasoninfo/\\w+\\.ver)");
    }

    public static boolean isVideoPage(String url) {
        return url.matches("(http://www\\.bilibili\\.com/video/\\w+)");
    }

    public static boolean isVideoMetaPage(String url) {
        return url.matches("(http://bangumi\\.bilibili\\.com/web_api/episode/\\w+\\.json)");
    }

    public static boolean isCommentPage(String url) {
        return url.matches("(http://comment\\.bilibili\\.com/\\w+\\.xml)");
    }

    public void process(Page page) {
        if (firstTime) {
            for (Integer i = 0; i < 2000; i++) {
                page.addTargetRequest("http://bangumi.bilibili.com/jsonp/seasoninfo/" + i.toString() + ".ver");
            }
            firstTime = false;
            return;
        }

        String url = page.getUrl().toString();
        if (isMetaPage(url)) {
            System.out.println("MetaPage: " + url);
            String rawText = page.getRawText();
            if (rawText.indexOf("seasonListCallback") != 0) {
                System.out.println("abandon url: " + url);
                return;
            }
            String jsonText = rawText.substring("seasonListCallback(".length(), rawText.length() - 2);
//            System.out.println(jsonText);
            JSONObject json = JSONObject.fromObject(jsonText);
//            System.out.println("json:" + json.toString());

            JSONArray episodes = JSONArray.fromObject(
                    JSONObject.fromObject(
                            json.get("result")
                    ).get("episodes")
            );
//            System.out.println("episodes: " + episodes.toString());

            seasonIdToName.put(
                    Integer.parseInt(url.substring("http://bangumi.bilibili.com/jsonp/seasoninfo/".length(),url.indexOf(".ver"))),
                    JSONObject.fromObject(
                        json.get("result")
                    ).get("bangumi_title").toString()
            );

            for (int i = 0; i < episodes.size(); i++) {
                JSONObject avInfo = JSONObject.fromObject(episodes.get(i));
                String episode_id = avInfo.get("episode_id").toString();
                page.addTargetRequest("http://bangumi.bilibili.com/web_api/episode/" + episode_id +".json");
            }
            return;
        }
        else if (isVideoMetaPage(url)) {
//            page.putField("name", page.getHtml().xpath("//div[@class='v-title']/h1/text()").toString());
//
//            String cmtJs = page.getHtml().xpath("//div[@class='scontent']/script").toString();
//            int head = cmtJs.indexOf("cid=") + 4;
//            int tail = cmtJs.indexOf("&aid");
//            String cmtUrl = "http://comment.bilibili.com/" + cmtJs.substring(head, tail) + ".xml";
//            System.out.println("cmtUrl: " + cmtUrl);
//            page.putField("cmtUrl", cmtUrl);

            System.out.println("VideoMetaPage: " + url);
            JSONObject currentEpisode = JSONObject.fromObject(
                    JSONObject.fromObject(
                            JSONObject.fromObject(page.getRawText()).get("result")
                    ).get("currentEpisode")
            );

//            System.out.println("VideoMetaPage: " + currentEpisode.toString());

            page.putField("seasonId", currentEpisode.get("seasonId").toString());
            page.putField("index", currentEpisode.get("index").toString());
//            page.putField("longTitle", currentEpisode.get("longTitle").toString());
            page.putField("cmtUrl", "http://comment.bilibili.com/" + currentEpisode.get("danmaku").toString() + ".xml");
            page.putField("name", seasonIdToName.get(Integer.parseInt(currentEpisode.get("seasonId").toString())));
            return;
        }

        System.out.println("no matching url: " + url);

//        System.out.println(page.getHtml().links().regex("(http://www\\.bilibili\\.com/\\w+)").all().toString());
//        if (isCommentPage(url)) {
//            page.putField("xml", page.getRawText());
//        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        firstTime = true;
        seasonIdToName = new HashMap<Integer, String>();
        Spider.create(new BilibiliProcessor())
                .addPipeline(new BilibiliPipeline("output/"))
                .addUrl("http://www.bilibili.com")
                .thread(4)
                .run();
    }
}

