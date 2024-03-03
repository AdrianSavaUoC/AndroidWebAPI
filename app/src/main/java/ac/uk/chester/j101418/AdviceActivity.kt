package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityAdviceBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner

class AdviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdviceBinding
    private lateinit var id: String
    private lateinit var advice: String

    private val advices = mutableListOf<AdviceData>()



    private val adviceList = mutableListOf<AdviceData>()

    private val resultLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult() ) {
            result ->
    }

    private fun loadMainActivity () {
        val intent = Intent (this, MainActivity::class.java)
        resultLauncher.launch(intent)
    }

    private fun loadShowAdviceListActivity (advices: List<AdviceData>) {
        val intent = Intent(this, ShowAdvicesListActivity::class.java)
        intent.putExtra("adviceList", ArrayList(advices))
        intent.putExtra("firstWord", getFirstWord())
        resultLauncher.launch(intent)
    }

    private fun getAdviceByWord(){
        val firstWord = getFirstWord()
        fetchData("https://api.adviceslip.com/advice/search/$firstWord")

    }

    private fun processJson4Advice (jsonString: String)  {
        try {
            val jsonObject = JSONObject(jsonString)
            val slipsArray = jsonObject.getJSONArray("slips")

            for (i in 0 until slipsArray.length()) {
                val slipObject = slipsArray.getJSONObject(i)
                val id = slipObject.getInt("id").toString()
                val advice = slipObject.getString("advice")
                advices.add(AdviceData(id, advice))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun fetchData(urlString: String) {
        val thread = Thread {
            try {
                val url = URL(urlString)
                val connection : HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 3000
                connection.readTimeout = 3000
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val scanner = Scanner(connection.inputStream).useDelimiter("\\A")
                    val text = if (scanner.hasNext()) scanner.next() else ""

                    val advice = processJson4Advice(text)
                    loadShowAdviceListActivity(advices)


                }

            }
            catch (e: IOException) {
                updateTextView("An error occurred while retrieving data from the server. This error: $e")
            }

        }
        thread.start()
    }

    private fun updateTextView (text: String) {
        binding.tvContinueAdvice.text = text
    }



    private fun getFirstWord () : String? {
        val advice = intent.getStringExtra("advice")
        val words = advice?.split(Regex("[ ,?;.:!]+"))?.toMutableList()
        return words?.firstOrNull()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdviceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val adviceId = intent.getStringExtra("id")
        val adviceText = intent.getStringExtra("advice")

        if (adviceId != null) {
            id = adviceId
        }
        if (adviceText != null) {
            advice = adviceText
        }

        val newAdvice = AdviceData(id, advice)
        adviceList.add(newAdvice)

        binding.tvAdvice.text = newAdvice.advice
        val firstWord = getFirstWord()
        binding.tvContinueAdvice.text = "As a bonus, we can search all the advices that contain the first word of the above advice: $firstWord"

        binding.buttonSearchAdviceByWord.setOnClickListener {
            getAdviceByWord()
        }

        binding.buttonHome.setOnClickListener {
            loadMainActivity()
        }


    }
}