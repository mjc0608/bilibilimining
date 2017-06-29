import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;

import java.io.*;
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
            return;
        }

        try {
//            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.getFile(path + "1.html")), "UTF-8"));
            PrintWriter printWriter = new PrintWriter(new FileWriter(new File(path + resultItems.getAll().get("seasonId").toString()), true));

            Iterator vars = resultItems.getAll().entrySet().iterator();
            while(vars.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)vars.next();
//                json.accumulate(entry.getKey(), entry.getValue());
                printWriter.print(entry.getValue() + " ");
            }

            printWriter.println("");
            printWriter.close();

        } catch (IOException e) {
            System.out.println("write file error");
        }
    }
}
