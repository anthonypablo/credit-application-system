package me.dio.credit_application_system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit_application_system.dto.CreditDto
import me.dio.credit_application_system.entity.Address
import me.dio.credit_application_system.entity.Customer
import me.dio.credit_application_system.repository.CustomerRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
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

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository
    @Autowired
    lateinit var testEntityManager: TestEntityManager
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var customer: Customer

    companion object {
        const val URL: String = "/api/credits"
    }

    @BeforeEach
    fun setup() {
        customer = testEntityManager.persist(buildCustomer())
    }

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


    private fun builderCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(500.0),
        numberOfInstallments: Int = 3,
        dayFirstOfInstallment: LocalDate = LocalDate.of(2024, 11, 15),
        customerId: Long = 1
    ) = CreditDto(
        creditValue = creditValue,
        numberOfInstallments = numberOfInstallments,
        dayFirstOfInstallment = dayFirstOfInstallment,
        customerId = customerId
    )

    private fun buildCustomer(
        firstName: String = "Anthony",
        lastName: String = "Pablo",
        cpf: String = "584.580.311-08",
        email: String = "anthony@email.com",
        password: String = "123456",
        zipCode: String = "00.000-000",
        street: String = "Rua Fulano de Tal",
        income: BigDecimal = BigDecimal.valueOf(1000.00),
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street
        ),
        income = income,
    )
}
