package com.freesoft.functional.coroutines

import kotlin.concurrent.thread

class CallbacksMain

class CallbackUserClient(private val client: UserClient) {
    fun getUser(id: Int, callback: (User) -> Unit) {
        thread {
            callback(client.getUser(id))
        }
    }
}

class CallbackFactClient(private val client: FactClient) {
    fun get(user: User, callback: (Fact) -> Unit) {
        thread {
            callback(client.getFact(user))
        }
    }
}

class CallBackUserRepository(private val userRepository: UserRepository) {
    fun getUserById(id: UserID, callback: (User?) -> Unit) {
        thread {
            callback(userRepository.getUserById(id))
        }
    }

    fun insertUser(user: User, callback: () -> Unit) {
        thread {
            userRepository.insertUser(user)
            callback()
        }
    }
}

class CallBackFactRepository(private val factRepository: FactRepository) {
    fun getFactByUserId(id: Int, callback: (Fact?) -> Unit) {
        thread {
            callback(factRepository.getFactByUserID(id))
        }
    }

    fun insertFact(fact: Fact, callback: () -> Unit) {
        thread {
            factRepository.insertFact(fact)
            callback()
        }
    }
}

class CallBackUserService(
        private val userClient: CallbackUserClient,
        private val factClient: CallbackFactClient,
        private val userRepository: CallBackUserRepository,
        private val factRepository: CallBackFactRepository) : UserService {
    override fun getFact(id: UserID): Fact {
        var aux: Fact? = null
        userRepository.getUserById(id) { user ->
            if (user == null) {
                userClient.getUser(id) { userFromClient ->
                    userRepository.insertUser(userFromClient) {}
                    factClient.get(userFromClient) { fact ->
                        factRepository.insertFact(fact) {}
                        aux = fact
                    }
                }
            } else {
                factRepository.getFactByUserId(id) { fact ->
                    if (fact == null) {
                        factClient.get(user) { factFromClient ->
                            factRepository.insertFact(factFromClient) {}
                            aux = factFromClient
                        }
                    } else {
                        aux = fact
                    }
                }
            }
        }
        while (aux == null) {
            Thread.sleep(2)
        }
        return aux!!
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
    val callbackUserClient = CallbackUserClient(userClient)
    val factClient = MockFactClient()
    val callBackFactClient = CallbackFactClient(factClient)
    val userRepository = MockUserRepository()
    val callbackUserRepository = CallBackUserRepository(userRepository)
    val factRepository = MockFactRepository()
    val callBackFactRepository = CallBackFactRepository(factRepository)

    val callBackUserService = CallBackUserService(
            userClient = callbackUserClient,
            factClient = callBackFactClient,
            userRepository = callbackUserRepository,
            factRepository = callBackFactRepository)

    execute(callBackUserService, 1)
    execute(callBackUserService, 2)
    execute(callBackUserService, 1)
    execute(callBackUserService, 2)
    execute(callBackUserService, 3)
    execute(callBackUserService, 4)
    execute(callBackUserService, 5)
    execute(callBackUserService, 10)
    execute(callBackUserService, 100)
}
