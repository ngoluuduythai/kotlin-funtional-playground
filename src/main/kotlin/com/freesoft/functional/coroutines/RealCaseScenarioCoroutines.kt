package com.freesoft.functional.coroutines

import com.github.salomonbrys.kotson.*
import com.google.gson.GsonBuilder
import org.h2.jdbcx.JdbcDataSource
import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import java.util.*

enum class Gender {
    MALE, FEMALE;

    companion object {
        fun valueOfIgnoreCase(name: String): Gender = valueOf(name.toUpperCase())
    }
}

typealias UserID = Int

data class User(val id: UserID, val firstName: String, val lastName: String, val gender: Gender)

data class Fact(val id: Int, val value: String, val user: User? = null)

interface UserService {
    fun getFact(id: UserID): Fact
}

/**
 * HTTP Clients
 */
interface UserClient {
    fun getUser(id: UserID): User
}

interface FactClient {
    fun getFact(user: User): Fact
}

abstract class WebClient {
    protected val apacheClient = ApacheClient()

    protected val gson = GsonBuilder().registerTypeAdapter<User> {
        deserialize { des ->
            val json = des.json
            User(json["info"]["seed"].int,
                    json["results"][0]["name"]["first"].string.capitalize(),
                    json["results"][0]["name"]["last"].string.capitalize(),
                    Gender.valueOfIgnoreCase(json["results"][0]["gender"].string))
        }
    }.registerTypeAdapter<Fact> {
        deserialize { des ->
            val json = des.json
            Fact(json["value"]["id"].int,
                    json["value"]["joke"].string)
        }
    }.create()
}

class Http4KUserClient : WebClient(), UserClient {
    override fun getUser(id: UserID): User {
        return gson.fromJson(apacheClient(Request(Method.GET, "https://randomuser.me/api")
                .query("seed", id.toString()))
                .bodyString())
    }
}

class Http4KFactClient : WebClient(), FactClient {
    override fun getFact(user: User): Fact {
        return gson.fromJson<Fact>(apacheClient(Request(Method.GET, "http://api.icndb.com/jokes/random")
                .query("firstName", user.firstName)
                .query("lastName", user.lastName))
                .bodyString()).copy(user = user)
    }

}

class MockUserClient : UserClient {
    override fun getUser(id: UserID): User {
        println("MockUserClient.getUser")
        Thread.sleep(500)
        return User(id, "Foo", "Bar", Gender.FEMALE)
    }
}

class MockFactClient : FactClient {
    override fun getFact(user: User): Fact {
        println("MockFactClient.getFact")
        Thread.sleep(500)
        return Fact(Random().nextInt(), "FACT ${user.firstName}, ${user.lastName}", user)
    }
}

interface UserRepository {
    fun getUserById(id: UserID): User?
    fun insertUser(user: User)
}

interface FactRepository {
    fun getFactByUserID(userID: UserID): Fact?
    fun insertFact(fact: Fact)
}

abstract class JdbcRepository(protected val template: JdbcTemplate) {
    protected fun <T> toNullable(block: () -> T): T? {
        return try {
            block()
        } catch (_: EmptyResultDataAccessException) {
            null
        }
    }
}

class JdbcUserRepository(template: JdbcTemplate) : JdbcRepository(template), UserRepository {
    override fun getUserById(id: UserID): User? {
        return toNullable {
            template.queryForObject("select * from USERS where id=?", id) { resultSet, _ ->
                with(resultSet) {
                    User(getInt("ID"),
                            getString("FIRST_NAME"),
                            getString("LAST_NAME"),
                            Gender.valueOfIgnoreCase(getString("GENDER")))
                }
            }
        }
    }

    override fun insertUser(user: User) {
        template.update("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.id,
                user.firstName,
                user.lastName,
                user.gender.name)
    }

}

class JdbcFactRepository(template: JdbcTemplate) : JdbcRepository(template), FactRepository {
    override fun getFactByUserID(userID: UserID): Fact? {
        return toNullable {
            template.queryForObject("select * from USERS as U inner join FACTS as F on U.ID = F.USER where U.ID =?", userID)
            { resultSet, _ ->
                with(resultSet) {
                    Fact(getInt(5),
                            getString(6),
                            User(getInt(1),
                                    getString(2),
                                    getString(3),
                                    Gender.valueOfIgnoreCase(getString(4))))
                }
            }
        }
    }

    override fun insertFact(fact: Fact) {
        template.update("INSERT INTO FACTS VALUES (?,?,?)", fact.id, fact.value, fact.user?.id)
    }
}

fun initJdbcTemplate(): JdbcTemplate {
    return JdbcTemplate(JdbcDataSource()
            .apply { setUrl("jdbc:h2:mem:facts_app;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false") })
            .apply {
                execute("CREATE TABLE USERS (ID INT AUTO_INCREMENT PRIMARY KEY, FIRST_NAME VARCHAR(64) NOT NULL, LAST_NAME VARCHAR(64) NOT NULL, GENDER VARCHAR(8) NOT NULL);")
                execute("CREATE TABLE FACTS (ID INT AUTO_INCREMENT PRIMARY KEY, VALUE_ TEXT NOT NULL, USER INT NOT NULL,  FOREIGN KEY (USER) REFERENCES USERS(ID) ON DELETE RESTRICT)")
            }
}

inline fun <T> T.apply(block: T.() -> Unit): T {
    block()
    return this
}

class MockUserRepository : UserRepository {
    private val users = hashMapOf<UserID, User>()

    override fun getUserById(id: UserID): User? {
        println("MockUserRepository.getUserById")
        Thread.sleep(200)
        return users[id]
    }

    override fun insertUser(user: User) {
        println("MockUserRepository.insertUser")
        Thread.sleep(200)
        users[user.id] = user
    }
}

class MockFactRepository : FactRepository {
    private val facts = hashMapOf<UserID, Fact>()

    override fun getFactByUserID(userID: UserID): Fact? {
        println("MockFactRepository.getFactByUserId")
        Thread.sleep(200)
        return facts[userID]
    }

    override fun insertFact(fact: Fact) {
        println("MockFactRepository.insertFact")
        Thread.sleep(200)
        facts[fact.user?.id ?: 0] = fact
    }
}

class SynchronousUserService(
        private val userClient: UserClient,
        private val factClient: FactClient,
        private val userRepository: UserRepository,
        private val factRepository: FactRepository) : UserService {
    override fun getFact(id: UserID): Fact {
        val user = userRepository.getUserById(id)
        return if (user == null) {
            val userFromService = userClient.getUser(id)
            userRepository.insertUser(userFromService)
            getFact(userFromService)
        } else {
            factRepository.getFactByUserID(id) ?: getFact(user)
        }
    }

    private fun getFact(user: User): Fact {
        val fact = factClient.getFact(user)
        factRepository.insertFact(fact)
        return fact
    }
}

inline fun <T> inTime(body: () -> T): Pair<T, Long> {
    val startTime = System.nanoTime()
    val v = body()
    val endTime = System.nanoTime()
    return v to endTime - startTime
}


