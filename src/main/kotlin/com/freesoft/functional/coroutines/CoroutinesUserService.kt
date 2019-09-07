package com.freesoft.functional.coroutines

import kotlinx.coroutines.*

class CoroutinesUserService(
        private val userClient: UserClient,
        private val factClient: FactClient,
        private val userRepository: UserRepository,
        private val factRepository: FactRepository) : UserService {
    override fun getFact(id: UserID): Fact = runBlocking {
        val user = async { userRepository.getUserById(id) }.await()
        if (user == null) {
            val userFromService = async { userClient.getUser(id) }.await()
            launch { userRepository.insertUser(userFromService) }
            getFact(userFromService)
        } else {
            async {
                factRepository.getFactByUserID(id) ?: getFact(user)
            }.await()
        }
    }

    private suspend fun getFact(user: User): Fact {
        val fact: Deferred<Fact> = withContext(Dispatchers.Default) {
            async { factClient.getFact(user) }
        }
        coroutineScope {
            launch { factRepository.insertFact(fact.await()) }
        }
        return fact.await()

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

    val coroutinesUserService = CoroutinesUserService(userClient, factClient, userRepository, factRepository)

    execute(coroutinesUserService, 1)
    execute(coroutinesUserService, 2)
    execute(coroutinesUserService, 1)
    execute(coroutinesUserService, 2)
    execute(coroutinesUserService, 3)
    execute(coroutinesUserService, 4)
    execute(coroutinesUserService, 5)
    execute(coroutinesUserService, 10)
    execute(coroutinesUserService, 100)
}