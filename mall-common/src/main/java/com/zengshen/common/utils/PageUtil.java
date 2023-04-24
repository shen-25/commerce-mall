package com.zengshen.common.utils;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页工具类
 * @author word
 */
public class PageUtil {

    public static PageInfoResult setPageInfoResult(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PageInfoResult pageInfoResult = new PageInfoResult();
        pageInfoResult.setPage(page);
        pageInfoResult.setRows(list);
        pageInfoResult.setTotal(pageList.getPages());
        pageInfoResult.setRecords(pageList.getTotal());
        return pageInfoResult;
    }
}
