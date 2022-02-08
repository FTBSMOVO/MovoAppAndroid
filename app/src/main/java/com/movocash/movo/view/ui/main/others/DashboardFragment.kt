package com.movocash.movo.view.ui.main.others

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentDashboardBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.view.ui.base.ActivityBase
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment

class DashboardFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
//        playAnimations()
        binding.tvTitle.isSelected = true
        binding.tvTitle.text = "${MovoApp.db.getString(Constants.FIRST_NAME)} ${MovoApp.db.getString(Constants.LAST_NAME)}"
        setListeners()
        return binding.root
    }

    private fun playAnimations() {
        YoYo.with(Techniques.ZoomInLeft).duration(700).repeat(0).playOn(binding.rlSupport)
        YoYo.with(Techniques.ZoomIn).duration(700).repeat(0).playOn(binding.rlMovoCredit)
        YoYo.with(Techniques.ZoomInLeft).duration(700).repeat(0).playOn(binding.rlTerms)
        YoYo.with(Techniques.ZoomIn).duration(700).repeat(0).playOn(binding.rlPrivacy)
        YoYo.with(Techniques.ZoomInLeft).duration(700).repeat(0).playOn(binding.rlChangePass)
    }

    private fun setListeners() {
        binding.rlMovoCredit.setOnClickListener(this)
        binding.rlChangePass.setOnClickListener(this)
        binding.rlPrivacy.setOnClickListener(this)
        binding.rlSupport.setOnClickListener(this)
        binding.rlTerms.setOnClickListener(this)
        binding.rlLeft.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlMovoCredit -> {
                binding.rlMovoCredit.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.rlChangePass.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlPrivacy.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlSupport.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlTerms.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))

                binding.ivCredit.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.ivChangePass.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivPrivacy.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivMessage.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivTerms.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))

                binding.tvCredit.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.tvChangePass.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvPrivacy.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvSupport.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvTerms.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            }
            R.id.rlChangePass -> {
                binding.rlMovoCredit.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlChangePass.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.rlPrivacy.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlSupport.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlTerms.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))

                binding.ivCredit.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivChangePass.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.ivPrivacy.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivMessage.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivTerms.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))

                binding.tvCredit.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvChangePass.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.tvPrivacy.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvSupport.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvTerms.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            }
            R.id.rlPrivacy -> {
                binding.rlMovoCredit.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlChangePass.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlPrivacy.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.rlSupport.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlTerms.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))

                binding.ivCredit.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivChangePass.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivPrivacy.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.ivMessage.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivTerms.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))

                binding.tvCredit.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvChangePass.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvPrivacy.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.tvSupport.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvTerms.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            }
            R.id.rlSupport -> {
                binding.rlMovoCredit.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlChangePass.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlPrivacy.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlSupport.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.rlTerms.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))

                binding.ivCredit.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivChangePass.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivPrivacy.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivMessage.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.ivTerms.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))

                binding.tvCredit.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvChangePass.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvPrivacy.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvSupport.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.tvTerms.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            }
            R.id.rlTerms -> {
                binding.rlMovoCredit.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlChangePass.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlPrivacy.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlSupport.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.rlTerms.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ActivityBase.activity, R.color.black))

                binding.ivCredit.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivChangePass.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivPrivacy.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivMessage.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                binding.ivTerms.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.white))

                binding.tvCredit.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvChangePass.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvPrivacy.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvSupport.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvTerms.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            }

            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
        }
    }
}