package com.guthierrez.minhaagua.ui.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.guthierrez.minhaagua.MainActivity
import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.auth.FirebaseAuthenticationManager
import com.guthierrez.minhaagua.business.UserBusiness
import com.guthierrez.minhaagua.constants.AppConstants
import com.guthierrez.minhaagua.exception.ValidationException
import com.guthierrez.minhaagua.util.SecurityPreferences
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var userBusiness: UserBusiness
    private lateinit var securityPreferences: SecurityPreferences
    private lateinit var authenticationManager: FirebaseAuthenticationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Fields
        userBusiness = UserBusiness(this)
        securityPreferences = SecurityPreferences(this)
        authenticationManager = FirebaseAuthenticationManager.getInstance()

        //Eventos
        setListerners()
        verifyLoggedUser()
    }

    override fun onClick(view: View) {
        when (view.id) {
           R.id.buttonSave -> handleSave()
        }
    }

    private fun setListerners() {
        buttonSave.setOnClickListener(this)
    }

    private fun handleSave() {
        try {
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            userBusiness.insert(name, email, password)  {
                if (it) {
                    securityPreferences.storeString(AppConstants.KEY.USER_ID, authenticationManager.getUserId())
                    securityPreferences.storeString(AppConstants.KEY.USER_NAME, authenticationManager.getUserName())
                    securityPreferences.storeString(AppConstants.KEY.USER_EMAIL, authenticationManager.getUserEmail())
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Ocorreu um erro inesperado.", Toast.LENGTH_LONG).show()
                }
            }
        } catch (validationException: ValidationException) {
            Toast.makeText(this, validationException.message, Toast.LENGTH_LONG).show()
        } catch (exception: Exception) {
            Toast.makeText(this, "Ocorreu um erro inesperado.", Toast.LENGTH_LONG).show()
        }
    }

    private fun verifyLoggedUser() {
        if(securityPreferences.getStoredString(AppConstants.KEY.USER_ID) != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
