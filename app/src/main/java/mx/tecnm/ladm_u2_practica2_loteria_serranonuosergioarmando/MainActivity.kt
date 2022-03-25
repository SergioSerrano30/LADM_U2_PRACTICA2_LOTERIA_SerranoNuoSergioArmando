package mx.tecnm.ladm_u2_practica2_loteria_serranonuosergioarmando

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import kotlinx.coroutines.*
import mx.tecnm.ladm_u2_practica2_loteria_serranonuosergioarmando.databinding.ActivityMainBinding
import java.lang.Exception
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var bindin:ActivityMainBinding
    lateinit var cartas:Array<Carta>
    lateinit var turnos: ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        var status = ""
        cartas = Array<Carta>(54,{ Carta("",0,0) })
        bindin = ActivityMainBinding.inflate(layoutInflater)
        llenarCartas()
        var conta = 0
        var ejecutar = true
        var pausar = false
        var faltaron = false
        var audio:MediaPlayer?=null



        val scope = CoroutineScope(Job() + Dispatchers.Main)

        val corruDarCartas = scope.launch(EmptyCoroutineContext, CoroutineStart.LAZY){  //No va a iniciar hasta que invoque el start
            var tiempo = 0L
            while (ejecutar){
                bindin.btnIniciar.isVisible = true
                delay(7000L)
                while (conta<=cartas.size-1){
                    if (pausar){
                        this@MainActivity.setTitle("¡LOTERIA!")
                        tiempo = 4000L
                        delay(tiempo)
                    }
                    else{
                        try {
                            audio = MediaPlayer.create(this@MainActivity,cartas[turnos[conta]].audio)
                        }catch (e:Exception){}
                        runOnUiThread {
                            this@MainActivity.setTitle("Carta: ${conta+1} de ${cartas.size}")
                            bindin.lblCarta.text = cartas[turnos[conta]].nombre
                            bindin.imgVCarta.setImageResource(cartas[turnos[conta]].imagen)
                        try {
                                audio?.start()
                            }catch (e:Exception){}
                        }
                        if (faltaron){
                            this@MainActivity.setTitle("Faltaron --> Carta: ${(conta++)+1} de ${cartas.size}")
                            tiempo = 2500L; bindin.btnIniciar.isVisible = false
                        }
                        else {
                            conta++
                            tiempo = 4000L}
                        delay(tiempo)
                    }
                }
                //delay(1000L)
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(bindin.root)
        this.setTitle("Loteria: Sergio Serrano")

        bindin.btnIniciar.setOnClickListener {
            if (status.equals("")) {
                bindin.btnIniciar.text = "¡Loteria!"
                status = "iniciado"
                val hilo:Hilo = Hilo(cartas.size)
                var audio_barajeo:MediaPlayer?=null
                var audio_inicio:MediaPlayer?=null
                try {
                    hilo.start()
                    turnos = hilo.turnos()
                    audio_barajeo = MediaPlayer.create(this,R.raw.barajeo_audio)
                    audio_barajeo.start()

                    audio_inicio = MediaPlayer.create(this,R.raw.inicio_juego)
                    audio_inicio.start()


                } catch (e: Exception) {
                    Toast.makeText(this, "Error al barajear \n${e}", Toast.LENGTH_LONG).show()
                }
                try {
                    conta=0
                    pausar=false
                    faltaron = false
                    corruDarCartas.start()
                }catch (e:Exception){
                    Toast.makeText(this,"Error al iniciar cartas",Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }//Parte iniciar
           if (status.equals("iniciado")){
                status = "faltaron"
                bindin.btnIniciar.text = "Las que faltaron"
                pausar=true
                faltaron=false
                return@setOnClickListener
            }

            if (status.equals("faltaron")){
            bindin.btnIniciar.text = "Nuevo juego"
            status = "fin"
                faltaron=true
                pausar =false
                try {
                    var audio_fin:MediaPlayer?=null
                    audio_fin = MediaPlayer.create(this,R.raw.fin_juego)
                    audio_fin.start()
                }catch (e:Exception){}
            return@setOnClickListener
            }
            if (status.equals("fin")){
                bindin.btnIniciar.text = "Iniciar"
                bindin.imgVCarta.setImageResource(0)
                bindin.lblCarta.text = ""
                status = ""
                this.setTitle("Loteria: Sergio Serrano")
                //hilo.continuarHilo()
                return@setOnClickListener
            }
        }
    }

    private fun llenarCartas() {
        val nomCartas = arrayOf("1 El gallo","2  El Diablito","3  La Dama","4  El catrín","5  El paraguas",
            "6  La sirena","7  La escalera","8  La botella","9  El barril","10 El árbol",
            "11 El melón","12 El valiente","13 El gorrito","14 La muerte","15 La pera",
            "16 La bandera","17 El bandolón","18 El violoncello","19 La garza","20 El pájaro",
            "21 La mano","22 La bota","23 La luna","24 El cotorro","25 El borracho",
            "26 El negrito","27 El corazón","28 La sandía","29 El tambor","30 El camarón",
            "31 Las jaras","32 El músico","33 La araña","34 El soldado","35 La estrella",
            "36 El cazo","37 El mundo","38 El apache","39 El nopal","40 El alacrán",
            "41 La rosa","42 La calavera","43 La campana","44 El cantarito","45 El venado",
            "46 El sol","47 La corona","48 La chalupa","49 El pino","50 El pescado",
            "51 La palma","52 La maceta","53 El arpa","54 La rana")
        val imgCartas = arrayOf(R.drawable.carta1,R.drawable.carta2,R.drawable.carta3,R.drawable.carta4,R.drawable.carta5,
            R.drawable.carta6,R.drawable.carta7,R.drawable.carta8,R.drawable.carta9,R.drawable.carta10,
            R.drawable.carta11,R.drawable.carta12,R.drawable.carta13,R.drawable.carta14,R.drawable.carta15,
            R.drawable.carta16,R.drawable.carta17,R.drawable.carta18,R.drawable.carta19,R.drawable.carta20,
            R.drawable.carta21,R.drawable.carta22,R.drawable.carta23,R.drawable.carta24,R.drawable.carta25,
            R.drawable.carta26,R.drawable.carta27,R.drawable.carta28,R.drawable.carta29,R.drawable.carta30,
            R.drawable.carta31,R.drawable.carta32,R.drawable.carta33,R.drawable.carta34,R.drawable.carta35,
            R.drawable.carta36,R.drawable.carta37,R.drawable.carta38,R.drawable.carta39,R.drawable.carta40,
            R.drawable.carta41,R.drawable.carta42,R.drawable.carta43,R.drawable.carta44,R.drawable.carta45,
            R.drawable.carta46,R.drawable.carta47,R.drawable.carta48,R.drawable.carta49,R.drawable.carta50,
            R.drawable.carta51,R.drawable.carta52,R.drawable.carta53,R.drawable.carta54,)
        val audCartas = arrayOf(R.raw.gallo,R.raw.diablito,R.raw.dama,R.raw.catrin,R.raw.paraguas,
            R.raw.sirena,R.raw.escalera,R.raw.botella,R.raw.barril,R.raw.arbol,
            R.raw.melon,R.raw.valiente,R.raw.gorrito,R.raw.muerte,R.raw.pera,
            R.raw.bandera,R.raw.bandolon,R.raw.violoncello,R.raw.garza,R.raw.pajarito,
            R.raw.mano,R.raw.bota,R.raw.luna,R.raw.cotorro,R.raw.borracho,
            R.raw.negrito,R.raw.corazon,R.raw.sandia,R.raw.tambor,R.raw.camaron ,
            R.raw.jaras,R.raw.musico,R.raw.arana,R.raw.soldado,R.raw.estrella,
            R.raw.cazo,R.raw.mundo,R.raw.apache,R.raw.nopal,R.raw.alacran,
            R.raw.rosa,R.raw.calavera,R.raw.campana,R.raw.cantarito,R.raw.venado,
            R.raw.sol,R.raw.corona,R.raw.chalupa,R.raw.pino,R.raw.pescado,
            R.raw.palma,R.raw.maceta,R.raw.arpa,R.raw.rana)
        (0..nomCartas.size-1).forEach {
            cartas[it].nombre = nomCartas[it]
            cartas[it].imagen = imgCartas[it]
            cartas[it].audio = audCartas[it]
        }
    }
}//MainClass
    class Hilo(tam:Int):Thread(){
        var turnos = ArrayList<Int>()
        var tam = tam
        override fun run() {
            super.run()
                Log.e("Entrando","Barajeando")
                barajear()
                sleep(1000)
                Log.e("Fin","Hilo Terminado")
        }
        fun barajear(){
            (0..tam-1).forEach {
                try {
                    turnos.add(generar(turnos))
                }catch (e:Exception){}
            }
        }
        fun generar(a:ArrayList<Int>):Int{
            var num = Random.nextInt(tam)
            if(!a.contains(num)){
                return num
            }
            else{
                return generar(a)
            }

        }
        fun turnos():ArrayList<Int>{
            return turnos
        }
    }