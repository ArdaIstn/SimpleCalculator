package com.ardaisitan.calculatorapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ardaisitan.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var expression: Expression

    var error: Boolean = false
    var lastDigit: Boolean = false
    lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        resultText = binding.resultTv


        val buttonList = listOf(
            R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
            R.id.button_4, R.id.button_5, R.id.button_6,
            R.id.button_7, R.id.button_8, R.id.button_9
        )  // Numarali butonlari idleri ile birlikte liste icerisinde tuttum.

        buttonList.forEach { button_id -> // ButtonList'in icerisindeki id'lerden butonlara ulasip her bir butona tiklaninca ne olacagini belirttim.
            findViewById<Button>(button_id).setOnClickListener {
                if (!error) { //Herhangi bi error yoksa bu bloga girer.
                    lastDigit = true // Son girilen bir numara oldugu icin true olarak cevirdim.
                    resultText.append((it as Button).text) // append fonksiyonunu kullanarak her bir butonun icerisindeki texti alip resultText'e yanyana bir bicimde yazdirdim
                    //Bunun sebebi kullanicinin cok haneli sayi girmesini saglamaktir.
                }

            }
        }

        binding.buttonClear.setOnClickListener {
            if (error) {
                resultText.text = ""
                error = false
            }
            resultText.text =
                resultText.text.dropLast(1) //Text'in son elemanini silip texte geri esitliyorum.

        }

        binding.buttonAllClear.setOnClickListener {
            resultText.text = ""
            error = false

        }

        binding.buttonPlus.setOnClickListener {
            handleOperator("+")
        }

        binding.buttonSubtract.setOnClickListener {
            handleOperator("-")
        }

        binding.buttonDivide.setOnClickListener {
            handleOperator("/")
        }

        binding.buttonMultiply.setOnClickListener {
            handleOperator("*")
        }


        binding.buttonEqual.setOnClickListener {
            performCalculate()
        }
    }


    private fun handleOperator(operator: String) {
        if (resultText.text.isEmpty()) {
            resultText.text = ""  //  text bos iken bir operatore tiklarsam bu blok calisir.

        } else if (lastDigit && !error) { //Baslangicta lastDigit ve error false durumundadir.Herhangi bir numara girdigimde ise ustte belirttigim gibi true doner ve bu blok calisir.
            resultText.append(operator)
            lastDigit =
                false  // Artik son girilen degerimiz bir operator oldugu icin bu degeri false olarak ayarladim.
        } else if (!error) { //Error olmamasi durumunda bu blok calisir.
            resultText.text = resultText.text.dropLast(1).toString() + operator
            // Bu blokta kullanici en son bir operator girdiginde ve tekrar bir operator tusuna bastiginda yazilan operatorun yanina bir operator daha yazilmamasini saglar.
            // Ayni zamanda numarayi yazip operatore tusuna bastiktan sonra tekrar bir operator tusuna basar ise operatoru degistirme firsati sunar.
        }
    }

    private fun performCalculate() {
        if (resultText.text.isEmpty() || !lastDigit) { // Text bos ise veya texte girilen son veri bir numara degil ise yani operatorse bu blok calisir.
            Toast.makeText(
                this@MainActivity,
                "Please enter a number.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (error) { // error true ise bu blok calisir.
                Toast.makeText(
                    this@MainActivity,
                    "Please press the clear button.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val result =
                    calculateResult(resultText.text.toString()) // Text bos degilse ve herhangi bir error yoksa bu blok calisir.

                if (result % 1 == 0.0) {
                    resultText.text = result.toInt()
                        .toString() // Eger donen deger tamsayi ise ekrana tamsayi sonucu yazdiririz.
                } else {
                    resultText.text =
                            // Eger donen deger kusuratli bir sayi ise donen sayiyi kusuratli olarak ekrana yazdiririz.
                        result.toString()
                }

            }

        }

    }

    private fun calculateResult(result: String): Double {
        val convertedResult: Double

        return try {
            expression =
                ExpressionBuilder(result).build() //Bir expression nesnesi olusturup matematiksel ifadeyi degerlendiririz.
            // ExpressionBuilder sinifi basit aritmetik islemleri ve matematiksel ifadeleri degerlendirmek icin kullanilir.

            convertedResult =
                expression.evaluate() // Sonucu evaluate ile  degerlendirir ve  elde ederiz.

            convertedResult
        } catch (e: ArithmeticException) {
            error = true // Eger bir hata alir isek error ifadesi true olur.
            Toast.makeText(this@MainActivity, "Error!", Toast.LENGTH_SHORT).show()
            Double.NaN  // 0'a bolme hatasinde Not a Number doner.

        }


    }


}