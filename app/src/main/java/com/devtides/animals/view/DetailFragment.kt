package com.devtides.animals.view


import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.devtides.animals.R
import com.devtides.animals.databinding.FragmentDetailBinding
import com.devtides.animals.model.Animal
import com.devtides.animals.model.AnimalPalette
import com.devtides.animals.util.getProgressDrawable
import com.devtides.animals.util.loadImage


class DetailFragment : Fragment() {

    var animal: Animal? = null
    private lateinit var dataBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            animal = DetailFragmentArgs.fromBundle(it).animal
        }

        animal?.imageUrl?.let {
            setupBackgroundColor(it)
        }

        dataBinding.animal = animal
    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object: CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate() {palette ->
                            val intColor = palette?.lightMutedSwatch?.rgb ?: 0
                            dataBinding.palette = AnimalPalette(intColor)
                        }
                }

            })
    }


}
