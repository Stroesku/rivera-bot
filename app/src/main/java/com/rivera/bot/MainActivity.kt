package com.rivera.bot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.extensions.filters.Filter
import com.github.kotlintelegrambot.network.fold
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        startBot(lifecycleScope)

    }
}

@IgnoreExtraProperties
data class Player(
    val id: Int = 0,
    val first_name: String = "",
    val last_name: String = "",
    val tg_id: String = "",
    val date_of_birth: String = ""
)


fun DatabaseReference.getPlayersList(onSuccess: (List<Player>) -> Unit) {
    ref.child("players").get().addOnSuccessListener {
        onSuccess(it.getValue<HashMap<String, Player>>()?.values?.toList() ?: emptyList())
    }
}

fun DatabaseReference.addNewPlayer(player: Player) {
    child("players").push()
        .setValue(this)
}

fun startBot(scope: LifecycleCoroutineScope) {

    val myToken = "5887474048:AAEfiXl6yhkEm2UlO2u7Cdmmz-Mz0K8rQPc"

    val db = Firebase.database.reference

    val bot = bot {
        token = myToken
        dispatch {
            // /start
            command("start") {
                db.getPlayersList {
                    DtkLog.print(tag = "duotek", "startBot ${it.size}")
                }
            }

            command("добавить_игрока") {
                var firstName = ""
                var lastName = ""
                var date_of_birth = ""

                val resultName = bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "Введите имя игрока:"
                )
                resultName.fold({

                },{
                    // do something with the error
                })
            }

            command("добавить_тренера") {
                val result =
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "coach added!")
                if (result.first?.isSuccessful == true) {
                    db.child("coaches").setValue("Константин Владимирович Шемонаев")
                }
            }

            command("remove_player") {
                val result =
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "removed!")
            }


            message(Filter.Text) {
                println("message 1 ${this.message.text}")
            }
        }
    }

    bot.startPolling()
}