package team10.moneyTube.service.crawl;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.ArrayList;

@Data
@AllArgsConstructor
class Reputation {
    private String word;
    private Integer times;
}

public class crawlSometrend {

    private static Integer stringToInt(String in){
        Integer result = 0;
        if(in.contains("건")){
            result = Integer.parseInt(in.replace("건", ""));

        }
        return result;
    }



    public static void main(String[] args) throws IOException {

        String URL = "https://some.co.kr/analysis/social/reputation";
        String filePath = "C:\\Tools\\chromedriver.exe";
        String find = "쯔양";

        try {
            System.setProperty("webdriver.chrome.driver", filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ChromeOptions options = new ChromeOptions();
        //options.addArguments("headless");
        WebDriver driver = new ChromeDriver(options);
        driver.get(URL);


        driver.findElement(By.xpath("/html/body/div[13]/div[5]/div/p/a")).click();
        driver.findElement(By.xpath("//button[@type='button']")).click();
        try {Thread.sleep(1000);} catch (InterruptedException e) {}
        driver.findElement(By.xpath("/html/body/div[4]/div/div[1]/div[2]/div[1]/form/fieldset/div/div[1]/div/input")).sendKeys(find);
        driver.findElement(By.xpath("/html/body/div[4]/div/div[1]/div[2]/div[2]/div/div[2]/button")).click();
        try {Thread.sleep(6000);} catch (InterruptedException e) {}
        Document html = Jsoup.connect(URL).get();


        Elements tableList = html.select("#totalRankList > tr");
        ArrayList<Reputation> positive = new ArrayList<>();
        ArrayList<Reputation> negative = new ArrayList<>();
        ArrayList<Reputation> neutral = new ArrayList<>();

        for(Element table : tableList) {
            if(!table.select("td.reputation.positive").isEmpty()) {
                Reputation r = new Reputation(table.select("td.keyword").text(),
                        Integer.parseInt(table.select("td.count").text()));
                positive.add(r);
            }
            else if(!table.select("td.reputation.negative").isEmpty()) {
                Reputation r = new Reputation(table.select("td.keyword").text(),
                        Integer.parseInt(table.select("td.count").text()));
                negative.add(r);
            }
            else if(!table.select("td.reputation.neutrality").isEmpty()) {
                Reputation r = new Reputation(table.select("td.keyword").text(),
                        Integer.parseInt(table.select("td.count").text()));
                neutral.add(r);
            }
        }

        Elements p = html.select("span.legend-value");
        Integer positiveNum = stringToInt(p.get(0).text());
        Integer negativeNum = stringToInt(p.get(1).text());
        Integer neutralNum = stringToInt(p.get(2).text());





    }
}
