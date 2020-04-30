package com.bignerdranch.android.beatbox

import android.os.Bundle
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.beatbox.databinding.ActivityMainBinding
import com.bignerdranch.android.beatbox.databinding.ListItemSoundBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private val beatBoxViewModel: BeatBoxViewModel by lazy {
        ViewModelProvider(this).get(BeatBoxViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beatBoxViewModel.beatBox = BeatBox(assets)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.seekBar.setOnSeekBarChangeListener(this)


        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context,3)
            adapter = SoundAdapter(beatBoxViewModel.beatBox!!.sounds)

        }
    }

    /**
     * To allow sound to work on device screen destry, delete [onDestroy]
     */
    override fun onDestroy() {
        super.onDestroy()
        beatBoxViewModel.beatBox?.release()
    }
    private inner class SoundHolder(private val binding: ListItemSoundBinding):
            RecyclerView.ViewHolder(binding.root){
        init {
            binding.viewmodel = SoundViewModel(beatBoxViewModel.beatBox!!)

        }
        fun bind(sound: Sound){
            binding.apply {
                viewmodel?.sound = sound

                executePendingBindings()
            }
        }
    }

    private inner class SoundAdapter(private val sounds: List<Sound>): RecyclerView.Adapter<SoundHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                layoutInflater,R.layout.list_item_sound,parent,false)
            return SoundHolder(binding)
        }

        override fun getItemCount(): Int = sounds.size
        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
        val sound = sounds[position]
        holder.bind(sound)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        playback_speed_text.text = getString(R.string.playback_speed, seekBar?.progress)
        beatBoxViewModel.beatBox!!.speed = progress
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        beatBoxViewModel.beatBox!!.speed
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        beatBoxViewModel.beatBox!!.speed
    }
}
