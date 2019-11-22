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
import com.guthierrez.minhaagua.util.SecurityPreferences
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.editPassword
import org.apache.commons.lang3.StringUtils

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var securityPreferences: SecurityPreferences
    private lateinit var authenticationManager: FirebaseAuthenticationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Fields
        securityPreferences = SecurityPreferences(this)
        authenticationManager = FirebaseAuthenticationManager.getInstance()

        //Eventos
        setListerners()
        verifyLoggedUser()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonLogin -> handleLogin()
            R.id.textRegister -> openRegisterActivity()
        }
    }

    private fun setListerners() {
        buttonLogin.setOnClickListener(this)
        textRegister.setOnClickListener(this)
    }

    private fun handleLogin() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            Toast.makeText(this, "Informe seu email e senha.", Toast.LENGTH_LONG).show()
            return
        }
        authenticationManager.login(email, password) {
            if (it) {
                securityPreferences.storeString(AppConstants.KEY.USER_ID, authenticationManager.getUserId())
                securityPreferences.storeString(AppConstants.KEY.USER_NAME, authenticationManager.getUserName())
                securityPreferences.storeString(AppConstants.KEY.USER_EMAIL, authenticationManager.getUserEmail())
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Dados de login inválidos", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun verifyLoggedUser() {
        if (securityPreferences.getStoredString(AppConstants.KEY.USER_ID) != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun openRegisterActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
