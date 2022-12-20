package dgitc.unam.juego

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Flow
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.random.Random.Default.nextLong

class Pruebas {
    fun main() {
        //version uno
        funcionlambda(3,4,{regresa ->
            println(regresa)
        })
        //version dos
        funcionlambda(3,4){regresa ->
            println(regresa)
        }

        println(hilo(4,5))

        hilolambda(4,7){
            println(it)
        }

        coroutines()

        coroutines2()

        bloque()

        launch()

        asyncc()

        job()

        deffered()

        dispatchers()

        dispatchers2()

        dispatchers3()

        dispatchers4()

        dispatchers5()

        dispatchers6()

        dispatchers7()

        floww()

    }
    fun funcionlambda(a:Int,b:Int,callback:(result:Int)->Unit) {
        callback(a+b)
    }


    fun hilo(a:Int,b:Int):Int{
        var result=0
        thread {
            Thread.sleep(Random.nextLong(1000,3000))
            result=a+b
        }
        //Thread.sleep(4000)
        return result
    }

    fun hilolambda(a:Int,b:Int,callback:(result:Int)->Unit){
        var result=0
        thread {
            Thread.sleep(Random.nextLong(1000,3000))
            result=a+b
            callback(result)
        }
        println("Ejecuta más lineas")
    }

    fun coroutines() {
        runBlocking {
            (1..1000000).forEach {
                launch {
                    delay(1000)
                    print("0")
                }
            }
        }
    }

    fun coroutines2() {
        GlobalScope.launch {
            while(true){
                println("Código de la coroutina ${Thread.currentThread().name} ejecutando")
            }
        }
        Thread.sleep(2000)
    }

    fun bloque() {
        runBlocking {
            println("inicia ${Thread.currentThread().name}")
            delay(2000)
            println("Ejecución de código")
            println("termina ${Thread.currentThread().name}")
        }
    }

    fun launch() {
        runBlocking {
            launch {
                println("inicia ${Thread.currentThread().name}")
                delay(1000)
                println("Ejecución de código 1")
                println("termina ${Thread.currentThread().name}")
            }
            launch {
                println("inicia ${Thread.currentThread().name}")
                delay(3000)
                println("Ejecución de código 2")
                println("termina ${Thread.currentThread().name}")
            }
            println("inicia ${Thread.currentThread().name}")
            delay(2000)
            println("Ejecución de código 3")
            println("termina ${Thread.currentThread().name}")
        }
    }

    fun asyncc() {
        runBlocking {
            val result=async {
                println("inicia 1 ${Thread.currentThread().name}")
                delay(3000)
                println("Ejecución de código 1")
                println("termina 1 ${Thread.currentThread().name}")
                "regreso una cadena"
            }
            println("Esperando el resultado")
            println("Resultado:= ${result.await()}")
            println("inicia 2 ${Thread.currentThread().name}")
            delay(2000)
            println("Ejecución de código 2")
            println("termina 2 ${Thread.currentThread().name}")
        }
    }

    fun job() {
        runBlocking {
            val job=launch {
                println("inicia 1 ${Thread.currentThread().name}")
                delay(6000)
                println("Ejecución de código 1")
                println("termina 1 ${Thread.currentThread().name}")
            }
            launch {
                while(true){
                    delay(1000)
                    println("Esta activo: ${job.isActive}")
                    println("Es cancelado: ${job.isCancelled}")
                    println("Es completado: ${job.isCompleted}")
                    //código para cancelar
                    if((1..5).shuffled().first()==3){
                        println("Cancelar el job")
                        job.cancel()
                    }
                }
            }
        }
    }


    fun deffered() {
        runBlocking {
            val deferred=async {
                println("inicia 1 ${Thread.currentThread().name}")
                delay(6000)
                println("Ejecución de código 1")
                println("termina 1 ${Thread.currentThread().name}")
                "regreso una cadena"
            }
            launch {
                while(true){
                    delay(1000)
                    println("Esta activo: ${deferred.isActive}")
                    println("Es cancelado: ${deferred.isCancelled}")
                    println("Es completado: ${deferred.isCompleted}")
                    //código para cancelar
                    if((1..5).shuffled().first()==3){
                        println("Cancelar el deferred")
                        deferred.cancel()
                    }
                    if((1..5).shuffled().first()==1){
                        println("Esperan el valor: ${deferred.await()}")
                    }
                }
            }
        }
    }

    fun dispatchers() {
        runBlocking {
            launch {
                println("Otro subproceso")
            }
            launch(Dispatchers.IO) {
                println("IO")
            }
        }
    }

    fun dispatchers2() {
        runBlocking {
            launch {
                println("Otro subproceso")
            }
            launch(Dispatchers.Default) {
                println("default")
            }
        }
    }

    fun dispatchers3() {
        runBlocking {
            launch {
                println("Otro subproceso")
            }
            launch(Dispatchers.Main) {
                println("main")
            }
        }
    }

    fun dispatchers4() {
        runBlocking {
            launch {
                println("Otro subproceso")
            }
            launch(newSingleThreadContext("Personalizada")) {
                println("mi corrutina ${Thread.currentThread().name}")
            }
            newSingleThreadContext("segunda personalizada").use { contexto->
                launch(contexto) {
                    println("corrutina ${Thread.currentThread().name}")
                }
            }
        }
    }

    fun dispatchers5() {
        runBlocking {
            println("nombre corrutina ${Thread.currentThread().name}")
            withContext(newSingleThreadContext("personalizado con with context")) {
                println("WithContext")
                delay(2000)
                println("nombre corrutina: ${Thread.currentThread().name}")
                println("termina corrutina: ${Thread.currentThread().name}")
            }
            println("termina corrutina: ${Thread.currentThread().name}")
        }
    }

    fun dispatchers6() {
        runBlocking {
            println("nombre corrutina ${Thread.currentThread().name}")
            withContext(newSingleThreadContext("personalizado con with context")) {
                println("WithContext")
                delay(2000)
                println("nombre corrutina: ${Thread.currentThread().name}")
                println("termina corrutina: ${Thread.currentThread().name}")
            }
            withContext(Dispatchers.Default) {
                println("WithContext")
                delay(1000)
                println("Uso del CPU: ${Thread.currentThread().name}")
                println("termina corrutina: ${Thread.currentThread().name}")
            }
            println("termina corrutina: ${Thread.currentThread().name}")
        }
    }

    fun dispatchers7() {
        println("Secuencias")
        crearSecuencias().forEach {
            println("$it datos regresados")
        }
    }
    fun crearSecuencias():Sequence<Int>{
        return sequence {
            (1..6).forEach {
                println("Emulando el procesamiento de datos")
                Thread.sleep(2000)
                yield(it+Random.nextInt(20,60))
            }
        }
    }

    fun floww() {
        println("Flow")
        runBlocking {
            launch {
                crearSecuenciasFlow().collect {
                    println("datos $it")
                }
            }
            launch {
                (1..10).forEach {
                    delay(300)
                    println("proceso dos")
                }
            }
        }
    }
    fun crearSecuenciasFlow(): Flow<Int> {
        return flow {
            (1..6).forEach {
                println("Emulando el procesamiento de datos")
                delay(2000)
                emit(it + Random.nextInt(20, 60))
            }
        }
    }


}

