package com.guthierrez.minhaagua.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.model.Bill
import com.guthierrez.minhaagua.ui.viewholder.AccountBillsViewHolder

class AccountBillsAdapter(
        private val accountBills: List<Bill>
) : RecyclerView.Adapter<AccountBillsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountBillsViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.account_bill_item, parent, false)
        return AccountBillsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return accountBills.count()
    }

    override fun onBindViewHolder(holder: AccountBillsViewHolder, position: Int) {
        val bill = accountBills[position]
        holder.bindData(bill)
    }

}