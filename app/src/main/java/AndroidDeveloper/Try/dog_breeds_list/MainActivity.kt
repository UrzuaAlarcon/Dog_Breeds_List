package AndroidDeveloper.Try.dog_breeds_list

import AndroidDeveloper.Try.dog_breeds_list.databinding.ActivityMainBinding
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private val dogImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svDogs.setOnQueryTextListener(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = DogAdapter(dogImages)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query: String) {

        CoroutineScope(Dispatchers.IO).launch {

            val call: Response<DogResponse> =
                getRetrofit().create(APiService::class.java).getDogsByBreeds("$query/images")

            val puppies = call.body()

            runOnUiThread {
                if (call.isSuccessful) {
                    val images = puppies?.images ?: emptyList()
                    dogImages.clear()
                    dogImages.addAll(images)
                    adapter.notifyDataSetChanged()
                } else {
                    showError()
                }

                hideKeybord()

            }

        }

    }

    private fun hideKeybord() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    private fun showError() {

        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()

    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        if (!query.isNullOrEmpty()) {

            searchByName(query.lowercase())

        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }
}