package com.roshka.formregister

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly

class MainActivity : AppCompatActivity() {
    private lateinit var editTextBirthdate: EditText
    private lateinit var spinnerSex: Spinner
    private lateinit var spinnerCivil: Spinner
    private lateinit var editTextPhone: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextRuc: EditText

    private var person = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSpinnerDesign()

        /*Get views*/
        editTextBirthdate = findViewById(R.id.editText_birthdate)
        editTextBirthdate.setOnClickListener { showDatePickerDialog() }

        editTextPhone = findViewById(R.id.edit_text_phone)
        editTextEmail = findViewById(R.id.edit_text_email)
        editTextRuc = findViewById(R.id.edit_text_ruc)

    }

    private fun setSpinnerDesign() {
        // Declarar e inicializar el spinner del activity
        spinnerSex = findViewById<Spinner>(R.id.spinner_sex)
        spinnerCivil = findViewById<Spinner>(R.id.spinner_civil)

        // Crear adaptador
        val sArrayAdapter =
            ArrayAdapter<Any?>(this, R.layout.spinner_list, resources.getStringArray(R.array.sex))
        val cArrayAdapter =
            ArrayAdapter<Any?>(this, R.layout.spinner_list, resources.getStringArray(R.array.civil))

        sArrayAdapter.setDropDownViewResource(R.layout.spinner_list)
        cArrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        // Setear el adaptador al spinner
        spinnerSex.adapter = sArrayAdapter
        spinnerCivil.adapter = cArrayAdapter
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val dateText = "$day/$month/$year"
        editTextBirthdate.setText(dateText)
    }

    fun register(view: View) {
        if (view is Button) {
            if (validateFields()){
                person["birthdate"] = editTextBirthdate.text.toString()
                person["sex"] = spinnerSex.selectedItem.toString()
                person["maritalStatus"] = spinnerCivil.selectedItem.toString()
                person["phone"] = editTextPhone.text.toString()
                person["email"] = editTextEmail.text.toString()
                person["ruc"] = editTextRuc.text.toString()

                // Mostrar datos
                showAlert()
            }
        } else
            return
    }

    private fun validateFields(): Boolean {
        val message = "Campo requerido"
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val rucPattern = "[0-9]+-[0-9]"
        var flag = false

        /* Campos vacios */
        if (editTextBirthdate.text.trim().isEmpty()) {
            editTextBirthdate.error = message
            editTextBirthdate.requestFocus()
        } else if (editTextPhone.text.trim().isEmpty()) {
            editTextPhone.error = message
            editTextPhone.requestFocus()
        } else if (editTextEmail.text.trim().isEmpty()) {
            editTextEmail.error = message
            editTextEmail.requestFocus()
        } else if (editTextRuc.text.trim().isEmpty()) {
            editTextRuc.error = message
            editTextRuc.requestFocus()
        } else if (!editTextPhone.text.trim().substring(0, 2)
                .contains("09") || !editTextPhone.text.trim().isDigitsOnly()
        ) {
            /* Validacion teléfono comienzo 09*/
            editTextPhone.error = "Número de teléfono inválido"
            editTextPhone.requestFocus()
            return false
        } else if (!editTextEmail.text.trim().matches(emailPattern.toRegex())) {
            // Validacion email
            editTextEmail.error = "Correo electrónico inválido"
            editTextEmail.requestFocus()
        } else if (!editTextRuc.text.trim().matches(rucPattern.toRegex())) {
            //Validacion RUC
            editTextRuc.error = "RUC inválido (222222-3)"
            editTextRuc.requestFocus()
        } else {
            flag = true
        }

        return flag
    }

    private fun showAlert() {
        val message = "\nFecha Nacimiento: ${person["birthdate"]}" +
                "\n\nSexo: ${person["sex"]} " +
                "\n\nEstado Civil: ${person["maritalStatus"]} " +
                "\n\nTeléfono: ${person["phone"]} " +
                "\n\nCorreo electrónico: ${person["email"]} " +
                "\n\nRUC: ${person["ruc"]} "

        AlertDialog.Builder(this)
            .setTitle("Person Data")
            .setMessage(message)
            .setPositiveButton("Clear")
            { _, _ ->
                clearForm()
            }
            .show()
    }

    private fun clearForm(){
        editTextBirthdate.setText("")
        editTextPhone.setText("")
        editTextEmail.setText("")
        editTextRuc.setText("")
        spinnerSex.setSelection(0)
        spinnerCivil.setSelection(0)
    }
}