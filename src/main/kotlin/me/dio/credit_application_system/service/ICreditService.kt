package me.dio.credit_application_system.service

import me.dio.credit_application_system.entity.Credit
import java.util.*

interface ICreditService {
    fun save(credit: Credit): Credit
    fun findAllByCustomer(CustomerId: Long): List<Credit>
    fun findByCreditCode(CustomerId: Long, creditCode: UUID): Credit
}