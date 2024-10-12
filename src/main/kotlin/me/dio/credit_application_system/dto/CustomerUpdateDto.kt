package me.dio.credit_application_system.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit_application_system.entity.Customer
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "O campo não pode ser deixado em branco") val firstName: String,
    @field:NotEmpty(message = "O campo não pode ser deixado em branco") val lastName: String,
    @field:NotNull(message = "Valor inválido") val income: BigDecimal,
    @field:NotEmpty(message = "O campo não pode ser deixado em branco") val zipCode: String,
    @field:NotEmpty(message = "O campo não pode ser deixado em branco") val street: String
) {
    fun toEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street
        return customer
    }
}
