package com.movocash.movo.view.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentEnrolmentBinding
import com.movocash.movo.utilities.extensions.showMyInfoDialog
import com.movocash.movo.utilities.extensions.showStylishDialog
import com.movocash.movo.utilities.helper.VPDashBoardAdapter
import com.movocash.movo.view.ui.base.ActivityBase

class EnrollmentFragment : BaseFragment(), ViewPager.OnPageChangeListener,View.OnClickListener {

    companion object {
        lateinit var binding: FragmentEnrolmentBinding
        lateinit var adapter: VPDashBoardAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enrolment, container, false)
        setupViewPager()
        setListener()
        return binding.root
    }

     override fun onClick(v: View?) {
         when (v!!.id) {
             R.id.personalDetailsInfo -> {

                // showMyInfoDialog("","To verify your identity and create a Secure login, so you can use MOVO, we need to validate your Social Security Number.","")
                 showStylishDialog("",getString(R.string.msg3),"")


             }

         }
     }

    private fun setListener() {
        binding.vpEnrol.addOnPageChangeListener(this)
        binding.personalDetailsInfo.setOnClickListener(this)
    }

    private fun setupViewPager() {
        adapter = VPDashBoardAdapter(childFragmentManager)
        adapter.addFragment(RegisterFragment(), "DashboardFragment")
        adapter.addFragment(MediaFragment(), "EventsFragment")
        adapter.addFragment(VerificationCodeFragment(), "WalletFragment")
        binding.vpEnrol.adapter = adapter
        binding.vpEnrol.setCurrentItem(0, true)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        when (position) {
            0 -> {
                binding.rl1.background = ContextCompat.getDrawable(ActivityBase.activity, R.drawable.bg_hollow_circle)
                binding.rl2.background = ContextCompat.getDrawable(ActivityBase.activity, R.drawable.bg_hollow_light_circle)
                binding.rl3.background = ContextCompat.getDrawable(ActivityBase.activity, R.drawable.bg_hollow_light_circle)

                binding.tv1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tv2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.light_separator))
                binding.tv3.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.light_separator))

                binding.tvContent.visibility = View.VISIBLE
                binding.tvPersonal.visibility = View.VISIBLE
                binding.tvContent.text = "Please enter in your personal information below"
                binding.tvPersonal.text = "Personal Details"
            }
            1 -> {
                binding.rl1.background = ContextCompat.getDrawable(ActivityBase.activity, R.drawable.bg_hollow_light_circle)
                binding.rl2.background = ContextCompat.getDrawable(ActivityBase.activity, R.drawable.bg_hollow_circle)
                binding.rl3.background = ContextCompat.getDrawable(ActivityBase.activity, R.drawable.bg_hollow_light_circle)

                binding.tv1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.light_separator))
                binding.tv2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tv3.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.light_separator))
                binding.tvContent.visibility = View.VISIBLE
                binding.tvPersonal.visibility = View.VISIBLE
                binding.tvContent.text = "Follow these instructions for identify verification. For best results, make sure to zoom out and fit the image in the frame."
                binding.tvPersonal.text = "Personal Details"
            }
            2 -> {
                binding.rl1.background = ContextCompat.getDrawable(ActivityBase.activity, R.drawable.bg_hollow_light_circle)
                binding.rl2.background = ContextCompat.getDrawable(ActivityBase.activity, R.drawable.bg_hollow_light_circle)
                binding.rl3.background = ContextCompat.getDrawable(ActivityBase.activity, R.drawable.bg_hollow_circle)

                binding.tv1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.light_separator))
                binding.tv2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.light_separator))
                binding.tv3.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvContent.visibility = View.GONE
                binding.tvPersonal.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val fragment = adapter.getItem(binding.vpEnrol.currentItem)
            if (fragment is MediaFragment) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
            if (fragment is VerifyCodeFragment) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}