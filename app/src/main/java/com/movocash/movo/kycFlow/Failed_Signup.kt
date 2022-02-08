package com.movocash.movo.kycFlow

import android.content.Context
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentFailedSignupBinding
import com.movocash.movo.databinding.FragmentMediaBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.auth.SendCodeFragment
import com.movocash.movo.view.ui.auth.SignInFragment
import com.movocash.movo.view.ui.auth.WelcomeFragment
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.profile.WebContentFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Failed_Signup.newInstance] factory method to
 * create an instance of this fragment.
 */
class Failed_Signup : BaseFragment() , View.OnClickListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentFailedSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_failed__signup, container, false)
        //binding.errorCode.setText(errorCode)
        binding.textView167.setText(Html.fromHtml(errorCode))

        setListener()

        return binding.root
    }

    companion object {
        lateinit var instance: Failed_Signup
        private var errorCode = ""

        fun newInstance(error: String): Failed_Signup {
            instance = Failed_Signup()
            errorCode = error
            return instance
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Toast.makeText(context, "back pressed", Toast.LENGTH_SHORT).show()

                callFragmentWithReplace(R.id.authContainer, WelcomeFragment(), "SignInFragment")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    private fun setListener() {
        binding.btnTryAgain.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)


//        binding.rlSelectionType.setOnClickListener(this)
//        binding.tvSelectionType.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnTryAgain -> callFragmentWithReplace(R.id.authContainer, WelcomeFragment(), "SignInFragment")
            R.id.btnCancel -> callFragmentWithReplace(R.id.authContainer, SignInFragment(), "SignInFragment")

        }
    }
}