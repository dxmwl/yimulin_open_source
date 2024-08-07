package com.yimulin.mobile.listener;

/**
 * @ClassName: PageDecorationLastJudge
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/7/13 22:15
 **/
public interface PageDecorationLastJudge {
    boolean isLastRow(int position);
    boolean isLastColumn(int position);
    boolean isPageLast(int position);
}
