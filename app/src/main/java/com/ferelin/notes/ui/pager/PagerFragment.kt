package com.ferelin.notes.ui.pager

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.ferelin.notes.R
import com.ferelin.notes.base.BaseFragment
import com.ferelin.notes.databinding.FragmentPagerBinding
import com.ferelin.notes.utilits.TabManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PagerFragment : BaseFragment(), PagerMvpView {

    private val mPresenter = PagerPresenter<PagerMvpView>()
    private lateinit var mBinding: FragmentPagerBinding

    private lateinit var mTabListener: TabLayout.OnTabSelectedListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentPagerBinding.inflate(inflater, container, false)
        mPresenter.attachView(this)
        return mBinding.root
    }

    override fun setUp(view: View) {
        mBinding.viewPager.adapter = PagerTabAdapter(childFragmentManager, lifecycle)
        configTabLayout()
    }

    override fun changeCurrentPagerScreen(position: Int, smoothScroll: Boolean) {
        mBinding.viewPager.setCurrentItem(position, smoothScroll)
    }

    override fun changeIconTintToOrange(icon: Drawable) {
        icon.setTint(ContextCompat.getColor(requireContext(), R.color.orangeAccent))
    }

    override fun changeIconTintToGray(icon: Drawable) {
        icon.setTint(ContextCompat.getColor(requireContext(), R.color.grayTabIcons))
    }

    private fun configTabLayout() {
        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager) { tabConfig, position ->
            tabConfig.setIcon(
                when (position) {
                    0 -> R.drawable.ic_notes
                    1 -> R.drawable.ic_settings
                    else -> throw NoSuchElementException("No drawable for tab layout position $position")
                }
            )
        }.attach()

        mBinding.tabLayout.apply {
            getTabAt(0)!!.icon!!.setTint(ContextCompat.getColor(requireContext(), R.color.orangeAccent))
            mTabListener = object : TabManager {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    mPresenter.onTabSelected(tab!!)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    mPresenter.onTabUnselected(tab!!)
                }
            }
            addOnTabSelectedListener(mTabListener)
        }
    }

    override fun onDestroy() {
        mBinding.tabLayout.removeOnTabSelectedListener(mTabListener)
        mPresenter.detachView()
        super.onDestroy()
    }
}