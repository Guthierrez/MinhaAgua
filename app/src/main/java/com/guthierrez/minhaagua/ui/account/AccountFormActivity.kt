package com.guthierrez.minhaagua.ui.account

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.guthierrez.minhaagua.MainActivity
import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.business.AccountBusiness
import com.guthierrez.minhaagua.constants.AppConstants
import com.guthierrez.minhaagua.util.SecurityPreferences
import com.guthierrez.minhaagua.util.timestamp
import kotlinx.android.synthetic.main.activity_account_form.*
import org.apache.commons.lang3.StringUtils
import java.util.*

class AccountFormActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private lateinit var accountBusiness: AccountBusiness
    private lateinit var sharedPreferences: SecurityPreferences

    private var birthDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_form)

        accountBusiness = AccountBusiness()
        sharedPreferences = SecurityPreferences(this)

        setListeners()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonDate -> openDatePicker()
            R.id.buttonSave -> handleSave()
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        birthDate = calendar.time
        buttonDate.text = birthDate!!.timestamp()
    }

    private fun setListeners() {
        buttonDate.setOnClickListener(this)
        buttonSave.setOnClickListener(this)
    }

    private fun openDatePicker() {
        DatePickerDialog(this, this, 2019, 11, 19).show()
    }

    private fun handleSave() {
        val account = editAccount.text.toString()
        val cpf = editCpf.text.toString()
        val date = birthDate?.timestamp()
        if (StringUtils.isBlank(account) || StringUtils.isBlank(cpf) || date == null) {
            Toast.makeText(this, "Informe todos os dados.", Toast.LENGTH_LONG).show()
            return
        }
        accountBusiness.accountExists(account, cpf, date) {
            if (it) {
                val user = sharedPreferences.getStoredString(AppConstants.KEY.USER_ID)
                accountBusiness.insertAccountToUser(user!!, account) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(AppConstants.KEY.ACCOUNT, account)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Conta não encontrada para os dados informados.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
