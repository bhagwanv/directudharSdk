package com.sk.directudhar.ui.approvalpending

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sk.directudhar.data.NetworkResult
import com.sk.directudhar.databinding.FragmentApprovalPendingBinding
import com.sk.directudhar.ui.mainhome.MainActivitySDk
import com.sk.directudhar.utils.DaggerApplicationComponent
import com.sk.directudhar.utils.ProgressDialog
import com.sk.directudhar.utils.SharePrefs
import com.sk.directudhar.utils.Utils
import com.sk.directudhar.utils.Utils.Companion.toast
import okhttp3.internal.UTC
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class ApprovalPendingFragment:Fragment() {

    lateinit var activitySDk: MainActivitySDk

    private lateinit var mBinding:FragmentApprovalPendingBinding

    lateinit var approvalPendingViewModel: ApprovalPendingViewModel
    @Inject
    lateinit var approvalPendingFactory: ApprovalPendingFactory


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activitySDk= context as MainActivitySDk

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentApprovalPendingBinding.inflate(inflater, container, false)
        initView()
        return mBinding.root
    }

    private fun initView() {

        val component = DaggerApplicationComponent.builder().build()
        component.injectApprovalPending(this)
        approvalPendingViewModel =
            ViewModelProvider(this, approvalPendingFactory)[ApprovalPendingViewModel::class.java]

        /*Log.e("TAG", "initView:${SharePrefs.getInstance(activitySDk)!!.getInt(SharePrefs.LEAD_MASTERID)} ", )*/

        approvalPendingViewModel.displayDisbursalAmount(SharePrefs.getInstance(activitySDk)!!.getInt(SharePrefs.LEAD_MASTERID))




        approvalPendingViewModel.displayDisbursalAmountResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.instance!!.show(activitySDk)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.instance!!.dismiss()
                    Toast.makeText(activitySDk, it.errorMessage, Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Success -> {
                    ProgressDialog.instance!!.dismiss()

                    if (it.data != null) {

                    }
                }
            }
        }

        approvalPendingViewModel.updateLeadSuccessResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.instance!!.show(activitySDk)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.instance!!.dismiss()
                    Toast.makeText(activitySDk, it.errorMessage, Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Success -> {
                    ProgressDialog.instance!!.dismiss()

                    if (it.data.Result != null) {
                        activitySDk.checkSequenceNo(it.data.Data.SequenceNo)

                    }else{
                        if (it.data.Msg!=null){
                            activitySDk.toast(it.data.Msg)
                        }
                    }
                }
            }
        }
    }
}