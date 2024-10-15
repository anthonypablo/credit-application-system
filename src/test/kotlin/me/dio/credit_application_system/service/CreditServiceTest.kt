package me.dio.credit_application_system.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import me.dio.credit_application_system.dto.CreditDto
import me.dio.credit_application_system.entity.Address
import me.dio.credit_application_system.entity.Credit
import me.dio.credit_application_system.entity.Customer
import me.dio.credit_application_system.exception.BusinessException
import me.dio.credit_application_system.repository.CreditRepository
import me.dio.credit_application_system.service.impl.CreditService
import me.dio.credit_application_system.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockKExtension::class)
class CreditServiceTest {

    @MockK
    lateinit var creditRepository: CreditRepository

    @InjectMockKs
    lateinit var creditService: CreditService

    @Test
    fun `should create credit`() {
        //given
        val fakeCredit: Credit = buildCredit()
        every { creditRepository.save(any()) } returns fakeCredit
        //when
        val actual: Credit = creditService.save(fakeCredit)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }

    private fun buildCredit(
        id: Long? = null,
        creditValue: BigDecimal = BigDecimal.valueOf(500.0),
        numberOfInstallments: Int = 3,
        dayFirstOfInstallment: LocalDate = LocalDate.of(2024, 11, 15),
        customerId: Long = 1
    ) = Credit(
        id = id,
        creditValue = creditValue,
        numberOfInstallments = numberOfInstallments,
        dayFirstOfInstallment = dayFirstOfInstallment,
        customerId = customerId
    )


    @Test
    fun `should find credit by id`() {
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCredit: Credit = buildCredit(id = fakeId)
        every { creditRepository.findById(fakeId) } returns Optional.of(fakeCredit)
        //when
        val actual: Credit? = creditService.findById(fakeId)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isExactlyInstanceOf(Credit::class.java)
        Assertions.assertThat(actual).isEqualTo(fakeCredit)
        verify(exactly = 1) { creditRepository.findById(fakeId) }
    }
}