package com.yimulin.mobile.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.gyf.immersionbar.ImmersionBar
import com.hjq.base.FragmentPagerAdapter
import com.yimulin.mobile.R
import com.pdlbox.tools.utils.SpUtils
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppApplication
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.manager.ActivityManager
import com.yimulin.mobile.manager.DialogManager
import com.yimulin.mobile.other.DoubleClickHelper
import com.yimulin.mobile.ui.adapter.NavigationAdapter
import com.yimulin.mobile.ui.dialog.StarAppDialog
import com.yimulin.mobile.ui.fragment.LocalToolsFragment
import com.yimulin.mobile.ui.fragment.OnlineToolsFragment
import com.yimulin.mobile.utils.CosManager

/**
 * 主页面
 */
class MainActivity : AppActivity(), NavigationAdapter.OnNavigationListener {

    companion object {

        private const val INTENT_KEY_IN_FRAGMENT_INDEX: String = "fragmentIndex"
        private const val INTENT_KEY_IN_FRAGMENT_CLASS: String = "fragmentClass"

        @JvmOverloads
        fun start(
            context: Context,
            fragmentClass: Class<out AppFragment<*>?>? = LocalToolsFragment::class.java
        ) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(INTENT_KEY_IN_FRAGMENT_CLASS, fragmentClass)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val viewPager: ViewPager? by lazy { findViewById(R.id.vp_home_pager) }
    private val navigationView: RecyclerView? by lazy { findViewById(R.id.rv_home_navigation) }
    private var navigationAdapter: NavigationAdapter? = null
    private var pagerAdapter: FragmentPagerAdapter<AppFragment<*>>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        //初始化COS
        CosManager.init(AppApplication.getApplication())

        navigationAdapter = NavigationAdapter(this).apply {
            addItem(
                NavigationAdapter.MenuItem(
                    getString(R.string.home_nav_index),
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.home_home_selector)
                )
            )
//            addItem(
//                NavigationAdapter.MenuItem(
//                    getString(R.string.home_nav_found),
//                    ContextCompat.getDrawable(this@MainActivity, R.drawable.home_found_selector)
//                )
//            )
            setOnNavigationListener(this@MainActivity)
            navigationView?.adapter = this
        }
    }

    override fun initData() {

        showMainDialog()

        pagerAdapter = FragmentPagerAdapter<AppFragment<*>>(this).apply {
            addFragment(LocalToolsFragment.newInstance())
//            addFragment(OnlineToolsFragment.newInstance())
            viewPager?.adapter = this
        }
        onNewIntent(intent)
    }

    private fun showMainDialog() {
        showCommentDialog()
    }

    private fun showCommentDialog() {
        val launchCount = SpUtils.getLong("APP_DATA", "LAUNCH_COUNT", 0)
        SpUtils.put("APP_DATA", "LAUNCH_COUNT", launchCount + 1)
        if ((launchCount % 6) == 5L) {
            DialogManager.getInstance(this).addShow(StarAppDialog.Builder(this).create())
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        pagerAdapter?.let {
            switchFragment(it.getFragmentIndex(getSerializable(INTENT_KEY_IN_FRAGMENT_CLASS)))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewPager?.let {
            // 保存当前 Fragment 索引位置
            outState.putInt(INTENT_KEY_IN_FRAGMENT_INDEX, it.currentItem)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // 恢复当前 Fragment 索引位置
        switchFragment(savedInstanceState.getInt(INTENT_KEY_IN_FRAGMENT_INDEX))
    }

    private fun switchFragment(fragmentIndex: Int) {
        if (fragmentIndex == -1) {
            return
        }
        when (fragmentIndex) {
            0, 1 -> {
                viewPager?.currentItem = fragmentIndex
                navigationAdapter?.setSelectedPosition(fragmentIndex)
            }
        }
    }

    /**
     * [NavigationAdapter.OnNavigationListener]
     */
    override fun onNavigationItemSelected(position: Int): Boolean {
        return when (position) {
            0, 1 -> {
                viewPager?.currentItem = position
                true
            }

            else -> false
        }
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig() // 指定导航栏背景颜色
            .navigationBarColor(R.color.white)
    }

    override fun onBackPressed() {
        if (!DoubleClickHelper.isOnDoubleClick()) {
            toast(R.string.home_exit_hint)
            return
        }

        // 移动到上一个任务栈，避免侧滑引起的不良反应
        moveTaskToBack(false)
        postDelayed({
            // 进行内存优化，销毁掉所有的界面
            ActivityManager.getInstance().finishAllActivities()
        }, 300)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager?.adapter = null
        navigationView?.adapter = null
        navigationAdapter?.setOnNavigationListener(null)
    }
}