package com.flchy.blog.common.crawler.htmlunit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.flchy.blog.common.crawler.htmlunit.entity.CommodityDetails;
import com.flchy.blog.common.crawler.htmlunit.entity.Types;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

public class Taobao {

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

	public String getTaobaoDetail(String url) {
		CommodityDetails commodityDetails = new CommodityDetails();
		List<Types> typeList=new ArrayList<Types>();
		WebClient wc = newWebClient();

		String detail = "";

		try {
			WebRequest request = new WebRequest(UrlUtils.toUrlUnsafe(url));
			Map<String, String> searchRequestHeader = new HashMap<>();
			searchRequestHeader.put("Referer", url);
			request.setAdditionalHeaders(searchRequestHeader);

			Page page = wc.getPage(request);

			if (page.isHtmlPage()) {
				HtmlPage htmlPage = (HtmlPage) page;
				String asXml = htmlPage.getElementById("J_UlThumb").asXml();
				Set<String> imgStr = getImgStr(asXml);
				commodityDetails.setImage(imgStr);
				HtmlHeading3 object = (HtmlHeading3) htmlPage.getByXPath("//h3").get(0);
				String title = object.asText();
				commodityDetails.setTitle(title);
				// String html = htmlPage.asXml();
				// System.out.println(htmlPage.getElementById("J_PromoPriceNum").asText());//价格
				DomElement elementById = htmlPage.getElementById("J_StrPrice");
				Iterable<DomElement> childElements2 = elementById.getChildElements();
				for (DomElement domElement : childElements2) {
					System.out.println(domElement.asXml());
					String asText = domElement.asText();
					boolean number = NumberUtils.isNumber(asText);
					if (number) {
						// 原价
						commodityDetails.setOriginalPrice(Double.valueOf(asText));
					}
				}
				DomElement J_PromoHd = htmlPage.getElementById("J_PromoHd");
				System.out.println(J_PromoHd.asXml());
				Iterable<DomElement> childElements3 = J_PromoHd.getChildElements();
				for (DomElement domElement : childElements3) {
					String asText = domElement.asText();
					if (NumberUtils.isNumber(asText)) {
						System.out.println(asText);
					}
				}
				// 库存
				String asText = htmlPage.getElementById("J_SpanStock").asText();
				commodityDetails.setStock(Integer.valueOf(asText));
				List<HtmlUnorderedList> byXPath = (List<HtmlUnorderedList>) htmlPage.getByXPath("//ul");
				for (HtmlUnorderedList object2 : byXPath) {
					String attribute = object2.getAttribute("data-property");
					if (attribute != null && !attribute.equals("")) {
						Types types = new Types();
						types.setTypeName(attribute.trim());
						System.out.println(attribute);
						System.out.println(object2.asXml());
						Iterable<DomElement> childElements = object2.getChildElements();
						Set<String> list = new HashSet<>();
						for (DomElement domElement : childElements) {
							String asXml2 = domElement.asXml();
							asXml2 = asXml2.substring(asXml2.indexOf("<span>") + 6, asXml2.indexOf("</span>"));
							list.add(asXml2);
						}
						types.setList(list);
						typeList.add(types);
					}

				}
				DomNodeList<HtmlElement> script = htmlPage.getHead().getElementsByTagName("script");
				String detailUrl = "";
				for (HtmlElement elm : script) {
					String textContent = elm.getTextContent();
					if (textContent.contains("var g_config = {")) {
						for (String line : textContent.split("\n")) {
							if (line.startsWith("        descUrl")) {
								detailUrl = "http:" + getFirstMatch(line,
										"'//dsc.taobaocdn.com/i[0-9]+/[0-9]+/[0-9]+/[0-9]+/.+[0-9]+'\\s+:")
												.replaceAll("\\s+:", "").replace("'", "");
								break;
							}

						}
						break;
					}
				}
				if (StringUtils.isNotBlank(detailUrl))
					detail = wc.getPage(detailUrl).getWebResponse().getContentAsString().replace("var desc='", "")
							.replace("';", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			wc.close();
		}
		
		return detail;
	}

	public static void main(String[] args) {
		String taobaoDetail = new Taobao().getTaobaoDetail(
				"https://item.taobao.com/item.htm?spm=a219r.lm874.14.43.2d18fa06FbAAf5&id=537569558868&ns=1&abbucket=19");
		// String taobaoDetail = new
		// Taobao().getTaobaoDetail("https://item.taobao.com/item.htm?id=528414286240&ali_refid=a3_430584_1006:1104972422:N:%E5%AE%B6%E5%85%B7:a68d702f4bfefe8e95086672ecdd2008&ali_trackid=1_a68d702f4bfefe8e95086672ecdd2008&spm=a219r.lm5704.14.1#detail");
		System.out.println(taobaoDetail);
	}

	// 1：这个是拿到一个字符取得里面的图像地址返回一个List
	public static Set<String> getImgStr(String htmlStr) {
		String img = "";
		Pattern p_image;
		Matcher m_image;
		Set<String> pics = new HashSet<String>();

		String regEx_img = "<img.*src=(.*?)[^>]*?>"; // 图片链接地址
		p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(htmlStr);
		while (m_image.find()) {
			img = img + "," + m_image.group();
			Matcher m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); // 匹配src
			while (m.find()) {
				String group = m.group(1);
				if (group.substring(0, 2).equals("//")) {
					group = "https:" + group;
				}
				int indexOf = group.lastIndexOf(".");
				String right = group.substring(indexOf + 1, group.length());
				String left = group.substring(0, indexOf);
				String lefts = group.substring(0, left.lastIndexOf("."));
				group = lefts + "." + right;
				if (new URLAvailability().isConnect(group) != null) {
					pics.add(group);
				}
			}
		}
		return pics;
	}

	public static String getFirstMatch(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		String ret = null;
		if (matcher.find()) {
			ret = matcher.group();
		}
		return ret;
	}
}
