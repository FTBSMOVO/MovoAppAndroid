package com.movocash.movo.view.ui.main.others

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentMovoChainBinding
import com.movocash.movo.utilities.Constants

import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.auth.WelcomeFragment
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.cashcard.CashCardFragment
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [movoChain.newInstance] factory method to
 * create an instance of this fragment.
 */
class movoChain : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentMovoChainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movo_chain, container, false)
        /*binding.movoChain.loadUrl("www.movochain.com/")
        binding.movoChain.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
               // binding.pbLoading.visibility = View.GONE
            }
        }*/


        setViews()
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setViews() {
       // binding.tvTitle.isSelected = true
        binding.movoChain.setMixedContentAllowed(true)
        binding.movoChain.loadUrl("www.movochain.com/")

        binding.movoChain.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                //binding.pbLoading.visibility = View.GONE
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment movoChain.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            movoChain().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Toast.makeText(context, "back pressed", Toast.LENGTH_SHORT).show()


                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                //callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "SignInFragment")
                addFragment(R.id.mainContainer, MyCardsFragment(), null)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}



  /*  override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.movoChain -> {

            }
        }
    }

}*/