package com.guthierrez.minhaagua.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.guthierrez.minhaagua.constants.DBConstants
import com.guthierrez.minhaagua.model.Account
import com.guthierrez.minhaagua.model.Bill
import com.guthierrez.minhaagua.util.date
import com.guthierrez.minhaagua.util.reference

class AccountRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun listAccountBills(account: String, onSucess: (accountBills: List<Bill>) -> Unit) {
        firestore.collection(DBConstants.COLLECTIONS.BILLS)
                .whereEqualTo(Account.KEY.ACCOUNT, account)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val billList = querySnapshot.documents.map { document ->
                        val bill = Bill(
                                account,
                                document.getString(Bill.KEY.DATE)?.reference(),
                                document.getDouble(Bill.KEY.VALUE),
                                document.getLong(Bill.KEY.CONSUMPTION),
                                document.getBoolean(Bill.KEY.PAYED)
                        )
                        bill
                    }
                    billList.sortedBy { it.date }
                    onSucess(billList)
                }
    }

    fun getAccount(account: String, onSucess: (account: Account) -> Unit) {
        firestore.collection(DBConstants.COLLECTIONS.ACCOUNT)
                .document(account)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val accountData = Account(
                            account,
                            documentSnapshot.getString(Account.KEY.CPF),
                            documentSnapshot.getString(Account.KEY.BIRTH_DATE)?.date()
                    )
                    accountData.averageConsumption = documentSnapshot.getLong(Account.KEY.AVERAGE_CONSUMPTION)
                    accountData.minimumConsumption = documentSnapshot.getLong(Account.KEY.MINIMUM_CONSUMPTION)
                    accountData.maximumConsumption = documentSnapshot.getLong(Account.KEY.MAXIMUM_CONSUMPTION)
                    onSucess(accountData)
                }
    }

    fun userHasAccount(user: String, onSucess: (hasAccount: Boolean, account: String?) -> Unit) {
        firestore.collection(DBConstants.COLLECTIONS.USER_ACCOUNT)
                .whereEqualTo(Account.KEY.USER, user)
                .get()
                .addOnSuccessListener { querySnapShot ->
                    val hasAccount = querySnapShot.documents.isNotEmpty()
                    if (hasAccount) {
                        onSucess(true, (querySnapShot.documents[0]?.get(Account.KEY.ACCOUNT) as String?))
                    } else {
                        onSucess(false, null)
                    }
                }
    }

    fun accountExists(account: String, cpf: String, birthDate: String, onSucess: (accountExists: Boolean) -> Unit) {
        firestore.collection(DBConstants.COLLECTIONS.ACCOUNT)
                .document(account)
                .get()
                .addOnSuccessListener {
                    if (it.exists() && cpf == it.get(Account.KEY.CPF) && birthDate == it.get(Account.KEY.BIRTH_DATE)) {
                        onSucess(true)
                    } else {
                        onSucess(false)
                    }
                }
    }

    fun insertAccountToUser(user: String, account: String, onSucess: () -> Unit) {
        val userAccount = hashMapOf(
                Account.KEY.USER to user,
                Account.KEY.ACCOUNT to account
        )
        firestore.collection(DBConstants.COLLECTIONS.USER_ACCOUNT)
                .add(userAccount)
                .addOnSuccessListener { onSucess() }
    }
}