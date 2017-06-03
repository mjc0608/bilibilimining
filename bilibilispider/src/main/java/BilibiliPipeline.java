import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

public class BilibiliPipeline extends FilePipeline{

    public BilibiliPipeline(String path) {
        this.setPath(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task){
        String path = this.path + PATH_SEPERATOR + task.getUUID() + PATH_SEPERATOR;
        JSONObject json = JSONObject.fromObject("{}");

        if (!resultItems.getRequest().getUrl()
                .matches("(http://bangumi\\.bilibili\\.com/web_api/episode/\\w+\\.json)")) {
            System.out.println("not saving " + resultItems.getRequest().getUrl());
            return;
        }

        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + ".html")), "UTF-8"));

            Iterator vars = resultItems.getAll().entrySet().iterator();
            while(vars.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)vars.next();
                json.accumulate(entry.getKey(), entry.getValue());
            }

            printWriter.println(json.toString());
            printWriter.close();

        } catch (IOException e) {
            System.out.println("write file error");
        }
    }
}
