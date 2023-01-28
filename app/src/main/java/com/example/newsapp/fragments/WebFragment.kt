package com.example.newsapp.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentWebBinding

class WebFragment : Fragment() {

    private lateinit var binding: FragmentWebBinding
    private val args: WebFragmentArgs by navArgs()

    private var progressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUrl = args.webUrl

        createProgressDialog()

        Handler(Looper.getMainLooper()).postDelayed({
            if (progressDialog?.isShowing == true) progressDialog!!.hide()
            binding.webView.loadUrl(currentUrl)
        }, 4000)
    }

    private fun createProgressDialog() {
        progressDialog = Dialog(context!!)
        progressDialog?.apply {
            setContentView(R.layout.loading_dialog)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }
}