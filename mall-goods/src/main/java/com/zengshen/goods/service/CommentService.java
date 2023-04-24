package com.zengshen.goods.service;

import com.zengshen.common.utils.PageInfoResult;
import com.zengshen.goods.model.vo.CommentLevelCountsVO;

public interface CommentService {
    CommentLevelCountsVO getCommentLevelCounts(String itemId);

   PageInfoResult getCommentList(String itemId, String level, int page, int pageSize);

    PageInfoResult getCommentByUserId(String userId, int page, int pageSize);
}
