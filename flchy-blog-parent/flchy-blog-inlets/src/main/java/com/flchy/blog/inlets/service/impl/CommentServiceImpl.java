package com.flchy.blog.inlets.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flchy.blog.base.exception.BusinessException;
import com.flchy.blog.base.response.ResultPage;
import com.flchy.blog.inlets.mapper.CommentMapper;
import com.flchy.blog.inlets.service.ICommentService;
import com.flchy.blog.pojo.Article;
import com.flchy.blog.pojo.Comment;
import com.flchy.blog.utils.NewMapUtil;

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
	@Autowired
	private CommentMapper commentMapper;

	@Override
	public Comment saveComment(Comment comment) {
		
		comment.setStatus(1);
		comment.setCreateTime(new Date());
		comment.insert();
		return comment;
	}
	@Override
	public ResultPage selectWebComment(Integer articleId, Integer current, Integer size) {
		if (articleId == null)
			throw new BusinessException("文章ID不能为空");
		Page<Comment> page = new Page<>(Integer.valueOf(current), Integer.valueOf(size));
		this.selectPage(page, new EntityWrapper<Comment>().where("articleId={0}", articleId).and(" upperId={0} ", -1).and(" `status`={0} ", 1).orderBy("create_time", false));
		List<Comment> records = page.getRecords();
		ArrayList<Map<String, Object>> collect = records.stream().collect(() -> new ArrayList<Map<String, Object>>(),
				(ls, item) -> ls.add(
						new NewMapUtil().set("id", item.getId()).set("articleId", articleId).set("status", 1).get()),
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

		return new ResultPage(page);
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
