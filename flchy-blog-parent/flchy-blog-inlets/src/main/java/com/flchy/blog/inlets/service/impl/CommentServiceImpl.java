package com.flchy.blog.inlets.service.impl;

import java.util.ArrayList;
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
		comment.insert();
		return comment;
	}
	@Override
	public ResultPage selectWebComment(Integer articleId, Integer current, Integer size) {
		if (articleId == null)
			throw new BusinessException("文章ID不能为空");
		Page<Comment> page = new Page<>(Integer.valueOf(current), Integer.valueOf(size));
		this.selectPage(page, new EntityWrapper<Comment>().where("articleId={0} and upperId=-1 ", articleId));
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

}
