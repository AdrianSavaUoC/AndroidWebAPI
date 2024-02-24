package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityMainBinding
import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var theQuerry: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val choiceRange = mutableListOf("Random Wisdom", "I want to look by Id")
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


                if (theQuerry == "I want to look by Id") {

                    binding.textView.text = "Your pick: $theQuerry"
                }
                else {
                    binding.textView.text = "Your pick: $theQuerry"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }
}