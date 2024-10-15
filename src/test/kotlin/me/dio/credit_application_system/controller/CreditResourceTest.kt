package me.dio.credit_application_system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit_application_system.dto.CreditDto
import me.dio.credit_application_system.dto.CustomerDto
import me.dio.credit_application_system.dto.CustomerUpdateDto
import me.dio.credit_application_system.entity.Customer
import me.dio.credit_application_system.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/credits"
    }

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tarDown() = customerRepository.deleteAll()

    @Test
    fun `should create a credit and return 201 status`() {
        //given
        val creditDto: CreditDto = builderCreditDto()
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value("500.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.dayFirstInstallment").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").exists())
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should create `() {
    }
    /* criar um crédito

    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstOfInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)


    não pode ter parcela no passado e deve ser no máximo após 3 meses da data atual

    não pode ter mais de  48 parcelas


    */

    @Test
    fun `should not save a customer with same  CPF and return 409 status`() {
        //given
        customerRepository.save(builderCustomerDto().toEntity())
        val customerDto: CustomerDto = builderCustomerDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("409"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.dao.DataIntegrityViolationException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a customer with firstName empty and return 400 status`() {
        //given
        val customerDto: CustomerDto = builderCustomerDto(firstName = "")
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .content(valueAsString)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find customer by id and return 200 status`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Anthony"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Pablo"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("584.580.311-08"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("anthony@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("1000.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("00.000-000"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua Fulano de Tal"))
            //Funciona apenas individualmente
            //    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find customer with invalid id and return 400 status `() {
        //given
        val invalidId: Long = 2L
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/$invalidId")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit_application_system.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should delete customer by id and return 204 status`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        //when
        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
        //then
    }

    @Test
    fun `should not delete customer by id and return 400 status`() {
        //given
        val invalidId: Long = Random().nextLong()
        //when
        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL/$invalidId")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit_application_system.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
        //then
    }

    @Test
    fun `should update a customer and return 200 status`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val customerUpdateDto: CustomerUpdateDto = builderCustomerUpdateDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL?customerId=${customer.id}")
                .content(valueAsString)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Maria"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("da Silva"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("584.580.311-08"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("anthony@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("5000.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("01.001-001"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua Updated"))
            //funciona apenas individualmente
            // .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not update a customer with invalid id and return 400 status`() {
        //given
        val invalidId: Long = Random().nextLong()
        val customerUpdateDto: CustomerUpdateDto = builderCustomerUpdateDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL?customerId=$invalidId")
                .content(valueAsString)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit_application_system.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())

    }

    private fun builderCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(500.0),
        numberOfInstallments: Int = 3,
        dayFirstOfInstallment: LocalDate = LocalDate.of(2024,11,15),
        customerId: Long? = null
    ) = CreditDto(
        creditValue = creditValue,
        numberOfInstallments = numberOfInstallments,
        dayFirstOfInstallment = dayFirstOfInstallment,
        customerId = customerId
    )
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private fun builderCustomerUpdateDto(
        firstName: String = "Maria",
        lastName: String = "da Silva",
        income: BigDecimal = BigDecimal.valueOf(5000.00),
        zipCode: String = "01.001-001",
        street: String = "Rua Updated"
    ) = CustomerUpdateDto(
        firstName = firstName,
        lastName = lastName,
        income = income,
        zipCode = zipCode,
        street = street
    )
}
}