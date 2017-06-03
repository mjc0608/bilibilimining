import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mark-lee on 6/3/17.
 */
public class BarrageObject {
    // 相对视频的时间
    private Double relativeTime;
    //        1 - 上端滚动弹幕
    //        2 - 下端滚动弹幕
    //        4 - 底部弹幕
    //        5 - 顶部弹幕
    //        6 - 逆向弹幕
    //        7 - 定位弹幕
    //        8 - 代码高级弹幕
    //        9 - 预留 Zoome弹幕兼容性
    //        17 - 图片定位弹幕
    //        18 - Canvas/SVG定位弹幕（未完成）
    //        19 - 绘图弹幕（未完成）
    //        20 - 高阶定位混合弹幕组（实验性）
    //        21 - 字幕弹幕 默认情况下，弹幕都必须包含：开始时间、字体大小、字体字号、颜色。
    private Integer barrageType;
    // 字号
    private Integer size;
    // 字体颜色
    private Integer color;
    // UNIX时间戳
    private Long timestamp;
    // 弹幕池类型 0普通 1字幕 2特殊
    private Integer poolType;
    // 发送者ID（不是UID，用于拼比）
    private Long senderID;
    // 弹幕在数据库中ID
    private Long barrageID;
    // 弹幕内容
    private String content;

    public BarrageObject(String[] arr) {
        assert arr.length == 8;
        relativeTime = Double.parseDouble(arr[0]);
        barrageType = Integer.parseInt(arr[1]);
        size = Integer.parseInt(arr[2]);
        color = Integer.parseInt(arr[3]);
        timestamp = Long.parseLong(arr[4]);
        poolType = Integer.parseInt(arr[5]);
        senderID = Long.parseLong(arr[6], 16);
        barrageID = Long.parseLong(arr[7]);
    }

    public Double getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(Double relativeTime) {
        this.relativeTime = relativeTime;
    }

    public Integer getBarrageType() {
        return barrageType;
    }

    public void setBarrageType(Integer barrageType) {
        this.barrageType = barrageType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getPoolType() {
        return poolType;
    }

    public void setPoolType(Integer poolType) {
        this.poolType = poolType;
    }

    public Long getSenderID() {
        return senderID;
    }

    public void setSenderID(Long senderID) {
        this.senderID = senderID;
    }

    public Long getBarrageID() {
        return barrageID;
    }

    public void setBarrageID(Long barrageID) {
        this.barrageID = barrageID;
    }

    public static Date timestamp2Date(Long timestamp) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(format.format(timestamp));
    }

    public static Long date2Timestamp(Date date) {
        return date.getTime();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}