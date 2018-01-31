package com.flchy.blog.common.crawler.htmlunit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

public class TianMao {
	public static BrowserVersion bv = BrowserVersion.BEST_SUPPORTED.clone();
	public static BrowserVersion getBrowserVersion() {

		// 设置语言，否则不知道传过来是什么编码
		bv.setUserLanguage("zh_cn");
		bv.setSystemLanguage("zh_cn");
		bv.setBrowserLanguage("zh_cn");

		// 源码里是写死Win32的，不知道到生产环境（linux）会不会变，稳妥起见还是硬设
		bv.setPlatform("Win32");

		return bv;
	}

	public static WebClient newWebClient() {
		WebClient wc = new WebClient(bv);
		wc.getOptions().setUseInsecureSSL(true); // 允许使用不安全的SSL连接。如果不打开，站点证书过期的https将无法访问
		wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器
		wc.getOptions().setCssEnabled(false); // 禁用css支持
		// 禁用一些异常抛出
		wc.getOptions().setThrowExceptionOnScriptError(false);
		wc.getOptions().setThrowExceptionOnFailingStatusCode(false);

		wc.getOptions().setDoNotTrackEnabled(false); // 随请求发送DoNotTrack
		wc.setJavaScriptTimeout(1000); // 设置JS超时，这里是1s
		wc.getOptions().setTimeout(5000); // 设置连接超时时间 ，这里是5s。如果为0，则无限期等待
		wc.setAjaxController(new NicelyResynchronizingAjaxController()); // 设置ajax控制器

		return wc;
	}
	public String getTmallDetail(String url) {
        WebClient wc = newWebClient();

        String detail = "";

        try {
            WebRequest request = new WebRequest(UrlUtils.toUrlUnsafe(url));
//            Map<String, String> searchRequestHeader=new HashMap<>();
//            searchRequestHeader.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36");
//            searchRequestHeader.put("Host", "detail.tmall.com");
//            searchRequestHeader.put("Pragma", "no-cache");
//            searchRequestHeader.put("Upgrade-Insecure-Requests", "1");
//            request.setAdditionalHeaders(searchRequestHeader);

            wc.getCurrentWindow().getTopWindow().setOuterHeight(Integer.MAX_VALUE);
            wc.getCurrentWindow().getTopWindow().setInnerHeight(Integer.MAX_VALUE);

            Page page = wc.getPage(request);
            page.getEnclosingWindow().setOuterHeight(Integer.MAX_VALUE);
            page.getEnclosingWindow().setInnerHeight(Integer.MAX_VALUE);

            if(page.isHtmlPage()) {
                HtmlPage htmlPage = (HtmlPage) page;
                ScriptResult sr = htmlPage.executeJavaScript(String.format("javascript:window.scrollBy(0,%d);",Integer.MAX_VALUE));
                // 执行页面所有渲染相关的JS
                int left = 0;
                do {
                    left = wc.waitForBackgroundJavaScript(50);
//                    System.out.println(left);
                } while (left > 7); // 有6-7个时间超长的js任务

                htmlPage = (HtmlPage)sr.getNewPage();
                System.out.println(htmlPage.asXml());
              String asXml = htmlPage.getElementById("J_UlThumb").asXml();
              Set<String> imgStr = getImgStr(asXml);
              System.out.println(imgStr.toString());
              HtmlHeading1 object = (HtmlHeading1) htmlPage.getByXPath("//H1").get(1);
              System.out.println(object);
                detail = htmlPage.getElementById("description").asXml()
                        .replaceAll("src=\"//.{0,100}.png\" data-ks-lazyload=", "src=");  // 移除懒加载
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wc.close();
        }
        return detail;
    }
	
	//1：这个是拿到一个字符取得里面的图像地址返回一个List   
	public static Set<String> getImgStr(String htmlStr){  
	         String img="";        
	         Pattern p_image;        
	         Matcher m_image;        
	         Set<String> pics = new HashSet<String>();     
	        
	         String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址        
	         p_image = Pattern.compile      
	                 (regEx_img,Pattern.CASE_INSENSITIVE);        
	        m_image = p_image.matcher(htmlStr);      
	        while(m_image.find()){        
	             img = img + "," + m_image.group();        
	             Matcher m  = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src     
	             while(m.find()){  
	            	 String group = m.group(1);
	            	 if(group.substring(0,2).equals("//")){
	            		 group="https:"+group;
	            	 }
	            	 int indexOf = group.lastIndexOf(".");
	            	 String right=group.substring(indexOf+1,group.length());
	            	 String left=group.substring(0,indexOf);
	            	 String lefts=group.substring(0,left.lastIndexOf("."));
	            	 group=lefts+"."+right;
	            	 if(new URLAvailability().isConnect(group)!=null){
	            		 pics.add(group);  
	            	 }
	             }     
	         }        
	            return pics;        
	     } 
	
	public static void main(String[] args) {
		String tmallDetail = new TianMao().getTmallDetail("https://detail.tmall.com/item.htm?spm=a221t.1476805.6299412507.51.7f8f27adhbYy2I&id=564272531514&scm=1003.1.03175.ITEM_564272531514_114332&acm=03175.1003.1.114332&uuid=0zS3M5AB&pos=3&crid=37");
		System.out.println(tmallDetail);
	}
}
