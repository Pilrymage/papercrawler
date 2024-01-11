package org.example;

import org.example.model.Paper;
import org.example.model.PaperAction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.time.Duration;

import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Crawler {
    static PaperAction paperAction = new PaperAction();
    Integer maxRecords;
    public void crawlData(Integer maxRecords, String keyword, String selectSite) throws InterruptedException {
        String url = getString(keyword, selectSite);
        this.maxRecords= maxRecords;

        try {
            // 发送HTTP请求并获取网页内容
            switch (selectSite){
                case "论文网":
                    crawlLunWenWang(url);
                    break;
                case "知网":
                    crawlZhiWang(url);
                    break;
                case "百度学术":
                    crawlBaiDuXueShu(url);
                    break;
                case "NSTL":
                    crawlNSTL(url);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + selectSite);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getString(String keyword, String selectSite) {
        Map<String, String> searchParams = new HashMap<>();
        searchParams.put("keyword", keyword);
        searchParams.put("BASE64_keyword", Base64.getEncoder().encodeToString(keyword.getBytes(StandardCharsets.UTF_8)));
        StringSubstitutor sub = new StringSubstitutor(searchParams,"${","}");
        String url = switch (selectSite) {
            case "论文网" ->
                    sub.replace("http://hsqk.bmhgc.com/periodical?search=${keyword}&button2=%E6%89%BE%E4%B8%80%E4%B8%8B"); // 替换为目标论文网站的URL
            case "百度学术" ->
                sub.replace("https://xueshu.baidu.com/s?wd=${keyword}&rsv_bp=0&tn=" +
                        "SE_baiduxueshu_c1gjeupa&rsv_spt=3&ie=utf-8&f=8&rsv_sug2=0&sc_f_para=sc_tasktype%3D%7BfirstSimpleSearch%7D");
            case "知网" ->
                    sub.replace("https://kns.cnki.net/kns/search?dbcode=SCDB&kw=${keyword}" +
                            "&korder=SU&crossdbcodes=CJFQ,CDFD,CMFD,CPFD,IPFD,CCND,CISD,SNAD,BDZK,CCJD,CJRF,CJFN");
            case "NSTL" ->
                sub.replace("https://www.nstl.gov.cn/search.html?t=JournalPaper,ProceedingsPaper,DegreePaper&q=${BASE64_keyword}");

            default -> throw new IllegalStateException("Unexpected value: " + selectSite);
        };
        return url;
    }

    private void crawlLunWenWang(String url) throws Exception {
        Document document = Jsoup.connect(url).get();
        Elements rows = document.select("tbody tr td.s12");
        String pattern = "<strong>刊名:<a href=\"[^\"]+\">([^<]+)</a></strong>.*?级别:([^<]+).*?主管:([^<]+).*?主办:([^<]+).*?国内统一刊号:([^<]+).*?国际标准刊号:([^<]+)";
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher m = p.matcher(rows.toString());
        List<Paper> papers = new ArrayList<>();;
        while (m.find()) {
            Paper paper = new Paper();
            paper.setTitle(m.group(1).trim());
            paper.setAuthor(m.group(4).trim());
            paperAction.add(paper);
        }

    }

    private void crawlBaiDuXueShu(String url) throws Exception {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        WebDriver driver = new FirefoxDriver(options);
        driver.get(url);
        Thread.sleep(5000);  // 等待5秒加载
        List<WebElement>  result = driver.findElements(By.cssSelector("#bdxs_result_lists>div.sc_default_result>div.sc_content"));

        int paperCount = 0;
        do {
            for (WebElement webElement : result) {
                Paper paper = new Paper();
                paper.setTitle(webElement.findElement(By.cssSelector("h3.t>a")).getText());
                paper.setAuthor(webElement.findElement(By.cssSelector("div.sc_info>span>a")).getText());
                paper.setSource(webElement.findElements(By.cssSelector("span.v_item_span")).stream().map(WebElement::getText).reduce("", (a, b) -> a + b + ';'));
                paper.setTime(webElement.findElement(By.cssSelector("span.sc_time")).getText());
                paper.setEmail("");
                paperAction.add(paper);
                paperCount += 1;
                if (paperCount >= maxRecords) {
                    break;
                }
            }

            driver.findElement(By.cssSelector("p#page>a.n")).click();
            Thread.sleep(2000);  // 等待2秒加载
            result = driver.findElements(By.cssSelector("#bdxs_result_lists>div.sc_default_result"));
        }while (paperCount < maxRecords);

    }

    private void crawlZhiWang(String url) throws Exception {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        WebDriver driver = new FirefoxDriver(options);

        driver.get(url);
        Thread.sleep(5000);  // 等待5秒加载
        List<WebElement>  result = driver.findElements(By.cssSelector("table.result-table-list>tbody>tr"));
        int paperCount = 0;
        do {
            for (WebElement webElement : result) {
                Paper paper = new Paper();
                paper.setTitle(webElement.findElement(By.cssSelector("td:nth-child(2)>a")).getText());
                paper.setAuthor(webElement.findElement(By.cssSelector("td:nth-child(3)")).getText());
                paper.setSource(webElement.findElement(By.cssSelector("td:nth-child(4)")).getText());
                paper.setTime(webElement.findElement(By.cssSelector("td:nth-child(5)")).getText());
                paper.setEmail("");
                paperAction.add(paper);paperCount += 1;
                if (paperCount >= maxRecords) {
                    break;
                }
            }

            driver.findElement(By.cssSelector("div.pages>a")).click();
            Thread.sleep(2000);  // 等待2秒加载
            result = driver.findElements(By.cssSelector("table.result-table-list>tbody>tr"));

        }while(paperCount < maxRecords);
        driver.quit();
    }

    private void crawlNSTL(String url) throws Exception {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        WebDriver driver = new FirefoxDriver(options);

        driver.get(url);
        Thread.sleep(5000);  // 等待5秒加载
        List<WebElement>  result = driver.findElements(By.cssSelector("div.result-lists-con>div.result-list-con"));
        int paperCount = 0;
        do {
            for (WebElement webElement : result) {
                Paper paper = new Paper();
                paper.setTitle(webElement.findElement(By.cssSelector("p.result-list-tit>a>span:not(.serial_number)")).getText());
                paper.setAuthor(webElement.findElements(By.cssSelector("span[title^=\"作者\"]")).stream().map(WebElement::getText).reduce("", (a, b) -> a + b + ';'));
                paper.setSource(webElement.findElement(By.cssSelector("span[title^=\"出处\"]")).getText());
                paper.setTime(webElement.findElement(By.cssSelector("span[title=\"年份\"]")).getText());
                paper.setEmail("");
                paperAction.add(paper);paperCount += 1;
                if (paperCount >= maxRecords) {
                    break;
                }
            }

            driver.findElement(By.cssSelector("a.layui-laypage-next")).click();
            Thread.sleep(2000);  // 等待2秒加载
            result = driver.findElements(By.cssSelector("div.result-lists-con>div.result-list-con"));

    }while (paperCount < maxRecords);
        driver.quit();
    }

}
