package com.freesoft.functional.coroutines

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FutureUserService(
        private val userClient: UserClient,
        private val factClient: FactClient,
        private val userRepository: UserRepository,
        private val factRepository: FactRepository) : UserService {

    private fun getFact(user: User, executor: ExecutorService): Fact {
        val fact = executor.submit<Fact> {
            factClient.getFact(user)
        }.get()
        return fact
    }

    override fun getFact(id: UserID): Fact {
        val executor = Executors.newFixedThreadPool(2)
        val user = executor.submit<User?> { userRepository.getUserById(id) }.get()

        return if (user == null) {
            val userFromService = executor.submit<User> { userClient.getUser(id) }.get()
            executor.submit {
                userRepository.insertUser(userFromService)
            }
            getFact(userFromService, executor)
        } else {
            executor.submit<Fact> {
                factRepository.getFactByUserID(id) ?: getFact(user, executor)
            }.get()
        }.also {
            executor.shutdown()
        }
    }
}

fun main(args: Array<String>) {
    fun execute(userService: UserService, id: Int) {
        val (fact, time) = inTime {
            userService.getFact(id)
        }
        println("fact = $fact")
        println("time = $time ms.")
    }

    val userClient = MockUserClient()
    val factClient = MockFactClient()
    val userRepository = MockUserRepository()
    val factRepository = MockFactRepository()

    val futureUserService = FutureUserService(userClient, factClient, userRepository, factRepository)

    execute(futureUserService, 1)
    execute(futureUserService, 2)
    execute(futureUserService, 1)
    execute(futureUserService, 2)
    execute(futureUserService, 3)
    execute(futureUserService, 4)
    execute(futureUserService, 5)
    execute(futureUserService, 10)
    execute(futureUserService, 100)

}

