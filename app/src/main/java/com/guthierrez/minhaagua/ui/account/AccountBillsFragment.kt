package com.guthierrez.minhaagua.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.business.AccountBusiness
import com.guthierrez.minhaagua.constants.AppConstants
import com.guthierrez.minhaagua.ui.adapter.AccountBillsAdapter
import kotlinx.android.synthetic.main.fragment_account_bills.*

class AccountBillsFragment : Fragment() {

    private lateinit var account: String
    private lateinit var accountBusiness: AccountBusiness
    private lateinit var recyclerBills: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_account_bills, container, false)
        accountBusiness = AccountBusiness()
        recyclerBills = rootView.findViewById(R.id.recyclerBills)
        recyclerBills.layoutManager = LinearLayoutManager(context)
        loadAccountBills()
        return rootView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            account = it.getString(AppConstants.KEY.ACCOUNT)!!
        }
    }

    private fun loadAccountBills() {
        accountBusiness.listAccountBills(account) { billList ->
            recyclerBills.adapter = AccountBillsAdapter(billList)
        }
        accountBusiness.getAccount(account) { account ->
            textMinimumConsumption.text = "Mínimo: ${account.minimumConsumption}"
            textAverageConsumption.text = "Médio: ${account.averageConsumption}"
            textMaximumConsumption.text = "Máximo: ${account.maximumConsumption}"
            textAccount.text = "Conta: ${account.number}"
        }
    }

    companion object {
        fun newInstance(account: String): AccountBillsFragment {
            val args = Bundle()
            args.putString(AppConstants.KEY.ACCOUNT, account)
            val fragment = AccountBillsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
