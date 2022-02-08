package com.movocash.movo.view.ui.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import com.movocash.movo.view.ui.base.ActivityBase
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.movocash.movo.view.ui.main.MainActivity.Companion.binding

@SuppressLint("ValidFragment")
class BottomSheetSelectorFragment constructor(val mList: ArrayList<String>, private val selectedPos: Int, val type: Int) : BottomSheetDialogFragment() {

    private lateinit var mListener: ISelectListener

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.fragment_bottom_selector, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(ContextCompat.getColor(ActivityBase.activity,android.R.color.transparent))
        val rvSelection = dialog.findViewById<RecyclerView>(R.id.rvSelection)
        mList.add(0, "")
        mList.add("")
        val linearLayoutManager = LinearLayoutManager(ActivityBase.activity)
        rvSelection.layoutManager = linearLayoutManager
        rvSelection.adapter = AdapterBottomSheet(
            ActivityBase.activity,
            mList
        )

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvSelection)

        if (selectedPos != 0) {
            rvSelection.scrollToPosition(selectedPos)
        }

        rvSelection.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                linearLayoutManager.findViewByPosition(linearLayoutManager.findFirstVisibleItemPosition())!!.scaleY =
                        1.0f
                linearLayoutManager.findViewByPosition(linearLayoutManager.findFirstVisibleItemPosition())!!.scaleX =
                        1.0f
                linearLayoutManager.findViewByPosition(linearLayoutManager.findLastVisibleItemPosition())!!.scaleY =
                        1.0f
                linearLayoutManager.findViewByPosition(linearLayoutManager.findLastVisibleItemPosition())!!.scaleX =
                        1.0f

                val contentView = snapHelper.findSnapView(linearLayoutManager)
                contentView!!.scaleX = 1.2f
                contentView!!.scaleY = 1.2f
            }
        }
        )

        dialog.findViewById<Button>(R.id.btnSelect).setOnClickListener {
            mListener.onSelect((rvSelection.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 1, type)
           /* if((rvSelection.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 1 == 1)
            {

            }*/


            dialog.dismiss()
        }

    }


    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        mListener.onCancel(type)
    }

    fun setMyListener(listener: ISelectListener) {
        mListener = listener
    }

    interface ISelectListener {
        fun onSelect(pos: Int, type: Int)
        fun onCancel(type: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.isDraggable = false
        return dialog
    }

}