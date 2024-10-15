package me.dio.credit_application_system.entity

import jakarta.persistence.*
import org.hibernate.engine.internal.Cascade

@Entity
//@Table(name = "Cliente")
data class Customer(
    var firstname: String = "",
    @Column(nullable = false) var lastname: String = "",
    @Column(nullable = false, unique = true) val cpf: String,
    @Column(nullable = false, unique = true) var email: String = "",
    @Column(nullable = false) val password: String = "",
    @Column(nullable = false) @Embedded var adress: Address = Address(),
    @Column(nullable = false) @OneToMany(fetch = FetchType.LAZY,
        cascade = arrayOf(CascadeType.REMOVE, CascadeType.PERSIST),
        mappedBy = "customer")
        var credits: List<Credit> = mutableListOf(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
)