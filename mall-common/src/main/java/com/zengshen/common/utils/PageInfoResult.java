package com.zengshen.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoResult {
//
//    /**
//     * 当前页数
//     */
//    private Integer page;
//
//    /**
//     * 当前页的记录数
//      */
//    private Integer pageSize;
//
//    /**
//     * 总页数
//     */
//    private Integer pages;
//
//    /**
//     * 总记录数
//     */
//    private Long total;
//
//    /**
//     * 是否是最后一页
//     */
//    private Boolean isLastPage;
//
//    /**
//     * 每页显示的内容
//     */
//    private List<?> list;

    private int page;			// 当前页数
    private int total;			// 总页数
    private long records;		// 总记录数
    private List<?> rows;		// 每行显示的内容

}
