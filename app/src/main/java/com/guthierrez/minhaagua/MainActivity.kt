package com.guthierrez.minhaagua

import android.content.Intent
import android.os.Bundle

import com.google.android.material.navigation.NavigationView

import androidx.drawerlayout.widget.DrawerLayout

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.guthierrez.minhaagua.auth.FirebaseAuthenticationManager
import com.guthierrez.minhaagua.business.AccountBusiness
import com.guthierrez.minhaagua.business.UserBusiness
import com.guthierrez.minhaagua.constants.AppConstants
import com.guthierrez.minhaagua.ui.account.AccountBillsFragment
import com.guthierrez.minhaagua.ui.account.AccountFormActivity
import com.guthierrez.minhaagua.ui.registration.LoginActivity
import com.guthierrez.minhaagua.ui.leaks.LeakListFragment
import com.guthierrez.minhaagua.util.SecurityPreferences
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var sharedPreferences: SecurityPreferences
    private lateinit var userBusiness: UserBusiness
    private lateinit var accountBusiness: AccountBusiness
    private lateinit var authenticationManager: FirebaseAuthenticationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toogle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawer.addDrawerListener(toogle)
        toogle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        sharedPreferences = SecurityPreferences(this)
        userBusiness = UserBusiness(this)
        accountBusiness = AccountBusiness()
        authenticationManager = FirebaseAuthenticationManager.getInstance()


        if (intent.hasExtra(AppConstants.KEY.ACCOUNT)) {
            val account = intent.getStringExtra(AppConstants.KEY.ACCOUNT)
            startAccountBillsFragment(account)
        } else {
            startListFragment(AppConstants.LEAKFILTER.COMPLETE)
        }
        formatUserName()
        formatDate()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_done -> {
                startListFragment(AppConstants.LEAKFILTER.COMPLETE)
            }
            R.id.nav_todo -> {
                accountBusiness.userHasAccount(sharedPreferences.getStoredString(AppConstants.KEY.USER_ID)!!) { hasAccount, account ->
                    if (hasAccount) {
                        startAccountBillsFragment(account)
                    } else {
                        startActivity(Intent(this, AccountFormActivity::class.java))
                    }
                }
            }
            R.id.nav_logout -> {
                handleLogout()
            }
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun startAccountBillsFragment(account: String?) {
        val fragment = AccountBillsFragment.newInstance(account!!)
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frameContent, fragment).commit()
    }

    private fun startListFragment(taskFilter: Int) {
        val fragment = LeakListFragment.newInstance(taskFilter)
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frameContent, fragment).commit()
    }

    private fun handleLogout() {
        authenticationManager.logOut()

        sharedPreferences.removedStoredString(AppConstants.KEY.USER_ID)
        sharedPreferences.removedStoredString(AppConstants.KEY.USER_NAME)
        sharedPreferences.removedStoredString(AppConstants.KEY.USER_EMAIL)

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun formatUserName() {
        val name = sharedPreferences.getStoredString(AppConstants.KEY.USER_NAME)
        textHello.text = "Olá $name!"

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val navHeaderView = navigationView.getHeaderView(0)
        val textName = navHeaderView.findViewById<TextView>(R.id.textName)
        val textEmail = navHeaderView.findViewById<TextView>(R.id.textEmail)

        textName.text = sharedPreferences.getStoredString(AppConstants.KEY.USER_NAME)
        textEmail.text = sharedPreferences.getStoredString(AppConstants.KEY.USER_EMAIL)
    }

    private fun formatDate() {
        val calendar = Calendar.getInstance()

        val days = arrayOf("Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado")
        val months = arrayOf("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novemembro", "Dezembro")

        val date = "${days[calendar.get(Calendar.DAY_OF_WEEK) - 1]}, ${calendar.get(Calendar.DAY_OF_MONTH)} de ${months[calendar.get(Calendar.MONTH)]}"
        textDate.text = date
    }

}
