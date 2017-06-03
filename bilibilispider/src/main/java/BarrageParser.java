import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BarrageParser {
    public static List<BarrageObject> parseBarrage(String filename) throws IOException, SAXException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        ParserHandler handler = new ParserHandler();

        parser.parse(new File(filename), handler);

        return handler.getBarrageList();
    }
}

class ParserHandler extends DefaultHandler {
    private BarrageObject barrage = null;

    private List<BarrageObject> barrageList = null;

    private boolean bD = false;

    public List<BarrageObject> getBarrageList() {
        return barrageList;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (qName.equals("d")) {
            String p = attributes.getValue("p");
            String[] arr = p.split(",");
            barrage = new BarrageObject(arr);
            bD = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (bD) {
            barrage.setContent(new String(ch, start, length));
            bD = false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equals("d")) {
            if (barrageList == null) {
                barrageList = new ArrayList();
            }
            barrageList.add(barrage);
        }
    }
}