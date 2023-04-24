package com.zengshen.goods.mapper;

import com.zengshen.goods.model.vo.CommentVO;
import com.zengshen.goods.model.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsCommentsCustomMapper {
    List<CommentVO> getCommentList(@Param("itemId") String itemId, @Param("level") String level);

    public List<MyCommentVO> queryMyComments(@Param("userId")String userId);

}