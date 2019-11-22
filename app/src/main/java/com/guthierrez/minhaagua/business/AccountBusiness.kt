package com.guthierrez.minhaagua.business

import com.guthierrez.minhaagua.model.Account
import com.guthierrez.minhaagua.model.Bill
import com.guthierrez.minhaagua.repository.AccountRepository

class AccountBusiness {

    private val accountRepository = AccountRepository()

    fun listAccountBills(account: String, onSucess: (accountBills: List<Bill>) -> Unit) {
        accountRepository.listAccountBills(account, onSucess)
    }
    fun getAccount(account: String, onSucess: (account: Account) -> Unit) {
        accountRepository.getAccount(account, onSucess)
    }

    fun userHasAccount(user: String, onSucess: (hasAccount: Boolean, account: String?) -> Unit) {
       accountRepository.userHasAccount(user, onSucess)
    }

    fun accountExists(account: String, cpf: String, birthDate: String, onSucess: (accountExists: Boolean) -> Unit) {
        accountRepository.accountExists(account, cpf, birthDate, onSucess)
    }

    fun insertAccountToUser(user: String, account: String, onSucess: () -> Unit) {
        accountRepository.insertAccountToUser(user, account, onSucess)
    }
}