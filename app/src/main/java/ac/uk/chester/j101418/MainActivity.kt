package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityMainBinding
import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var theQuerry: String

    private val resultLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult() ) {
            result ->
    }

    private fun loadAdviceAcrivity (advice: String) {

        val intent = Intent(this, AdviceActivity::class.java)
        intent.putExtra("advice", advice)
        resultLauncher.launch(intent)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val choiceRange = mutableListOf("Advice", "Quote")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, choiceRange)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spFirstChoice.adapter = adapter

        binding.spFirstChoice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val choice = choiceRange[position]
                theQuerry = choice
                val yourPick = "Your pick"

                if (theQuerry == "Quote") {
                    binding.textView.text = "$yourPick: $theQuerry"
                }
                else {
                    binding.textView.text = "$yourPick: $theQuerry"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.buttonGO.setOnClickListener {
            loadAdviceAcrivity("dummy")
        }



    }
}