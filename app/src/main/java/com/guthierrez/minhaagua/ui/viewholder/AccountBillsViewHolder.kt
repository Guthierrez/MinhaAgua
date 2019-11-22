package com.guthierrez.minhaagua.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.model.Bill
import com.guthierrez.minhaagua.util.reference

class AccountBillsViewHolder(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val billItemImage: ImageView = itemView.findViewById(R.id.billItemImage)
    private val textBillReference: TextView = itemView.findViewById(R.id.textBillReference)
    private val textBillValue: TextView = itemView.findViewById(R.id.textBillValue)
    private val textBillConsumption: TextView = itemView.findViewById(R.id.textBillConsumption)

    fun bindData(bill: Bill) {
        billItemImage.setImageResource(if (bill.payed != null && bill.payed!!) R.drawable.ic_done else R.drawable.ic_error )
        textBillReference.text = bill.date?.reference()
        textBillValue.text = "R$ " + bill.value?.toString()
        textBillConsumption.text = "Consumo: ${bill.consumption}"
    }
}