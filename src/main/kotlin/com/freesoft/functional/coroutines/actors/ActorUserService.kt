package com.freesoft.functional.coroutines.actors

import com.freesoft.functional.coroutines.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.runBlocking

class ActorUserService(
        private val userClient: UserClient,
        private val factClient: FactClient,
        private val userRepository: UserRepository,
        private val factRepository: FactRepository) : UserService {

    sealed class UserMsg {
        data class GetFact(val id: UserID, val returner: SendChannel<Fact>) : UserMsg()
        data class MaybeUser(val id: UserID, val user: User?, val returner: SendChannel<Fact>) : UserMsg()
        data class InsertUser(val user: User) : UserMsg()
    }

    sealed class FactMsg {
        data class UserFromDb(val user: User, val inserted: Boolean, val returner: SendChannel<Fact>) : FactMsg()
        data class InsertFact(val fact: Fact) : FactMsg()
        data class SomeFact(val fact: Fact, val returner: SendChannel<Fact>) : FactMsg()
        data class GetFactFromService(val user: User, val returner: SendChannel<Fact>) : FactMsg()
    }

    @ObsoleteCoroutinesApi
    private fun CoroutineScope.factActor() = actor<FactMsg>(capacity = 64) {
        for (msg in channel) {
            when (msg) {
                is FactMsg.UserFromDb -> {
                    if (msg.inserted) {
                        channel.send(FactMsg.GetFactFromService(msg.user, msg.returner))
                    } else {
                        val fact = factRepository.getFactByUserID(msg.user.id)
                        if (fact == null) {
                            channel.send(FactMsg.GetFactFromService(msg.user, msg.returner))
                        } else {
                            channel.send(FactMsg.SomeFact(fact, msg.returner))
                        }
                    }
                }
                is FactMsg.GetFactFromService -> {
                    val fact = factClient.getFact(msg.user)
                    channel.send(FactMsg.InsertFact(fact))
                    channel.send(FactMsg.SomeFact(fact, msg.returner))
                }
                is FactMsg.SomeFact -> msg.returner.send(msg.fact)
                is FactMsg.InsertFact -> factRepository.insertFact(msg.fact)
            }
        }
    }

    private fun CoroutineScope.userActor(factActor: SendChannel<FactMsg>) = actor<UserMsg>(capacity = 64) {
        for (msg in channel) {
            when (msg) {
                is UserMsg.GetFact -> channel.send(UserMsg.MaybeUser(msg.id, userRepository.getUserById(msg.id), msg.returner))
                is UserMsg.MaybeUser -> {
                    val (user, inserted) = if (msg.user == null) {
                        val user = userClient.getUser(msg.id)
                        channel.send(UserMsg.InsertUser(user))
                        user to true
                    } else {
                        msg.user to false
                    }
                    factActor.send(FactMsg.UserFromDb(user, inserted, msg.returner))
                }
                is UserMsg.InsertUser -> userRepository.insertUser(msg.user)
            }
        }
    }

    @ObsoleteCoroutinesApi
    override fun getFact(id: UserID): Fact = runBlocking {
        val returner = Channel<Fact>()
        val userActor = userActor(factActor())
        userActor.send(UserMsg.GetFact(id, returner))
        returner.receive()
    }
}

fun main(args: Array<String>) = runBlocking {
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

    val userService = ActorUserService(userClient,
            factClient,
            userRepository,
            factRepository)

    execute(userService, 1)
    execute(userService, 2)
    execute(userService, 1)
    execute(userService, 2)
    execute(userService, 3)
    execute(userService, 4)
    execute(userService, 5)
    execute(userService, 10)
    execute(userService, 100)
}