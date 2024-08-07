package com.yimulin.mobile.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @project : YinMan
 * @Description : 首页Fragment适配器
 * @author : 常利兵
 * @time : 2021/12/29
 */
class MainFragmentAdapter(
    fragmentManager: FragmentManager,
    private val fragments: ArrayList<Fragment>,
    private val tabList: List<String>
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabList[position]
    }
}