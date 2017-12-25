package com.flchy.blog.inlets.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flchy.blog.base.exception.BusinessException;
import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.inlets.enums.Keys;
import com.flchy.blog.inlets.enums.StatusEnum;
import com.flchy.blog.inlets.holder.ConfigHolder;
import com.flchy.blog.inlets.mapper.CommentMapper;
import com.flchy.blog.inlets.service.ICommentService;
import com.flchy.blog.pojo.Comment;
import com.flchy.blog.utils.HttpRequestor;
import com.flchy.blog.utils.NewMapUtil;
import com.flchy.blog.utils.ValidUtils;
import com.flchy.blog.utils.ip.IpUtils;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author nieqs
 * @since 2017-11-02
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
	private static Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
	@Autowired
	private CommentMapper commentMapper;
	
	
	/**
	 * 查询分页
	 * @param page
	 * @param comment
	 * @return
	 */
	@Override
	public Page<Map<String, Object>> selectCommentPage(Page<Map<String, Object>> page,Comment comment){
		 page.setRecords( commentMapper.selectCommentPage(page, comment));
		 return page;
	}

	@Override
	public Comment saveComment(Comment comment) {
		if(comment.getIsAdmin()){
			comment.setStatus(StatusEnum.NORMAL.getCode());
			isSendMail(comment);
		}else{
			try {
				if(Boolean.valueOf(ConfigHolder.getConfig(Keys.IS_COMMENT_VERIFY.getKey())) ){
					comment.setStatus(StatusEnum.DRAFT.getCode());
				}else{
					comment.setStatus(StatusEnum.NORMAL.getCode());
					isSendMail(comment);
				}
			} catch (Exception e) {
				logger.error("判断是否需要审核异常  || 不影响运行:"+e.getMessage());
			}
			isSendMailAdmin(comment);
		}
		comment.setServerIp(IpUtils.getHostAddress());
		comment.setCreateTime(new Date());
		comment.insert();
		return comment;
	}
	/**
	 * 发送邮件给管理员
	 * @param comment
	 */
	private void isSendMailAdmin(Comment comment) {
		if(Boolean.valueOf(ConfigHolder.getConfig(Keys.IS_SEND_MAIL_ADMIN.getKey()))){
			//给管理员发送邮件
			new Thread(new Runnable() {
				@Override
				public void run() {
					String urlPath=comment.getArticleId()==-1?"/about":"/detail/"+comment.getArticleId();
					urlPath=ConfigHolder.getConfig(Keys.WEBSITE_URL.getKey())+urlPath;
					String content=ConfigHolder.getConfig(Keys.COMMENT_MAIL_NOTIFICATION_ADMIN_CONTENT.getKey()).replace("{content}", comment.getComment()).replace("{urlPath}", urlPath).replace("{website}", ConfigHolder.getConfig(Keys.WEBSITE_URL.getKey())).replace("{webName}", ConfigHolder.getConfig(Keys.WEBSITE_NAME.getKey()));
					try {
						new HttpRequestor().doPost(ConfigHolder.getConfig(Keys.MAIL_HTTP_ADDRESS.getKey()), new NewMapUtil()
								.set("to", ConfigHolder.getConfig(Keys.ADMIN_MAIL.getKey()))
								.set("title",ConfigHolder.getConfig(Keys.COMMENT_MAIL_NOTIFICATION_ADMIN_TITLE.getKey()).replace("{sendName}", comment.getNickname()))
								.set("content",content )
								.get(), null);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("发送邮件失败:"+e.getMessage());
					}
				}
			}).start();;
		}
	}
	/**
	 * 发送邮件
	 * @param comment
	 */
	private void isSendMail(Comment comment) {
		if(comment.getUpperId()!=-1){
			new Thread(new Runnable() {
				@Override
				public void run() {
					Comment selectById = selectById(comment.getUpperId());
					if(selectById!=null){
						if(selectById.getNotice() && ValidUtils.isEmail(selectById.getMail())){
							String urlPath=comment.getArticleId()==-1?"/about":"/detail/"+comment.getArticleId();
							urlPath=ConfigHolder.getConfig(Keys.WEBSITE_URL.getKey())+urlPath;
							String content=ConfigHolder.getConfig(Keys.COMMENT_MAIL_NOTIFICATION_CONTENT.getKey()).replace("{content}", comment.getComment()).replace("{urlPath}", urlPath).replace("{website}", ConfigHolder.getConfig(Keys.WEBSITE_URL.getKey())).replace("{webName}", ConfigHolder.getConfig(Keys.WEBSITE_NAME.getKey()));
							// comment.getComment()+"<br /> 地址:<a href='https://flchy.cn/"+urlPath+"'>https://flchy.cn/"+urlPath+"</a> <br/><br/><br/><a href='https://flchy.cn'>. Blog</a>";
							try {
//								+selectById.getNickname()+" 你好,收到来自【"+comment.getNickname()+"】的回复"
								new HttpRequestor().doPost(ConfigHolder.getConfig(Keys.MAIL_HTTP_ADDRESS.getKey()), new NewMapUtil()
										.set("to", selectById.getMail())
										.set("title",ConfigHolder.getConfig(Keys.COMMENT_MAIL_NOTIFICATION_TITLE.getKey()).replace("{receiveName}", selectById.getNickname()).replace("{sendName}", comment.getNickname()))
										.set("content",content )
										.get(), null);
							} catch (Exception e) {
								e.printStackTrace();
								logger.error("发送邮件失败:"+e.getMessage());
							}
						}
					}
				}
			}).start();;
		}
	}
	@Override
	public PageHelperResult selectWebComment(Integer articleId, Integer current, Integer size,String nickName) {
		if (articleId == null)
			throw new BusinessException("文章ID不能为空");
		Page<Comment> page = new Page<>(Integer.valueOf(current), Integer.valueOf(size));
		 Wrapper<Comment> wrapper = new EntityWrapper<Comment>().where("articleId={0}", articleId).and(" upperId={0} ", -1);
		 if(nickName!=null && !nickName.equals("") ){
			 wrapper.and(" ( `status` ={0} or (`status`=2 and nickname={1} )) ", 1,nickName);	
		 }else{
			 wrapper.and(" ( `status` ={0} ) ", 1);
		 }
		 wrapper.orderBy("create_time", false);
		this.selectPage(page,wrapper);
		List<Comment> records = page.getRecords();
		ArrayList<Map<String, Object>> collect = records.stream().collect(() -> new ArrayList<Map<String, Object>>(),
				(ls, item) -> ls.add(
						new NewMapUtil().set("id", item.getId()).set("articleId", articleId).set("status", 1).set("nickName", nickName).get()),
				(one, two) -> one.addAll(two));
		if(records.size()>0){
			List<Comment> selectWebComment = commentMapper.selectWebComment(collect);
			Map<Integer, List<Comment>> collect2 = selectWebComment.stream().collect(Collectors.groupingBy(Comment::getUpperId));
			List<Comment> list = collect2.get(-1);
			for (Comment comment : list) {
				List<Comment> l=new ArrayList<>();
				l=getChildComment(comment.getId(), collect2, l);
				comment.setList(l);
			}
			page.setRecords(list);
		}

		return new PageHelperResult(page);
	}
	
	public List<Comment>  getChildComment(Integer id,Map<Integer, List<Comment>> comments,List<Comment> childComment){
		List<Comment> list = comments.get(id);
		if(list!=null){
			childComment.addAll(list);
			for (Comment comment : list) {
				childComment=	getChildComment(comment.getId(), comments, childComment);
			}
		}
		return childComment;
	}
	
	
	
	
	 public static String hex(byte[] array) {
	      StringBuffer sb = new StringBuffer();
	      for (int i = 0; i < array.length; ++i) {
	      sb.append(Integer.toHexString((array[i]
	          & 0xFF) | 0x100).substring(1,3));        
	      }
	      return sb.toString();
	  }
	  public static String md5Hex (String message) {
	      try {
	      MessageDigest md = 
	          MessageDigest.getInstance("MD5");
	      return hex (md.digest(message.getBytes("CP1252")));
	      } catch (NoSuchAlgorithmException e) {
	      } catch (UnsupportedEncodingException e) {
	      }
	      return null;
	  }
	  
	  public static void main(String[] args) {
		  String email = "flchy.cn";
		  String hash = CommentServiceImpl.md5Hex(email);
		  System.out.println(hash);
	}

}
