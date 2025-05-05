package com.example.becycle.items

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.becycle.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UploadBottomSheet : BottomSheetDialogFragment() {

    private lateinit var initialLayout: View
    private lateinit var addCreateLayout: View
    private lateinit var fileSelectedLayout: View
    private lateinit var selectedFileName: TextView

    private var fileUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.upload_bottom_sheet, container, false)

        // Find views
        initialLayout = view.findViewById(R.id.initialLayout)
        addCreateLayout = view.findViewById(R.id.addCreateLayout)
        fileSelectedLayout = view.findViewById(R.id.fileSelectedLayout)
        selectedFileName = view.findViewById(R.id.selectedFileName)
        val btnSubmitFinal = view.findViewById<View>(R.id.btnSubmitFinal)

        view.findViewById<View>(R.id.close_button).setOnClickListener {
            dismiss()
        }

        view.findViewById<View>(R.id.btnAddOrCreate).setOnClickListener {
            switchToAddCreate()
        }

        view.findViewById<View>(R.id.btnUploadFile).setOnClickListener {
            pickFromGallery()
        }

        view.findViewById<View>(R.id.btnSubmit).setOnClickListener {
            // Handle initial submit (if needed)
        }

        view.findViewById<View>(R.id.btnAddAnother).setOnClickListener {
            switchToAddCreate()
        }

        btnSubmitFinal.setOnClickListener {
            // Handle final submit
            dismiss()
        }

        return view
    }


    private fun switchToAddCreate() {
        initialLayout.visibility = View.GONE
        fileSelectedLayout.visibility = View.GONE
        addCreateLayout.visibility = View.VISIBLE
    }

    private fun switchToFileSelected(uri: Uri) {
        fileUri = uri
        initialLayout.visibility = View.GONE
        addCreateLayout.visibility = View.GONE
        fileSelectedLayout.visibility = View.VISIBLE
        selectedFileName.text = uri.lastPathSegment
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                switchToFileSelected(it)
            }
        }
    }
}
