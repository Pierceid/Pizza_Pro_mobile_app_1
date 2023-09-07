package com.example.pizza_pro.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pizza_pro.databinding.FragmentDetailBinding

@Suppress("DEPRECATION")
class DetailFragment : Fragment(){

    private lateinit var binding: FragmentDetailBinding
    private lateinit var name: String
    private lateinit var rating: String
    private lateinit var time: String
    private lateinit var calories: String
    private lateinit var description: String
    private var imageSource: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            name = String.format("%s", it.getString("name"))
            rating = String.format("%.1f", it.getDouble("rating"))
            time = String.format("%d min", it.getInt("time"))
            calories = String.format("%d cal", it.getInt("calories"))
            description = String.format("%s", it.getString("description"))
            imageSource = it.getInt("imageSource")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener {
            requireFragmentManager().popBackStack()
        }
        updateDetail()
    }

    // updates detail fragment
    private fun updateDetail() {
        binding.ivPicture.setImageResource(imageSource)
        binding.name = name
        binding.rating = rating
        binding.time = time
        binding.calories = calories
        binding.description = description
    }
}