package com.project.lidor_amraby

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import android.widget.EditText
import android.widget.TextView

class PhoneNumberDialog : DialogFragment() {

    interface PhoneNumberDialogListener {
        fun onPhoneNumberEntered(phoneNumber: String)
    }

    private lateinit var phoneNumberEditText : EditText

    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {
        // Create a new AlertDialog.Builder with the activity's context
        val builder = AlertDialog.Builder(requireActivity())
        // Get the layout inflater
        val inflater = requireActivity().layoutInflater
        // Inflate the layout for the dialog
        val dialogView = inflater.inflate(R.layout.dialog_phone_number, null)

        phoneNumberEditText = dialogView.findViewById(R.id.phone_number_edittext)

        builder.setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                // When OK is clicked, get the entered phone number from the EditText
                val phoneNumber = phoneNumberEditText.text.toString()
                // Call the onPhoneNumberEntered method of the listener interface with the entered phone number
                (activity as? PhoneNumberDialogListener)?.onPhoneNumberEntered(phoneNumber)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                // When Cancel is clicked, simply cancel the dialog
                dialog.cancel()
            }

        return builder.create()
    }
}