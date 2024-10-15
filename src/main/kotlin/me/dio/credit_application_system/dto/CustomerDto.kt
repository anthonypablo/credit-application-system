package me.dio.credit_application_system.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit_application_system.entity.Address
import me.dio.credit_application_system.entity.Customer
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto (
    @field:NotEmpty(message = "O campo não pode ser deixado em branco") val firstName: String,
    @field:NotEmpty(message = "O campo não pode ser deixado em branco") val lastName: String,
    @field:NotEmpty(message = "O campo não pode ser deixado em branco")@field:CPF (message = "CPF inválido") val cpf: String,
    @field:NotNull(message = "Valor inválido") val income: BigDecimal,
    @field:NotEmpty(message = "O campo não pode ser deixado em branco")@field:Email(message = "e-mail inválido") val email: String,
    @field:NotEmpty(message = "O campo não pode ser deixado em branco") val password: String,
    @field:NotEmpty(message = "O campo não pode ser deixado em branco") val zipCode: String,
    @field:NotEmpty(message = "O campo não pode ser deixado em branco") val street: String
){
    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
    )
}
