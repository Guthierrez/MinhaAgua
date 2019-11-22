package com.guthierrez.minhaagua.ui.leaks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.business.LeakBusiness
import com.guthierrez.minhaagua.constants.AppConstants
import com.guthierrez.minhaagua.listeners.LeakListEventListener
import com.guthierrez.minhaagua.model.Leak
import com.guthierrez.minhaagua.ui.adapter.LeakListAdapter
import com.guthierrez.minhaagua.ui.registration.LoginActivity
import com.guthierrez.minhaagua.util.SecurityPreferences
import java.lang.Exception

class LeakListFragment : Fragment(), View.OnClickListener {

    private val defaultOnError: (ex: Exception) -> Unit = {
        showFailure(it)
    }

    private lateinit var leakBusiness: LeakBusiness
    private lateinit var securityPreferences: SecurityPreferences
    private lateinit var recyclerLeakList: RecyclerView
    private lateinit var leakListEventListener: LeakListEventListener

    private var leakFilter: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        leakBusiness = LeakBusiness()
        securityPreferences = SecurityPreferences(context!!)
        leakListEventListener = object : LeakListEventListener {
            override fun onLeakSelection(leak: Leak) {
                val intent = Intent(context, LeakFlowActivity::class.java)
                intent.putExtra(AppConstants.KEY.LEAK, leak)
                startActivity(intent)
            }
        }

        val rootView = inflater.inflate(R.layout.fragment_leak_list, container, false)
        val floatButton = rootView.findViewById<FloatingActionButton>(R.id.floatAddLeak)
        floatButton.setOnClickListener(this)

        recyclerLeakList = rootView.findViewById(R.id.recyclerLeakList)
        recyclerLeakList.layoutManager = LinearLayoutManager(context)

        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            leakFilter = arguments!!.getInt(AppConstants.LEAKFILTER.KEY)
        }
    }

    override fun onResume() {
        super.onResume()
        loadLeaks()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.floatAddLeak -> startActivity(Intent(context, LeakFormActivity::class.java))
        }
    }

    private fun loadLeaks() {
        val userId = securityPreferences.getStoredString(AppConstants.KEY.USER_ID)!!
        leakBusiness.listLeaks(userId, defaultOnError) {
            recyclerLeakList.adapter = LeakListAdapter(it, leakListEventListener)
        }
    }

    private fun showFailure(ex: Exception) {
        Toast.makeText(context, "Ocorreu um erro inesperado.", Toast.LENGTH_LONG).show()
        Log.e(null, "Erro ao listar vazamentos.", ex)
    }

    companion object {
        fun newInstance(leakFilter: Int): LeakListFragment {
            val args = Bundle()
            args.putInt(AppConstants.LEAKFILTER.KEY, leakFilter)
            val fragment = LeakListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
