import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class Item {
    String title;
    String link;
    String description;
    String pubDate;

    public Item(String title, String link, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
    }
}

class RssFeed {
    List<Item> items = new ArrayList<Item>();
    String title;

    public void parse(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            title = doc.select("title").first().text();
            Elements itemElems = doc.select("item");
            for (Element itemElem : itemElems) {
                String title = itemElem.select("title").first().text();
                String link = itemElem.select("link").first().text();
                String description = itemElem.select("description").first().text();
                String pubDate = itemElem.select("pubDate").first().text();
                Item item = new Item(title, link, description, pubDate);
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayItems() {
        for (Item item : items) {
            System.out.println(item.title + " - " + item.link);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        RssFeed feed = new RssFeed();
        feed.parse("http://rss.cnn.com/rss/edition.rss");
        feed.displayItems();
    }
}
